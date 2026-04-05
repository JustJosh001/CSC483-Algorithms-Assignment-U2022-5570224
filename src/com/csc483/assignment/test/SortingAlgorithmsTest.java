package com.csc483.assignment.test;

import com.csc483.assignment.sorting.SortMetrics;
import com.csc483.assignment.sorting.SortingAlgorithms;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 tests for {@link SortingAlgorithms}.
 *
 * @author Student ID: U2022/5570224
 */
@DisplayName("CSC483 – Sorting Algorithms Tests")
class SortingAlgorithmsTest {

    // =========================================================================
    // Helper: check sorted ascending
    // =========================================================================

    private static boolean isSorted(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] < arr[i - 1]) return false;
        }
        return true;
    }

    // =========================================================================
    // Test data provider
    // =========================================================================

    static Stream<int[]> testArrays() {
        return Stream.of(
            new int[]{5, 3, 8, 1, 9, 2, 7, 4, 6},       // random
            new int[]{1, 2, 3, 4, 5},                     // already sorted
            new int[]{5, 4, 3, 2, 1},                     // reverse sorted
            new int[]{1},                                  // single element
            new int[]{2, 2, 2, 2},                         // all duplicates
            new int[]{3, 1},                               // two elements
            new int[]{}                                    // empty
        );
    }

    // =========================================================================
    // Insertion Sort tests
    // =========================================================================

    @ParameterizedTest(name = "insertionSort – {0}")
    @MethodSource("testArrays")
    @DisplayName("Insertion Sort – all cases correct")
    void testInsertionSort(int[] arr) {
        int[] expected = Arrays.copyOf(arr, arr.length);
        Arrays.sort(expected);

        SortingAlgorithms.insertionSort(arr, null);
        assertArrayEquals(expected, arr);
    }

    @Test
    @DisplayName("Insertion Sort – null array is no-op")
    void testInsertionSortNull() {
        assertDoesNotThrow(() -> SortingAlgorithms.insertionSort(null, null));
    }

    @Test
    @DisplayName("Insertion Sort – metrics tracked correctly")
    void testInsertionSortMetrics() {
        int[] arr = {3, 1, 2};
        SortMetrics m = new SortMetrics();
        SortingAlgorithms.insertionSort(arr, m);
        assertTrue(m.comparisons > 0, "Expected comparisons > 0");
        assertTrue(m.swaps >= 0,      "Expected swaps >= 0");
    }

    // =========================================================================
    // Merge Sort tests
    // =========================================================================

    @ParameterizedTest(name = "mergeSort – {0}")
    @MethodSource("testArrays")
    @DisplayName("Merge Sort – all cases correct")
    void testMergeSort(int[] arr) {
        int[] expected = Arrays.copyOf(arr, arr.length);
        Arrays.sort(expected);

        SortingAlgorithms.mergeSort(arr, null);
        assertArrayEquals(expected, arr);
    }

    @Test
    @DisplayName("Merge Sort – null array is no-op")
    void testMergeSortNull() {
        assertDoesNotThrow(() -> SortingAlgorithms.mergeSort(null, null));
    }

    @Test
    @DisplayName("Merge Sort – comparisons tracked")
    void testMergeSortMetrics() {
        int[] arr = {4, 2, 3, 1};
        SortMetrics m = new SortMetrics();
        SortingAlgorithms.mergeSort(arr, m);
        assertTrue(m.comparisons > 0);
    }

    // =========================================================================
    // Quick Sort tests
    // =========================================================================

    @ParameterizedTest(name = "quickSort – {0}")
    @MethodSource("testArrays")
    @DisplayName("Quick Sort – all cases correct")
    void testQuickSort(int[] arr) {
        int[] expected = Arrays.copyOf(arr, arr.length);
        Arrays.sort(expected);

        SortingAlgorithms.quickSort(arr, null);
        assertArrayEquals(expected, arr);
    }

    @Test
    @DisplayName("Quick Sort – handles sorted input (median-of-three)")
    void testQuickSortAlreadySorted() {
        int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        SortingAlgorithms.quickSort(arr, null);
        assertTrue(isSorted(arr));
    }

    @Test
    @DisplayName("Quick Sort – handles reverse sorted input")
    void testQuickSortReverse() {
        int[] arr = {10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
        SortingAlgorithms.quickSort(arr, null);
        assertTrue(isSorted(arr));
    }

    // =========================================================================
    // Heap Sort tests
    // =========================================================================

    @ParameterizedTest(name = "heapSort – {0}")
    @MethodSource("testArrays")
    @DisplayName("Heap Sort – all cases correct")
    void testHeapSort(int[] arr) {
        int[] expected = Arrays.copyOf(arr, arr.length);
        Arrays.sort(expected);

        SortingAlgorithms.heapSort(arr, null);
        assertArrayEquals(expected, arr);
    }

    @Test
    @DisplayName("Heap Sort – null array is no-op")
    void testHeapSortNull() {
        assertDoesNotThrow(() -> SortingAlgorithms.heapSort(null, null));
    }

    @Test
    @DisplayName("Heap Sort – metrics tracked")
    void testHeapSortMetrics() {
        int[] arr = {9, 3, 7, 1, 5};
        SortMetrics m = new SortMetrics();
        SortingAlgorithms.heapSort(arr, m);
        assertTrue(m.comparisons > 0);
        assertTrue(m.swaps > 0);
    }

    // =========================================================================
    // Cross-algorithm consistency test
    // =========================================================================

    @Test
    @DisplayName("All four algorithms produce the same sorted result")
    void testAllAlgorithmsProduceSameResult() {
        int[] original = {15, 3, 9, 1, 7, 12, 4, 11, 6, 2, 8, 10, 14, 5, 13};

        int[] a = Arrays.copyOf(original, original.length);
        int[] b = Arrays.copyOf(original, original.length);
        int[] c = Arrays.copyOf(original, original.length);
        int[] d = Arrays.copyOf(original, original.length);

        SortingAlgorithms.insertionSort(a, null);
        SortingAlgorithms.mergeSort(b, null);
        SortingAlgorithms.quickSort(c, null);
        SortingAlgorithms.heapSort(d, null);

        assertArrayEquals(a, b, "Insertion vs Merge mismatch");
        assertArrayEquals(b, c, "Merge vs Quick mismatch");
        assertArrayEquals(c, d, "Quick vs Heap mismatch");
    }

    @Test
    @DisplayName("All algorithms handle large array (n = 10,000)")
    void testLargeArray() {
        int[] arr = new int[10_000];
        for (int i = 0; i < arr.length; i++) arr[i] = arr.length - i; // reverse sorted

        int[] a = Arrays.copyOf(arr, arr.length);
        int[] b = Arrays.copyOf(arr, arr.length);
        int[] c = Arrays.copyOf(arr, arr.length);
        int[] d = Arrays.copyOf(arr, arr.length);

        assertDoesNotThrow(() -> SortingAlgorithms.insertionSort(a, null));
        assertDoesNotThrow(() -> SortingAlgorithms.mergeSort(b, null));
        assertDoesNotThrow(() -> SortingAlgorithms.quickSort(c, null));
        assertDoesNotThrow(() -> SortingAlgorithms.heapSort(d, null));

        assertTrue(isSorted(a));
        assertTrue(isSorted(b));
        assertTrue(isSorted(c));
        assertTrue(isSorted(d));
    }
}
