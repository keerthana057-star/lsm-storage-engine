package com.lsm;

public class MemTableTest {

    public static void main(String[] args) {

        MemTable memTable = new MemTable();

        memTable.put("fruit", "Apple");
        memTable.put("color", "Blue");
        memTable.put("city", "Chennai");

        System.out.println(memTable.getAll());
    }
}
