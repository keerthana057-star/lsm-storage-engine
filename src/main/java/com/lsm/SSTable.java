package com.lsm;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;

public class SSTable {

    private final Path path;

    public SSTable(String fileName) throws IOException {
        path = Path.of(fileName);

        if (path.getParent() != null) {
            Files.createDirectories(path.getParent());
        }
    }

    public Path getPath() {
        return path;
    }

    public void write(Map<String, String> data) throws IOException {
        Map<String, String> sortedData = new TreeMap<>(data);

        StringBuilder content = new StringBuilder();

        for (Map.Entry<String, String> entry : sortedData.entrySet()) {
            content.append(entry.getKey())
                    .append("|")
                    .append(entry.getValue())
                    .append(System.lineSeparator());
        }

        Files.writeString(path, content.toString());
    }

    public String get(String key) throws IOException {
        if (!Files.exists(path)) {
            return null;
        }

        for (String line : Files.readAllLines(path)) {
            String[] parts = line.split("\\|", 2);

            if (parts.length == 2 && parts[0].equals(key)) {
                return parts[1];
            }
        }

        return null;
    }

    public Map<String, String> readAll() throws IOException {
        Map<String, String> data = new TreeMap<>();

        if (!Files.exists(path)) {
            return data;
        }

        for (String line : Files.readAllLines(path)) {
            String[] parts = line.split("\\|", 2);

            if (parts.length == 2) {
                data.put(parts[0], parts[1]);
            }
        }

        return data;
    }
}
