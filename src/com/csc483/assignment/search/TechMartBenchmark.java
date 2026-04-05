package com.csc483.assignment.search;

import java.util.Arrays;
import java.util.Random;

/**
 * TechMart Search Performance Benchmark.
 *
 * <p>Generates a dataset of 100,000 random products, then measures
 * and compares sequential search versus binary search across
 * best-case, average-case, and worst-case scenarios.
 *
 * @author Student ID: U2022/5570224
 * @version 1.0
 */
public class TechMartBenchmark {

    /** Total number of products in the catalog */
    private static final int N = 100_000;

    /** Product IDs are drawn from [1, MAX_ID] to create gaps */
    private static final int MAX_ID = 200_000;

    /** Number of timing runs to average */
    private static final int RUNS = 5;

    private static final String[] CATEGORIES = {
        "Laptops", "Smartphones", "Tablets", "Monitors",
        "Keyboards", "Mice", "Headphones", "Cameras", "Printers", "Storage"
    };

    private static final String[] NAME_PREFIXES = {
        "TechPro", "UltraMax", "SmartX", "ProElite", "NexGen",
        "PowerCore", "SwiftTech", "ZenBook", "AlphaPlus", "OmegaX"
    };

    // =========================================================================
    // Main entry point
    // =========================================================================

    public static void main(String[] args) {
        System.out.println("================================================================");
        System.out.printf("TECHMART SEARCH PERFORMANCE ANALYSIS (n = %,d products)%n", N);
        System.out.println("================================================================");

        // ----- 1. Generate dataset -----
        Product[] products = generateProducts(N);

        // Keep a copy sorted by ID for binary search
        Product[] sortedProducts = Arrays.copyOf(products, products.length);
        Arrays.sort(sortedProducts);

        System.out.printf("%n%-35s %,d products generated.%n", "Dataset:", N);
        System.out.printf("%-35s IDs range from 1 to %,d (with gaps)%n%n",
                        "ID Range:", MAX_ID);

        // ----- 2. Sequential Search -----
        System.out.println("SEQUENTIAL SEARCH:");
        runSequentialBenchmark(products, sortedProducts);

        // ----- 3. Binary Search -----
        System.out.println("\nBINARY SEARCH:");
        runBinaryBenchmark(sortedProducts);

        // ----- 4. Performance improvement -----
        double seqAvg  = measureSequentialAvg(products, sortedProducts);
        double binAvg  = measureBinaryAvg(sortedProducts);
        double ratio   = (binAvg > 0) ? seqAvg / binAvg : 0;
        System.out.printf("%nPERFORMANCE IMPROVEMENT: Binary search is ~%.0fx faster on average%n", ratio);

        // ----- 5. Hybrid Name Search -----
        System.out.println("\nHYBRID NAME SEARCH:");
        runHybridBenchmark(sortedProducts);

        System.out.println("\n================================================================");
    }

    // =========================================================================
    // Dataset generation
    // =========================================================================

    /**
     * Generates an array of N products with unique random IDs in [1, MAX_ID].
     */
    private static Product[] generateProducts(int count) {
        Random rng   = new Random(42);
        boolean[] used = new boolean[MAX_ID + 1];
        Product[] arr  = new Product[count];

        for (int i = 0; i < count; i++) {
            int id;
            do { id = rng.nextInt(MAX_ID) + 1; } while (used[id]);
            used[id] = true;

            String name     = NAME_PREFIXES[rng.nextInt(NAME_PREFIXES.length)] + "-" + id;
            String category = CATEGORIES[rng.nextInt(CATEGORIES.length)];
            double price    = 10.0 + rng.nextDouble() * 2990.0; // $10 – $3000
            int    stock    = rng.nextInt(500);

            arr[i] = new Product(id, name, category, price, stock);
        }
        return arr;
    }

    // =========================================================================
    // Sequential search benchmarks
    // =========================================================================

