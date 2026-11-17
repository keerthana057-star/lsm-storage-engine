package com.lsm;

import java.io.IOException;

public class FinalIntegrationTest {

    public static void main(String[] args) throws IOException {

        String directory = "data/final-integration-test";

        LSMTree lsm = new LSMTree(directory, 2);

        // Insert and flush
        lsm.put("fruit", "Apple");
        lsm.put("color", "Blue");

        // Update and flush
        lsm.put("fruit", "Mango");
        lsm.put("city", "Chennai");

        // Delete and flush
        lsm.delete("fruit");
        lsm.put("food", "Pizza");

        // Compact SSTables
        lsm.compact();

        System.out.println("Before restart:");
        System.out.println("Fruit: " + lsm.get("fruit"));
        System.out.println("Color: " + lsm.get("color"));
        System.out.println("City: " + lsm.get("city"));
        System.out.println("Food: " + lsm.get("food"));

        // Simulate restart
        LSMTree recovered = new LSMTree(directory, 2);

        System.out.println("\nAfter restart:");
        System.out.println("Fruit: " + recovered.get("fruit"));
        System.out.println("Color: " + recovered.get("color"));
        System.out.println("City: " + recovered.get("city"));
        System.out.println("Food: " + recovered.get("food"));
    }
}
