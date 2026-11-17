package com.lsm;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

public class SSTableTest {

    public static void main(String[] args) throws IOException {

        SSTable sstable = new SSTable("data/test-sstable.txt");

        Map<String, String> data = new TreeMap<>();
        data.put("fruit", "Apple");
        data.put("color", "Blue");
        data.put("city", "Chennai");

        sstable.write(data);

        System.out.println("Get fruit: " + sstable.get("fruit"));
        System.out.println("Get missing key: " + sstable.get("animal"));
        System.out.println("All entries: " + sstable.readAll());
    }
}
