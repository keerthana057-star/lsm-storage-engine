package com.lsm;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class CompactionTest {

    public static void main(String[] args) throws IOException {

        SSTable oldTable = new SSTable("data/compact-test/old.txt");
        SSTable newTable = new SSTable("data/compact-test/new.txt");

        Map<String, String> oldData = new TreeMap<>();
        oldData.put("fruit", "Apple");
        oldData.put("color", "Blue");

        Map<String, String> newData = new TreeMap<>();
        newData.put("fruit", "Mango");
        newData.put("city", "Chennai");

        oldTable.write(oldData);
        newTable.write(newData);

        Compaction compaction = new Compaction();
        compaction.compact(
                List.of(oldTable, newTable),
                "data/compact-test/merged.txt"
        );

        SSTable mergedTable
                = new SSTable("data/compact-test/merged.txt");

        System.out.println("Merged entries: " + mergedTable.readAll());
        System.out.println("Latest fruit: " + mergedTable.get("fruit"));
    }
}