    private static void runSequentialBenchmark(Product[] products, Product[] sorted) {
        // Best case: target at index 0
        int bestId = products[0].getProductId();
        double bestMs = avgTime(RUNS, () -> SearchAlgorithms.sequentialSearchById(products, bestId));
        System.out.printf("  Best Case  (ID found at position 0)    : %.3f ms%n", bestMs);

        // Average case: random existing ID
        Random rng = new Random(99);
        int avgId = products[rng.nextInt(N)].getProductId();
        double avgMs = avgTime(RUNS, () -> SearchAlgorithms.sequentialSearchById(products, avgId));
        System.out.printf("  Average Case (random existing ID)      : %.3f ms%n", avgMs);

        // Worst case: ID guaranteed not to exist
        int worstId = MAX_ID + 1;
        double worstMs = avgTime(RUNS, () -> SearchAlgorithms.sequentialSearchById(products, worstId));
        System.out.printf("  Worst Case (ID not found)              : %.3f ms%n", worstMs);
    }

    private static double measureSequentialAvg(Product[] products, Product[] sorted) {
        int avgId = sorted[N / 2].getProductId();
        return avgTime(RUNS, () -> SearchAlgorithms.sequentialSearchById(products, avgId));
    }

    // =========================================================================
    // Binary search benchmarks
    // =========================================================================

    private static void runBinaryBenchmark(Product[] sorted) {
        // Best case: target is the middle element
        int midId = sorted[N / 2].getProductId();
        double bestMs = avgTime(RUNS, () -> SearchAlgorithms.binarySearchById(sorted, midId));
        System.out.printf("  Best Case  (ID at middle)              : %.3f ms%n", bestMs);

        // Average case: random existing ID
        Random rng = new Random(7);
        int avgId = sorted[rng.nextInt(N)].getProductId();
        double avgMs = avgTime(RUNS, () -> SearchAlgorithms.binarySearchById(sorted, avgId));
        System.out.printf("  Average Case (random existing ID)      : %.3f ms%n", avgMs);

        // Worst case: ID not present
        int worstId = MAX_ID + 1;
        double worstMs = avgTime(RUNS, () -> SearchAlgorithms.binarySearchById(sorted, worstId));
        System.out.printf("  Worst Case (ID not found)              : %.3f ms%n", worstMs);
    }

    private static double measureBinaryAvg(Product[] sorted) {
        Random rng = new Random(7);
        int avgId = sorted[rng.nextInt(N)].getProductId();
        return avgTime(RUNS, () -> SearchAlgorithms.binarySearchById(sorted, avgId));
    }

    // =========================================================================
    // Hybrid search benchmarks
    // =========================================================================

    private static void runHybridBenchmark(Product[] sorted) {
        SearchAlgorithms sa = new SearchAlgorithms();

        // Build index
        long buildStart = System.nanoTime();
        sa.buildNameIndex(sorted);
        long buildEnd   = System.nanoTime();
        System.out.printf("  Index build time                       : %.3f ms%n",
                        (buildEnd - buildStart) / 1_000_000.0);

        // Average search time
        Random rng    = new Random(13);
        String target = sorted[rng.nextInt(N)].getProductName();
        double searchMs = avgTime(RUNS, () -> sa.hybridNameSearch(target));
        System.out.printf("  Average search time                    : %.3f ms%n", searchMs);

        // Average insert time (insert a new product)
        Product newProd = new Product(MAX_ID + 10, "NewProduct-999999", "Storage", 99.99, 50);
        double insertMs = avgTime(1, () -> {
            Product[] copy = Arrays.copyOf(sorted, sorted.length + 1);
            sa.addProduct(copy, newProd);
        });
        System.out.printf("  Average insert time (addProduct)       : %.3f ms%n", insertMs);
    }

    // =========================================================================
    // Timing utility
    // =========================================================================

    /**
     * Runs a task {@code runs} times and returns the average elapsed time in milliseconds.
     */
    private static double avgTime(int runs, Runnable task) {
        // Warm-up
        task.run();

        long total = 0;
        for (int i = 0; i < runs; i++) {
            long start = System.nanoTime();
            task.run();
            total += System.nanoTime() - start;
        }
        return (total / (double) runs) / 1_000_000.0;
    }
}
