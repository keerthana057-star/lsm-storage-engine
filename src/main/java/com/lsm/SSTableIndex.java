package com.lsm;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;

public class SSTableIndex {

    private final Map<String, Integer> lineNumbers = new TreeMap<>();

    public void build(String fileName) throws IOException {
        lineNumbers.clear();

        Path path = Path.of(fileName);

        if (!Files.exists(path)) {
            return;
        }

        int lineNumber = 0;

        for (String line : Files.readAllLines(path)) {
            String[] parts = line.split("\\|", 2);

            if (parts.length == 2) {
                lineNumbers.put(parts[0], lineNumber);
            }

            lineNumber++;
        }
    }

    public Integer getLineNumber(String key) {
        return lineNumbers.get(key);
    }

    public boolean containsKey(String key) {
        return lineNumbers.containsKey(key);
    }
}
