What is LSM Tree?
LSM (Log-Structured Merge Tree) is a storage engine design optimized for high write throughput systems.
Instead of writing directly to disk for every operation, writes are first buffered in memory and later flushed sequentially to disk.
This reduces random disk I/O and improves write performance significantly.

Why this project?
Traditional B-Tree databases perform random disk writes, which increases latency under heavy write workloads.
LSM architecture buffers writes in memory using a MemTable and flushes them sequentially to disk as SSTables, significantly improving write performance.
To ensure durability and crash recovery, every write is first stored in a Write Ahead Log (WAL) before updating memory.

This project helps understand:
storage engine internals
write optimization
crash recovery
memory + disk coordination
backend system design

Problem Statement
Imagine an Instagram-like system receiving massive numbers of likes, comments, or messages continuously.
If every write directly goes to disk:
random disk writes become slow
disk I/O becomes a bottleneck
latency increases heavily
the database may struggle under heavy traffic
The system needs an optimized write mechanism that can handle massive write throughput efficiently.

Solution
Instead of writing directly to disk:
incoming writes are first buffered in RAM using a MemTable
writes are also stored in WAL for durability
once the MemTable reaches a threshold, data is flushed sequentially to disk as SSTables
Bloom filters optimize reads
Compaction merges SSTables and removes duplicates

This architecture improves write performance and scalability.

High Level Workflow
Client Request
      │
      ▼
Write Ahead Log (WAL)
      │
      ▼
MemTable (RAM)
      │
      ▼
Flush Threshold Check
      │
      ▼
SSTable Flush
      │
      ▼
SSTable Storage
      │
      ▼
Bloom Filter + Read Workflow
      │
      ▼
Compaction

Core Components
Write Ahead Log (WAL)
Stores every write operation before updating memory to ensure durability and crash recovery.

MemTable
In-memory sorted storage for fast writes.

MemTable Threshold Check
Triggers SSTable flush when memory limit exceeds threshold.

SSTable Flush Logic
Converts MemTable data into immutable disk files.

Reset MemTable
Clears memory after successful flush.

SSTable Storage
Stores immutable sorted files on disk.

Read Workflow
Reads search MemTable first, then SSTables from newest to oldest.

Bloom Filter
Optimizes reads by avoiding unnecessary SSTable disk searches.

Compaction
Background process that merges SSTables and removes duplicate keys.

Data Structures Used
TreeMap / Sorted Map
HashMap
List
Bloom Filter (Bit Array + Hash Functions)

Edge Cases
Duplicate key writes
System crash before flush
MemTable full during heavy traffic
Multiple SSTables containing same key

Performance Considerations
Sequential disk writes improve write performance
MemTable reduces write latency
Bloom filters reduce unnecessary disk reads

Implementation Coding Parts
LSMStorageEngine (Orchestrator)
WAL (Write Ahead Log)
MemTable
SSTable
put() / Write Flow
get() / Read Flow
Flush Logic
Compaction Logic
Final Goal

Build a mini storage engine that demonstrates:

high write throughput handling
durability
crash recovery
SSTable management
backend system design fundamentals
