LSM-Tree Key-Value Store

A Java-based LSM-Tree Key-Value Store built to understand how storage engines handle write-heavy workloads efficiently.

Project Goal

The goal of this project is to understand how a storage engine works internally by implementing the core components of an LSM-Tree based key-value store.

The project focuses on:

* Write-Ahead Logging (WAL)
* MemTable
* SSTables
* Efficient reads
* Bloom Filters
* SSTable Indexing
* Compaction
* Tombstones and Delete handling
* WAL-based crash recovery

 Architecture

```text
                 Client
                    │
             PUT / GET / DELETE
                    │
             ┌──────▼──────┐
             │   LSM-Tree  │
             └──────┬──────┘
                    │
          ┌─────────┴─────────┐
          │                   │
        WRITE                READ
          │                   │
         WAL              MemTable
          │                   │
      MemTable          SSTable Search
          │                   │
        Flush            Bloom Filter
          │                   │
       SSTable           SSTable Index
          │                   │
      Compaction          SSTables
```

Core Flow

### Write

```text
Client
  ↓
WAL
  ↓
MemTable
  ↓
MemTable Full?
  ↓
Flush
  ↓
SSTable
```

### Read

```text
Client
  ↓
MemTable
  ↓
SSTables
  ↓
Bloom Filter
  ↓
SSTable Index
  ↓
Value
```

### Delete

```text
DELETE
  ↓
WAL
  ↓
Tombstone
  ↓
MemTable
  ↓
SSTable
  ↓
Compaction
```

### Recovery

```text
Crash
  ↓
WAL
  ↓
Replay Operations
  ↓
MemTable Recovery
```

🧩 Components

1. WAL
2. MemTable
3. PUT Operation
4. MemTable Full Check
5. Flush Trigger
6. MemTable → SSTable Flush
7. SSTable File Creation
8. MemTable Reset
9. GET Operation
10. SSTable Search
11. Bloom Filter
12. SSTable Index
13. Compaction
14. Tombstone / Delete Handling
15. DELETE Operation
16. WAL Recovery

 🛠️ Tech Stack

* Java
* Maven
* File I/O
* Collections
* Data Structures
* Hashing

📚 What This Project Demonstrates

This project demonstrates practical understanding of:

* Write-heavy storage design
* In-memory data structures
* Persistent sorted files
* Read optimization
* Crash recovery
* Deletion handling
* Compaction
* Storage-engine design

 🚧 Project Status

In Development

The project is being implemented component-by-component, with each component understood, implemented, tested, and integrated into the complete LSM-Tree.

Learning Objective

This project is primarily built as a hands-on learning project to understand the internal design and implementation of an LSM-Tree based storage engine.
