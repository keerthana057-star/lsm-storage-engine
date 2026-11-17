package com.lsm;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Compaction {

    public void compact(
            List<SSTable> sstables,
            String outputFile
    ) throws IOException {

        Map<String, String> mergedData = new TreeMap<>();

        // Merge SSTables from oldest to newest.
        // Newer values overwrite older values.
        for (SSTable sstable : sstables) {
            mergedData.putAll(sstable.readAll());
        }

        // Write merged, sorted data to a new SSTable.
        SSTable compactedTable = new SSTable(outputFile);
        compactedTable.write(mergedData);
    }
}
