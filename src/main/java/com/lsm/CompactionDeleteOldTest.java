package com.lsm;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class CompactionDeleteOldTest {

    public static void main(String[] args) throws IOException {
        String dir = "data/compaction-delete-test";

        // Clean folder
        Path folder = Path.of(dir);
        if (Files.exists(folder)) {
            try (var files = Files.list(folder)) {
                files.forEach(p -> {
                    try {
                        Files.deleteIfExists(p);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
            Files.deleteIfExists(folder);
        }
        Files.createDirectories(folder);

        // Instance 1: two flushes with overlapping key
        LSMTree lsm = new LSMTree(dir, 2);
        lsm.put("fruit", "Apple");
        lsm.put("color", "Blue");    // flush -> sstable-1
        lsm.put("fruit", "Mango");
        lsm.put("city", "Chennai");  // flush -> sstable-2

        System.out.println("Files BEFORE compact: " + listFiles(dir));

        lsm.compact();

        System.out.println("Files AFTER compact: " + listFiles(dir));

        // Restart: new instance on same folder
        LSMTree restarted = new LSMTree(dir, 2);
        System.out.println("After restart get(\"fruit\") -> " + restarted.get("fruit"));
    }

    private static List<String> listFiles(String dir) throws IOException {
        try (var files = Files.list(Path.of(dir))) {
            return files.map(p -> p.getFileName().toString()).sorted().toList();
        }
    }
}
