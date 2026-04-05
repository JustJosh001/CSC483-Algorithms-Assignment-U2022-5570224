# CSC483-Algorithms-Assignment

**Name:** Mathias Joshua Abraham
**Student ID:** U2022/5570224
**Course:** CSC 483.1 – Algorithms Analysis and Design
**Assignment:** Algorithm Design, Analysis, and Optimization for Real-World Systems
**Session:** 2025/2026 | First Semester
**Institution:** University of Port Harcourt, Faculty of Computing

---

## Project Structure

```
CSC483-Algorithms-Assignment/
├── src/
│   └── com/csc483/assignment/
│       ├── search/
│       │   ├── Product.java              # Product entity class
│       │   ├── SearchAlgorithms.java     # Sequential, binary, and hybrid search
│       │   └── TechMartBenchmark.java    # Q1 benchmark runner (main class)
│       ├── sorting/
│       │   ├── SortMetrics.java          # Comparison/swap counter helper
│       │   ├── SortingAlgorithms.java    # Insertion, Merge, Quick, Heap Sort
│       │   └── SortingBenchmark.java     # Q2 benchmark runner (main class)
│       └── test/
│           ├── SearchAlgorithmsTest.java # JUnit 5 tests – Q1
│           └── SortingAlgorithmsTest.java# JUnit 5 tests – Q2
├── datasets/
│   ├── sample_products.csv              # 20-row sample product catalog
│   └── sample_sort_input.txt            # 50-integer sample sort dataset
├── out/                                 # Compiled .class files (auto-generated)
├── .gitignore
└── README.md
```

---

## Dependencies

| Dependency                        | Version | Purpose                                                |
| --------------------------------- | ------- | ------------------------------------------------------ |
| Java JDK                          | 20      | Language runtime and compiler                          |
| JUnit Platform Console Standalone | 5.9.3   | Running JUnit 5 tests from command line                |
| Maven (pom.xl)                    | 3.9+    | VSCode error resolution and JUnit dependency managment |

---

## Setup Before You Begin

**Step 1 — Verify Java is installed**

Open a terminal and run:

```
java -version
```

You should see a version number printed. If not, download and install the JDK from:
**https://www.oracle.com/java/technologies/downloads/**

**Step 2 — Download the JUnit JAR**

Download this file and place it in your **project root folder** (same level as `src/`):

**https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.9.3/junit-platform-console-standalone-1.9.3.jar**

**Step 3 — Create the output folder**

In your terminal, navigate to the project root and run:

```
mkdir out
```

---

## Compilation

Open a terminal, navigate to the project root, then run this command to compile all source files at once:

```
javac -d out src\com\csc483\assignment\search\Product.java src\com\csc483\assignment\search\SearchAlgorithms.java src\com\csc483\assignment\search\TechMartBenchmark.java src\com\csc483\assignment\sorting\SortMetrics.java src\com\csc483\assignment\sorting\SortingAlgorithms.java src\com\csc483\assignment\sorting\SortingBenchmark.java
```

> **Important:** Always compile from the project root directory. Never compile individual files in isolation — the classes depend on each other.

If successful, you will see no output and compiled `.class` files will appear in the `out/` folder.

---

## Running the Programs

### Question 1 — TechMart Search Benchmark

```
java -cp out com.csc483.assignment.search.TechMartBenchmark
```

**Expected output:** A formatted performance comparison table for sequential search, binary search, and hybrid name search on 100,000 products.

### Question 2 — Sorting Algorithms Benchmark

```
java -cp out com.csc483.assignment.sorting.SortingBenchmark
```

**Expected output:** Comparison tables for Insertion Sort, Merge Sort, Quick Sort, and Heap Sort across 5 data types and 4 input sizes.

---

## Running the Tests

**Step 1 — Compile the test files**

```
javac -cp "out;junit-platform-console-standalone-1.9.3.jar" -d out src\com\csc483\assignment\test\SearchAlgorithmsTest.java src\com\csc483\assignment\test\SortingAlgorithmsTest.java
```

**Step 2 — Run all tests**

```
java -jar junit-platform-console-standalone-1.9.3.jar --classpath out --scan-classpath
```

**Expected output:**

```
Thanks for using JUnit! ...
[  PASSED ] 35 tests
```

---

## Complete Workflow Summary

Run these commands in order every time:

```
# 1. Compile main source files
javac -d out src\com\csc483\assignment\search\Product.java src\com\csc483\assignment\search\SearchAlgorithms.java src\com\csc483\assignment\search\TechMartBenchmark.java src\com\csc483\assignment\sorting\SortMetrics.java src\com\csc483\assignment\sorting\SortingAlgorithms.java src\com\csc483\assignment\sorting\SortingBenchmark.java

# 2. Run Question 1
java -cp out com.csc483.assignment.search.TechMartBenchmark

# 3. Run Question 2
java -cp out com.csc483.assignment.sorting.SortingBenchmark

# 4. Compile test files
javac -cp "out;junit-platform-console-standalone-1.9.3.jar" -d out src\com\csc483\assignment\test\SearchAlgorithmsTest.java src\com\csc483\assignment\test\SortingAlgorithmsTest.java

# 5. Run all tests
java -jar junit-platform-console-standalone-1.9.3.jar --classpath out --scan-classpath
```

---

## Algorithm Complexity Quick Reference

| Algorithm         | Best       | Average    | Worst      | Space    |
| ----------------- | ---------- | ---------- | ---------- | -------- |
| Sequential Search | O(1)       | O(n)       | O(n)       | O(1)     |
| Binary Search     | O(1)       | O(log n)   | O(log n)   | O(1)     |
| Insertion Sort    | O(n)       | O(n²)      | O(n²)      | O(1)     |
| Merge Sort        | O(n log n) | O(n log n) | O(n log n) | O(n)     |
| Quick Sort        | O(n log n) | O(n log n) | O(n²)      | O(log n) |
| Heap Sort         | O(n log n) | O(n log n) | O(n log n) | O(1)     |

---

## Known Limitations

- `TechMartBenchmark` generates products with random IDs on every run — exact timing results will vary by machine
- `binarySearchById` requires the array to be **pre-sorted by productId** — calling it on an unsorted array produces undefined results
- `addProduct` shifts elements in an array (O(n)); for high-frequency inserts a TreeMap or skip list would be more appropriate
- The JUnit JAR file must be in the **project root folder** for test commands to work correctly
- Quick Sort uses median-of-three pivot selection; worst-case O(n²) is therefore rare but still theoretically possible

---

_Submitted in partial fulfilment of CSC 483.1 requirements, University of Port Harcourt._
