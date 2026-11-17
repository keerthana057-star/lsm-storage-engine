package com.lsm;

import java.io.IOException;

public class RecoveryTest {

    public static void main(String[] args) throws IOException {

        String directory = "data/recovery-test";

        LSMTree first = new LSMTree(directory, 10);
        first.put("fruit", "Apple");
        first.put("color", "Blue");

        // Simulate restart by creating a new LSMTree instance.
        LSMTree restarted = new LSMTree(directory, 10);

        System.out.println("Recovered fruit: "
                + restarted.get("fruit"));
        System.out.println("Recovered color: "
                + restarted.get("color"));
    }
}
