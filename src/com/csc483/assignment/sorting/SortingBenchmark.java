package com.csc483.assignment.sorting;

import java.util.Random;

/**
 * Empirical benchmark for Insertion Sort, Merge Sort, Quick Sort, and Heap Sort.
 *
 * <p>Tests each algorithm on:
 * <ul>
 *   <li>Random order</li>
 *   <li>Already sorted (ascending)</li>
 *   <li>Reverse sorted (descending)</li>
 *   <li>Nearly sorted (90% sorted, 10% swapped)</li>
 *   <li>Many duplicates (only 10 distinct values)</li>
 * </ul>
 * for input sizes: 100, 1 000, 10 000, 100 000.
 *
 * @author CSC483 Student
 * @version 1.0
 */
public class SortingBenchmark {

    private static final int[]    SIZES            = {100, 1_000, 10_000, 100_000};
    private static final int      RUNS             = 5;
    private static final int      FORMAT_THRESHOLD = 1_000; // format numbers >= this with commas
    private static final Random   RNG              = new Random(42);

    // =========================================================================
    // Main
    // =========================================================================

    public static void main(String[] args) {
        String[] dataTypes = {"Random", "Sorted", "Reverse", "Nearly Sorted", "Many Duplicates"};

        for (String dataType : dataTypes) {
            printSectionHeader(dataType);
            printTableHeader();

            for (int n : SIZES) {
                runAlgorithm("Insertion", n, dataType);
                runAlgorithm("Merge",     n, dataType);
                runAlgorithm("Quick",     n, dataType);
                runAlgorithm("Heap",      n, dataType);
                if (n < SIZES[SIZES.length - 1]) System.out.println();
            }
            System.out.println();
        }

        printConclusions();
    }

    // =========================================================================
    // Core runner
    // =========================================================================

    private static void runAlgorithm(String algo, int n, String dataType) {
        double totalMs   = 0;
        long totalCmp    = 0;
        long totalSwaps  = 0;
        double[] times   = new double[RUNS];

        for (int run = 0; run < RUNS; run++) {
            int[] data    = generateData(n, dataType);
            SortMetrics m = new SortMetrics();

            long start = System.nanoTime();
            sort(algo, data, m);
            long end   = System.nanoTime();

            double ms    = (end - start) / 1_000_000.0;
            times[run]   = ms;
            totalMs     += ms;
            totalCmp    += m.comparisons;
            totalSwaps  += m.swaps;

            // Verify sort correctness on first run
            if (run == 0 && !isSorted(data)) {
                System.err.printf("ERROR: %s sort produced incorrect result for n=%d%n", algo, n);
            }
        }

        double avgMs  = totalMs   / RUNS;
        long avgCmp   = totalCmp  / RUNS;
        long avgSwaps = totalSwaps / RUNS;
        double stdDev = stdDev(times, avgMs);

        String swapsStr = algo.equals("Merge") ? "N/A" : String.format("%,d", avgSwaps);
        System.out.printf("%-12s %-14s %-8s %10.3f ± %5.3f  %,15d  %14s%n",
                        algo, n >= FORMAT_THRESHOLD ? String.format("%,d", n) : String.valueOf(n),
                        dataType.length() > 6 ? dataType.substring(0, 6) : dataType,
                        avgMs, stdDev, avgCmp, swapsStr);
    }

    // =========================================================================
    // Data generators
    // =========================================================================

    private static int[] generateData(int n, String type) {
        int[] arr = new int[n];
        switch (type) {
            case "Random":
                for (int i = 0; i < n; i++) arr[i] = RNG.nextInt(n * 10);
                break;
            case "Sorted":
                for (int i = 0; i < n; i++) arr[i] = i;
                break;
            case "Reverse":
                for (int i = 0; i < n; i++) arr[i] = n - i;
                break;
            case "Nearly Sorted":
                for (int i = 0; i < n; i++) arr[i] = i;
                // Swap 10% randomly
                int swaps = Math.max(1, n / 10);
                for (int s = 0; s < swaps; s++) {
                    int a = RNG.nextInt(n), b = RNG.nextInt(n);
                    int tmp = arr[a]; arr[a] = arr[b]; arr[b] = tmp;
                }
                break;
            case "Many Duplicates":
                for (int i = 0; i < n; i++) arr[i] = RNG.nextInt(10);
                break;
            default:
                throw new IllegalArgumentException("Unknown data type: " + type);
        }
        return arr;
    }

    // =========================================================================
    // Dispatcher
    // =========================================================================

    private static void sort(String algo, int[] arr, SortMetrics m) {
        switch (algo) {
            case "Insertion": SortingAlgorithms.insertionSort(arr, m); break;
            case "Merge":     SortingAlgorithms.mergeSort(arr, m);     break;
            case "Quick":     SortingAlgorithms.quickSort(arr, m);     break;
            case "Heap":      SortingAlgorithms.heapSort(arr, m);      break;
        }
    }

    // =========================================================================
    // Utilities
    // =========================================================================

    private static boolean isSorted(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] < arr[i - 1]) return false;
        }
        return true;
    }

    private static double stdDev(double[] times, double mean) {
        double sum = 0;
        for (double t : times) sum += (t - mean) * (t - mean);
        return Math.sqrt(sum / times.length);
    }

    // =========================================================================
    // Formatting helpers
    // =========================================================================

    private static void printSectionHeader(String dataType) {
        System.out.println("================================================================");
        System.out.printf("SORTING ALGORITHMS COMPARISON – %s DATA%n", dataType.toUpperCase());
        System.out.println("================================================================");
    }

    private static void printTableHeader() {
        System.out.printf("%-12s %-14s %-8s %16s  %15s  %14s%n",
                        "Algorithm", "Input Size", "Type", "Time(ms)±StdDev",
                        "Comparisons", "Swaps");
        System.out.println("-".repeat(80));
    }

    private static void printConclusions() {
        System.out.println("================================================================");
        System.out.println("CONCLUSIONS:");
        System.out.println("  - Quick Sort is fastest on average for random and nearly-sorted data.");
        System.out.println("  - Insertion Sort is competitive only for n <= 1,000.");
        System.out.println("  - Merge Sort provides consistent O(n log n) regardless of data order.");
        System.out.println("  - Heap Sort uses O(1) extra space but is slower than Quick Sort in practice.");
        System.out.println("  - Quick Sort degrades toward O(n^2) for sorted input without median pivot.");
        System.out.println("  - Insertion Sort excels on Nearly Sorted data (approaches O(n)).");
        System.out.println("================================================================");
    }
}
