package com.lsm;

import java.io.IOException;

public class WALTest {

    public static void main(String[] args) throws IOException {
        WAL wal = new WAL("data/test-wal.log");

        wal.append("PUT", "fruit", "Apple");
        wal.append("DELETE", "color", "");

        System.out.println(wal.readAll());
    }
}
