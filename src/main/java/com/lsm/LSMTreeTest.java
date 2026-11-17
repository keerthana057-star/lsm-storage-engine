package com.lsm;

import java.io.IOException;

public class LSMTreeTest {

    public static void main(String[] args) throws IOException {

        // STAGE 1 - APPLICATION INPUT
        System.out.println("fruit = Apple");
        System.out.println("color = Blue");
        System.out.println("city = Chennai");

        // STAGE 2 - PUT REQUEST
        System.out.println("\nPUT REQUEST:");
        System.out.println("PUT(fruit, Apple)");
        System.out.println("PUT(color, Blue)");
        System.out.println("PUT(city, Chennai)");

        // STAGE 3 - WAL
        System.out.println("\nWAL RECORDS:");

        WAL wal = new WAL("data/wal-stage-test.log");

        wal.append("PUT", "fruit", "Apple");
        wal.append("PUT", "color", "Blue");
        wal.append("PUT", "city", "Chennai");

        for (String record : wal.readAll()) {
            System.out.println(record);
        }

        // STAGE 4 - MEMTABLE
        MemTable memTable = new MemTable();

        memTable.put("fruit", "Apple");
        memTable.put("color", "Blue");
        memTable.put("city", "Chennai");

        System.out.println("\nMEMTABLE:");
        System.out.println(memTable.getAll());

        // STAGE 5 - SIZE CHECK
        int capacity = 3;

        memTable.clear();

        memTable.put("fruit", "Apple");
        System.out.println("MemTable Size: "
                + memTable.size() + "/" + capacity);

        memTable.put("color", "Blue");
        System.out.println("MemTable Size: "
                + memTable.size() + "/" + capacity);

        memTable.put("city", "Chennai");
        System.out.println("MemTable Size: "
                + memTable.size() + "/" + capacity);
    }
}
