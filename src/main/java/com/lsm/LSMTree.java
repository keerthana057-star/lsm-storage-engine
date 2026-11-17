package com.lsm;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class LSMTree {

    private final WAL wal;
    private final MemTable memTable;
    private final BloomFilter bloomFilter;
    private final String dataDirectory;
    private final int capacity;

    private final List<SSTable> sstables = new ArrayList<>();
    private final List<SSTableIndex> sstableIndexes = new ArrayList<>();
    private int sstableCount = 0;

    public LSMTree(String dataDirectory, int capacity)
            throws IOException {

        if (capacity <= 0) {
            throw new IllegalArgumentException(
                    "Capacity must be greater than zero");
        }

        this.dataDirectory = dataDirectory;
        this.capacity = capacity;

        Files.createDirectories(Path.of(dataDirectory));

        wal = new WAL(dataDirectory + "/wal.log");
        bloomFilter = new BloomFilter(capacity * 2);
        memTable = new MemTable();

        loadSSTables();
        recoverFromWAL();
    }

    private void loadSSTables() throws IOException {
        Path directory = Path.of(dataDirectory);

        try (var files = Files.list(directory)) {
            List<Path> tableFiles = files
                    .filter(path -> path.getFileName().toString()
                            .matches("sstable-\\d+\\.txt"))
                    .sorted(Comparator.comparingInt(
                            path -> getTableNumber(path)))
                    .toList();

            for (Path path : tableFiles) {
                SSTable sstable = new SSTable(path.toString());
                sstables.add(sstable);

                SSTableIndex index = new SSTableIndex();
                index.build(path.toString());
                sstableIndexes.add(index);

                sstableCount = Math.max(
                        sstableCount, getTableNumber(path));

                Map<String, String> data = sstable.readAll();
                for (String key : data.keySet()) {
                    bloomFilter.add(key);
                }
            }
        }
    }

    private int getTableNumber(Path path) {
        String name = path.getFileName().toString();
        return Integer.parseInt(
                name.substring(8, name.length() - 4));
    }

    private void recoverFromWAL() throws IOException {
        List<String> records = wal.readAll();

        if (records.isEmpty()) {
            return;
        }

        System.out.println("=== CRASH RECOVERY ===");
        System.out.println("Reading WAL records and replaying operations...");

        for (String record : records) {
            String[] parts = record.split("\\|", 3);

            if (parts.length != 3) {
                continue;
            }

            String operation = parts[0];
            String key = parts[1];
            String value = parts[2];

            System.out.println("Replay: " + operation + " " + key + " " + value);

            if (operation.equals("PUT")) {
                memTable.put(key, value);
            } else if (operation.equals("DELETE")) {
                memTable.delete(key);
            }

            if (memTable.isFull(capacity)) {
                flush();
            }
        }

        System.out.println("Restored MemTable in RAM: " + memTable.getAll());
    }

    public void put(String key, String value) throws IOException {
        // STAGE: WAL
        wal.append("PUT", key, value);
        System.out.println("WAL: PUT|" + key + "|" + value);

        // STAGE: MEMTABLE
        memTable.put(key, value);

        System.out.println("MemTable Size: "
                + memTable.size() + "/" + capacity);

        // STAGE: SIZE CHECK & FLUSH TRIGGER
        if (memTable.isFull(capacity)) {
            System.out.println("Capacity reached. Flush triggered!");
            flush();
        }
    }

    public String get(String key) throws IOException {
        // STAGE: GET - Check MemTable first
        String value = memTable.get(key);

        if (value != null) {
            return value.equals(MemTable.TOMBSTONE) ? null : value;
        }

        // STAGE: BLOOM FILTER - Check if key likely exists
        if (!bloomFilter.mightContain(key)) {
            System.out.println("Bloom Filter says Definitely NOT contain key: "
                    + key);
            return null;
        }

        // STAGE: SEARCH SSTABLES (using SSTableIndex)
        for (int i = sstables.size() - 1; i >= 0; i--) {
            SSTableIndex index = sstableIndexes.get(i);
            Integer lineNumber = index.getLineNumber(key);

            if (lineNumber == null) {
                // Key not in this SSTable
                continue;
            }

            value = sstables.get(i).get(key);

            if (value != null) {
                return value.equals(MemTable.TOMBSTONE)
                        ? null : value;
            }
        }

        return null;
    }

    public void delete(String key) throws IOException {
        wal.append("DELETE", key, "");
        memTable.delete(key);

        if (memTable.isFull(capacity)) {
            flush();
        }
    }

    public void flush() throws IOException {
        if (memTable.size() == 0) {
            return;
        }

        // STAGE: FLUSH - Print SSTable content being written
        String fileName = dataDirectory + "/sstable-"
                + (++sstableCount) + ".txt";

        SSTable sstable = new SSTable(fileName);
        sstable.write(memTable.getAll());

        System.out.println("SSTable written to disk (sorted by key):");
        for (Map.Entry<String, String> entry : memTable.getAll().entrySet()) {
            System.out.println(entry.getKey() + "|" + entry.getValue());
        }

        sstables.add(sstable);

        SSTableIndex index = new SSTableIndex();
        index.build(fileName);
        sstableIndexes.add(index);

        // Add all keys to Bloom Filter
        Map<String, String> data = memTable.getAll();
        for (String key : data.keySet()) {
            bloomFilter.add(key);
        }

        wal.clear();

        memTable.clear();
        System.out.println("MemTable after flush: " + memTable.getAll());
    }

    public void compact() throws IOException {
        if (sstables.size() <= 1) {
            return;
        }

        System.out.println("=== COMPACTION ===");
        System.out.println("Merging " + sstables.size() + " SSTables...");

        // Print each SSTable content before compaction
        for (int i = 0; i < sstables.size(); i++) {
            System.out.print("SSTable " + (i + 1) + ": ");
            Map<String, String> data = sstables.get(i).readAll();
            boolean first = true;
            for (Map.Entry<String, String> entry : data.entrySet()) {
                if (!first) System.out.print(", ");
                System.out.print(entry.getKey() + "|" + entry.getValue());
                first = false;
            }
            System.out.println();
        }

        String outputFile = dataDirectory + "/sstable-"
                + (++sstableCount) + ".txt";

        Compaction compaction = new Compaction();
        compaction.compact(sstables, outputFile);

        SSTable mergedTable = new SSTable(outputFile);

        SSTableIndex mergedIndex = new SSTableIndex();
        mergedIndex.build(outputFile);

        // Remember paths of old SSTable files before merging
        List<Path> oldFiles = new ArrayList<>();
        for (SSTable s : sstables) {
            oldFiles.add(s.getPath());
        }

        sstables.clear();
        sstables.add(mergedTable);
        sstableIndexes.clear();
        sstableIndexes.add(mergedIndex);

        // Delete old SSTable files from disk (never delete merged output)
        List<String> deletedFiles = new ArrayList<>();
        for (Path f : oldFiles) {
            if (Files.deleteIfExists(f)) {
                deletedFiles.add(f.getFileName().toString());
            }
        }
        System.out.println("Deleted old SSTables: " + deletedFiles);

        // Print merged result
        System.out.println("After Compaction (merged & deduplicated, keeping latest):");
        Map<String, String> mergedData = mergedTable.readAll();
        for (Map.Entry<String, String> entry : mergedData.entrySet()) {
            System.out.println(entry.getKey() + "|" + entry.getValue());
        }
    }

    public Map<String, String> getMemTableData() {
        return memTable.getAll();
    }
}
