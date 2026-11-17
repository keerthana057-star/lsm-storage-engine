package com.lsm;

import java.io.IOException;

public class WALRecoveryCapacityTest {

    public static void main(String[] args) throws IOException {

        String directory = "data/wal-capacity-test";

        LSMTree lsm = new LSMTree(directory, 2);

        lsm.put("fruit", "Apple");
        lsm.put("color", "Blue");
        lsm.put("city", "Chennai");
        lsm.put("food", "Pizza");

        System.out.println("Before restart:");
        System.out.println("MemTable size: "
                + lsm.getMemTableData().size());

        LSMTree recovered = new LSMTree(directory, 2);

        System.out.println("\nAfter restart:");
        System.out.println("MemTable size: "
                + recovered.getMemTableData().size());

        System.out.println("Fruit: " + recovered.get("fruit"));
        System.out.println("Color: " + recovered.get("color"));
        System.out.println("City: " + recovered.get("city"));
        System.out.println("Food: " + recovered.get("food"));
    }
}
