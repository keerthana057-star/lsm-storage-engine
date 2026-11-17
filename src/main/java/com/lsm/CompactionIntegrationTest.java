package com.lsm;

import java.io.IOException;

public class CompactionIntegrationTest {

    public static void main(String[] args) throws IOException {

        LSMTree lsm = new LSMTree("data/compact-integration-test", 2);

        lsm.put("fruit", "Apple");
        lsm.put("color", "Blue");

        lsm.put("fruit", "Mango");
        lsm.put("city", "Chennai");

        System.out.println("Before compaction:");
        System.out.println("Fruit: " + lsm.get("fruit"));
        System.out.println("Color: " + lsm.get("color"));
        System.out.println("City: " + lsm.get("city"));

        lsm.compact();

        System.out.println("\nAfter compaction:");
        System.out.println("Fruit: " + lsm.get("fruit"));
        System.out.println("Color: " + lsm.get("color"));
        System.out.println("City: " + lsm.get("city"));
    }
}
