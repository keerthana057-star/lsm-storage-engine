package com.lsm;

import java.io.IOException;

public class DeleteRecoveryTest {

    public static void main(String[] args) throws IOException {

        String directory = "data/delete-recovery-test";

        LSMTree lsm = new LSMTree(directory, 2);

        lsm.put("fruit", "Apple");
        lsm.put("color", "Blue");

        lsm.delete("fruit");
        lsm.put("city", "Chennai");

        lsm.compact();

        System.out.println("Before restart:");
        System.out.println("Fruit: " + lsm.get("fruit"));
        System.out.println("Color: " + lsm.get("color"));
        System.out.println("City: " + lsm.get("city"));

        LSMTree recovered = new LSMTree(directory, 2);

        System.out.println("\nAfter restart:");
        System.out.println("Fruit: " + recovered.get("fruit"));
        System.out.println("Color: " + recovered.get("color"));
        System.out.println("City: " + recovered.get("city"));
    }
}
