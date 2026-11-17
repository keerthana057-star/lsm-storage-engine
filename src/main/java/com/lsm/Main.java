package com.lsm;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {

    public static void main(String[] args) throws IOException {
        runWritePath();
        runCompactionPath();
        runReadPath();
        runCrashRecoveryPath();
    }

    private static void runWritePath() throws IOException {
        System.out.println("========================================");
        System.out.println("1. WRITE PATH (PUT)");
        System.out.println("========================================");

        // Clean up data directory
        cleanDirectory("data/write-test");

        LSMTree lsm = new LSMTree("data/write-test", 3);

        // APPLICATION INPUT
        System.out.println("\n--- APPLICATION - Input ---");
        System.out.println("fruit = Apple");
        System.out.println("color = Blue");
        System.out.println("city = Chennai");

        // PUT REQUESTS
        System.out.println("\n--- PUT REQUEST ---");
        System.out.println("PUT(fruit, Apple)");
        System.out.println("PUT(color, Blue)");
        System.out.println("PUT(city, Chennai)");

        // Execute PUTs
        lsm.put("fruit", "Apple");
        lsm.put("color", "Blue");
        lsm.put("city", "Chennai");

        System.out.println("\n--- MemTable after flush ---");
        System.out.println(lsm.getMemTableData());
    }

    private static void runCompactionPath() throws IOException {
        System.out.println("\n========================================");
        System.out.println("2. COMPACTION PATH");
        System.out.println("========================================");

        // Clean up data directory
        cleanDirectory("data/compaction-test");

        LSMTree lsm = new LSMTree("data/compaction-test", 2);

        // Create two SSTables with overlapping keys
        System.out.println("\n--- Creating SSTable 1 ---");
        lsm.put("fruit", "Apple");
        lsm.put("color", "Blue");

        System.out.println("\n--- Creating SSTable 2 ---");
        lsm.put("fruit", "Mango");
        lsm.put("city", "Chennai");

        System.out.println("\n--- Compaction ---");
        lsm.compact();
    }

    private static void runReadPath() throws IOException {
        System.out.println("\n========================================");
        System.out.println("3. READ PATH (GET)");
        System.out.println("========================================");

        // Clean up data directory
        cleanDirectory("data/read-test");

        LSMTree lsm = new LSMTree("data/read-test", 3);

        // Put data and flush
        lsm.put("fruit", "Apple");
        lsm.put("color", "Blue");
        lsm.put("city", "Chennai");

        // MemTable is now empty after flush
        System.out.println("\n--- MemTable after flush ---");
        System.out.println(lsm.getMemTableData());

        // GET requests
        System.out.println("\n--- GET REQUEST ---");
        System.out.println("lsm.get(\"fruit\") -> " + lsm.get("fruit"));
        System.out.println("lsm.get(\"color\") -> " + lsm.get("color"));
        System.out.println("lsm.get(\"unknown\") -> " + lsm.get("unknown"));
    }

    private static void runCrashRecoveryPath() throws IOException {
        System.out.println("\n========================================");
        System.out.println("4. CRASH RECOVERY PATH");
        System.out.println("========================================");

        // Clean up data directory
        cleanDirectory("data/recovery-test");

        // First instance - write data without flushing
        System.out.println("\n--- First Instance (before crash) ---");
        LSMTree lsm = new LSMTree("data/recovery-test", 10);
        lsm.put("fruit", "Apple");
        lsm.put("color", "Blue");
        lsm.put("city", "Chennai");

        // Simulate System Crash (restart LSMTree instance)
        System.out.println("\n--- Simulate System Crash (restart LSMTree instance) ---");
        LSMTree restarted = new LSMTree("data/recovery-test", 10);

        // Verify recovery
        System.out.println("\n--- Verify Recovery ---");
        System.out.println("Recovered fruit: " + restarted.get("fruit"));
        System.out.println("Recovered color: " + restarted.get("color"));
        System.out.println("Recovered city: " + restarted.get("city"));
    }

    private static void cleanDirectory(String dirPath) throws IOException {
        Path dir = Path.of(dirPath);
        if (Files.exists(dir)) {
            try (var files = Files.list(dir)) {
                files.forEach(path -> {
                    try {
                        Files.deleteIfExists(path);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
            Files.deleteIfExists(dir);
        }
        Files.createDirectories(dir);
    }
}
