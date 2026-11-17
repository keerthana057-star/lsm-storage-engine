package com.lsm;

import java.util.Map;
import java.util.TreeMap;

public class MemTable {

    public static final String TOMBSTONE = "__DELETED__";

    private final Map<String, String> data = new TreeMap<>();

    public void put(String key, String value) {
        data.put(key, value);
    }

    public String get(String key) {
        return data.get(key);
    }

    public void delete(String key) {
        data.put(key, TOMBSTONE);
    }

    public int size() {
        return data.size();
    }

    public boolean isFull(int capacity) {
        return data.size() >= capacity;
    }

    public Map<String, String> getAll() {
        return new TreeMap<>(data);
    }

    public void clear() {
        data.clear();
    }
}
