package com.lsm;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class WAL {

    private final Path path;

    public WAL(String fileName) throws IOException {
        path = Path.of(fileName);

        if (path.getParent() != null) {
            Files.createDirectories(path.getParent());
        }

        if (!Files.exists(path)) {
            Files.createFile(path);
        }
    }

    public void append(String operation, String key, String value)
            throws IOException {

        String record = operation + "|" + key + "|" + value;

        Files.writeString(
                path,
                record + System.lineSeparator(),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
        );
    }

    public void clear() throws IOException {
        Files.writeString(path, "", StandardOpenOption.TRUNCATE_EXISTING);
    }

    public List<String> readAll() throws IOException {
        return Files.readAllLines(path);
    }
}
