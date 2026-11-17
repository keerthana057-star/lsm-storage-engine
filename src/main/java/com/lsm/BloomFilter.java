package com.lsm;

import java.util.BitSet;

public class BloomFilter {

    private final BitSet bits;
    private final int size;

    public BloomFilter(int size) {
        this.size = size;
        this.bits = new BitSet(size);
    }

    public void add(String key) {
        bits.set(hash1(key));
        bits.set(hash2(key));
    }

    public boolean mightContain(String key) {
        return bits.get(hash1(key))
                && bits.get(hash2(key));
    }

    private int hash1(String key) {
        return Math.floorMod(key.hashCode(), size);
    }

    private int hash2(String key) {
        return Math.floorMod(
                key.hashCode() * 31 + 17, size);
    }
}
