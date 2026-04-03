package com.csc483.assignment.sorting;

/**
 * Implements three sorting algorithms from scratch for empirical comparison:
 * Insertion Sort, Merge Sort, and Quick Sort.
 *
 * <p>All methods operate on {@code int[]} arrays. Each method also accepts
 * a {@link SortMetrics} object to record comparison and swap counts.
 *
 * <p>Complexity Reference:
 * <pre>
 * Algorithm       Best        Average     Worst       Space   Stable?
 * InsertionSort   O(n)        O(n^2)      O(n^2)      O(1)    Yes
 * MergeSort       O(n log n)  O(n log n)  O(n log n)  O(n)    Yes
 * QuickSort       O(n log n)  O(n log n)  O(n^2)      O(log n) No
 * HeapSort        O(n log n)  O(n log n)  O(n log n)  O(1)    No
 * </pre>
 *
 * @author CSC483 Student
 * @version 1.0
 */
public class SortingAlgorithms {

    // =========================================================================
    // Insertion Sort – O(n) best, O(n^2) avg/worst, O(1) space, STABLE
    // =========================================================================

    /**
     * Sorts {@code arr} using insertion sort (ascending order).
     *
     * @param arr     the array to sort (modified in-place)
     * @param metrics counters for comparisons and swaps (may be null)
     */
    public static void insertionSort(int[] arr, SortMetrics metrics) {
        if (arr == null || arr.length <= 1) return;

        for (int i = 1; i < arr.length; i++) {
            int key = arr[i];
            int j   = i - 1;

            // Shift elements greater than key one position ahead
            while (j >= 0) {
                if (metrics != null) metrics.comparisons++;
                if (arr[j] > key) {
                    arr[j + 1] = arr[j];
                    if (metrics != null) metrics.swaps++;
                    j--;
                } else {
                    break;
                }
            }
            arr[j + 1] = key;
        }
    }

    // =========================================================================
    // Merge Sort – O(n log n) all cases, O(n) space, STABLE
    // =========================================================================

    /**
     * Sorts {@code arr} using merge sort (ascending order).
     *
     * @param arr     the array to sort (modified in-place)
     * @param metrics counters for comparisons (swaps N/A for merge sort)
     */
    public static void mergeSort(int[] arr, SortMetrics metrics) {
        if (arr == null || arr.length <= 1) return;
        mergeSortHelper(arr, 0, arr.length - 1, metrics);
    }

    private static void mergeSortHelper(int[] arr, int left, int right, SortMetrics metrics) {
        if (left >= right) return;

        int mid = left + (right - left) / 2;
        mergeSortHelper(arr, left, mid, metrics);
        mergeSortHelper(arr, mid + 1, right, metrics);
        merge(arr, left, mid, right, metrics);
    }

    private static void merge(int[] arr, int left, int mid, int right, SortMetrics metrics) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        int[] L = new int[n1];
        int[] R = new int[n2];

        System.arraycopy(arr, left,     L, 0, n1);
        System.arraycopy(arr, mid + 1,  R, 0, n2);

        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            if (metrics != null) metrics.comparisons++;
            if (L[i] <= R[j]) {
                arr[k++] = L[i++];
            } else {
                arr[k++] = R[j++];
            }
        }
        while (i < n1) arr[k++] = L[i++];
        while (j < n2) arr[k++] = R[j++];
    }

    // =========================================================================
    // Quick Sort – O(n log n) best/avg, O(n^2) worst, O(log n) space, NOT stable
    // =========================================================================

    /**
     * Sorts {@code arr} using quick sort with median-of-three pivot selection.
     *
     * @param arr     the array to sort (modified in-place)
     * @param metrics counters for comparisons and swaps
     */
    public static void quickSort(int[] arr, SortMetrics metrics) {
        if (arr == null || arr.length <= 1) return;
        quickSortHelper(arr, 0, arr.length - 1, metrics);
    }

    private static void quickSortHelper(int[] arr, int low, int high, SortMetrics metrics) {
        if (low < high) {
            int pi = partition(arr, low, high, metrics);
            quickSortHelper(arr, low, pi - 1, metrics);
            quickSortHelper(arr, pi + 1, high, metrics);
        }
    }

    /** Lomuto partition with median-of-three pivot to avoid O(n^2) on sorted input. */
    private static int partition(int[] arr, int low, int high, SortMetrics metrics) {
        // Median-of-three pivot selection
        int mid = low + (high - low) / 2;
        if (arr[mid] < arr[low])  swap(arr, low, mid, metrics);
        if (arr[high] < arr[low]) swap(arr, low, high, metrics);
        if (arr[mid] < arr[high]) swap(arr, mid, high, metrics);
        // arr[high] is now the median – use as pivot
        int pivot = arr[high];
        int i     = low - 1;

        for (int j = low; j < high; j++) {
            if (metrics != null) metrics.comparisons++;
            if (arr[j] <= pivot) {
                i++;
                swap(arr, i, j, metrics);
            }
        }
        swap(arr, i + 1, high, metrics);
        return i + 1;
    }

    // =========================================================================
    // Heap Sort – O(n log n) all cases, O(1) space, NOT stable
    // =========================================================================

    /**
     * Sorts {@code arr} using heap sort (ascending order).
     *
     * @param arr     the array to sort (modified in-place)
     * @param metrics counters for comparisons and swaps
     */
    public static void heapSort(int[] arr, SortMetrics metrics) {
        if (arr == null || arr.length <= 1) return;
        int n = arr.length;

        // Build max-heap
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(arr, n, i, metrics);
        }

        // Extract elements from heap one by one
        for (int i = n - 1; i > 0; i--) {
            swap(arr, 0, i, metrics);
            heapify(arr, i, 0, metrics);
        }
    }

    private static void heapify(int[] arr, int n, int root, SortMetrics metrics) {
        int largest = root;
        int left    = 2 * root + 1;
        int right   = 2 * root + 2;

        if (left < n) {
            if (metrics != null) metrics.comparisons++;
            if (arr[left] > arr[largest]) largest = left;
        }
        if (right < n) {
            if (metrics != null) metrics.comparisons++;
            if (arr[right] > arr[largest]) largest = right;
        }
        if (largest != root) {
            swap(arr, root, largest, metrics);
            heapify(arr, n, largest, metrics);
        }
    }

    // =========================================================================
    // Utility
    // =========================================================================

    private static void swap(int[] arr, int i, int j, SortMetrics metrics) {
        if (i == j) return;
        int temp = arr[i];
        arr[i]   = arr[j];
        arr[j]   = temp;
        if (metrics != null) metrics.swaps++;
    }
}
