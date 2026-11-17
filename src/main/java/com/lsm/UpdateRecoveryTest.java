package com.lsm;

import java.io.IOException;

public class UpdateRecoveryTest {

    public static void main(String[] args) throws IOException {

        String directory = "data/update-recovery-test";

        LSMTree lsm = new LSMTree(directory, 2);

        lsm.put("fruit", "Apple");
        lsm.put("color", "Blue");

        lsm.put("fruit", "Mango");
        lsm.put("city", "Chennai");

        lsm.put("fruit", "Orange");

        System.out.println("Before restart:");
        System.out.println("Fruit: " + lsm.get("fruit"));

        LSMTree recovered = new LSMTree(directory, 2);

        System.out.println("\nAfter restart:");
        System.out.println("Fruit: " + recovered.get("fruit"));
    }
}
