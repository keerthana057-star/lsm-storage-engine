LSM-Tree Based Key-Value Storage Engine

A Log-Structured Merge Tree (LSM-Tree) is a storage-engine architecture designed to support high-throughput write workloads by buffering writes in memory and writing data sequentially to disk.

Why This Project?

Traditional storage systems can face performance bottlenecks when handling large volumes of random disk writes. LSM-Tree architecture addresses this by using an in-memory MemTable, a durable Write-Ahead Log (WAL), and immutable Sorted String Tables (SSTables).

What I Built

A mini LSM-Tree based key-value storage engine in Java that implements:

Write-Ahead Logging (WAL) for durability and crash recovery
MemTable for in-memory writes
SSTables for persistent sorted storage
Bloom Filters for efficient reads
Compaction for managing and merging SSTables
Read and Write workflows for key-value operations

System Flow

Client → WAL → MemTable → SSTable → Disk

For reads:

Client → MemTable → SSTables → Bloom Filter → Result

Goal

To understand and implement the core internals of a modern storage engine while exploring high-throughput writes, durability, crash recovery, efficient reads, and backend system design.
