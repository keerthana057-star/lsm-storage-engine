package com.lsm;

import java.io.IOException;

public class MultiSSTableTest {

    public static void main(String[] args) throws IOException {

        LSMTree lsm = new LSMTree("data/multi-test", 2);

        lsm.put("fruit", "Apple");
        lsm.put("color", "Blue");  // First flush

        lsm.put("fruit", "Mango");
        lsm.put("city", "Chennai"); // Second flush

        System.out.println("Latest fruit: " + lsm.get("fruit"));
        System.out.println("Old color: " + lsm.get("color"));
        System.out.println("New city: " + lsm.get("city"));
    }
}
