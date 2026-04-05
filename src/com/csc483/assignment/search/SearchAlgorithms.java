package com.csc483.assignment.search;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Provides sequential search, binary search, and a hybrid index-based
 * search for the TechMart product catalog.
 *
 * <p>Complexity Summary:
 * <ul>
 *   <li>sequentialSearchById  – O(1) best, O(n) average, O(n) worst</li>
 *   <li>binarySearchById      – O(1) best, O(log n) average, O(log n) worst</li>
 *   <li>searchByName          – O(n) all cases (names unsorted)</li>
 *   <li>addProduct            – O(n) (shift to maintain sorted order)</li>
 *   <li>hybridNameSearch      – O(1) average (HashMap lookup)</li>
 * </ul>
 *
 * @author Student ID: U2022/5570224
 * @version 1.0
 */
public class SearchAlgorithms {

    // =========================================================================
    // Part B – Core Search Methods
    // =========================================================================

    /**
     * Performs a sequential (linear) search on the products array by product ID.
     *
     * <p>Best Case:  O(1)  – target found at index 0 (1 comparison)<br>
     * Average Case: O(n/2) ~ O(n) – target near middle<br>
     * Worst Case:   O(n)  – target not present or at last position (n comparisons)
     *
     * @param products array of Product objects (need not be sorted)
     * @param targetId the product ID to search for
     * @return the matching Product, or {@code null} if not found
     */
    public static Product sequentialSearchById(Product[] products, int targetId) {
        if (products == null) throw new IllegalArgumentException("products array must not be null");

        for (Product product : products) {
            if (product != null && product.getProductId() == targetId) {
                return product;
            }
        }
        return null; // not found
    }

    /**
     * Performs a binary search on a <strong>sorted</strong> products array by product ID.
     *
     * <p><b>Precondition:</b> The array must be sorted in ascending order of product ID.<br>
     * Best Case:  O(1)  – target found at the midpoint<br>
     * Average Case: O(log n) – search space halved each iteration<br>
     * Worst Case:   O(log n) – target not present (search space exhausted)
     *
     * @param products array sorted by productId (ascending)
     * @param targetId the product ID to search for
     * @return the matching Product, or {@code null} if not found
     */
    public static Product binarySearchById(Product[] products, int targetId) {
        if (products == null) throw new IllegalArgumentException("products array must not be null");

        int low  = 0;
        int high = products.length - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;  // avoids integer overflow

            if (products[mid] == null) {
                high = mid - 1;
                continue;
            }

            int midId = products[mid].getProductId();

            if (midId == targetId) {
                return products[mid];           // found – O(1) best case
            } else if (midId < targetId) {
                low = mid + 1;                  // search right half
            } else {
                high = mid - 1;                 // search left half
            }
        }
        return null; // not found
    }

    /**
     * Performs a sequential search by product name (case-insensitive).
     *
     * <p>Because product names are unsorted, only sequential search is viable.
     * Complexity: O(n) for all cases.
     *
     * @param products  array of Product objects
     * @param targetName the product name to search for
     * @return the first matching Product, or {@code null} if not found
     */
    public static Product searchByName(Product[] products, String targetName) {
        if (products == null)   throw new IllegalArgumentException("products array must not be null");
        if (targetName == null) throw new IllegalArgumentException("targetName must not be null");

        for (Product product : products) {
            if (product != null && product.getProductName().equalsIgnoreCase(targetName)) {
                return product;
            }
        }
        return null; // not found
    }

    // =========================================================================
    // Part C – Hybrid Search Approach
    // =========================================================================

    /**
     * Maintains a name-to-Product index (HashMap) for O(1) average name lookups.
     * Key = lowercased product name, Value = Product reference.
     */
    private final Map<String, Product> nameIndex = new HashMap<>();

    /**
     * Builds the name index from an existing products array.
     * Call this once after the initial data load.
     *
     * @param products the initial sorted products array
     */
    public void buildNameIndex(Product[] products) {
        nameIndex.clear();
        for (Product p : products) {
            if (p != null) {
                nameIndex.put(p.getProductName().toLowerCase(), p);
            }
        }
    }

    /**
     * Hybrid name search: O(1) average – uses the pre-built HashMap index.
     *
     * @param targetName the product name to search for
     * @return the matching Product, or {@code null} if not found
     */
    public Product hybridNameSearch(String targetName) {
        if (targetName == null) throw new IllegalArgumentException("targetName must not be null");
        return nameIndex.get(targetName.toLowerCase());
    }

    /**
     * Inserts a new product into the sorted array while maintaining sort order
     * by productId, and updates the name index.
     *
     * <p>Strategy: find the correct insertion position using binary search (O(log n)),
     * then shift elements right to make room (O(n)).
     * Overall time complexity: O(n) due to the shift.
     *
     * @param products   the current sorted products array (must have one extra null slot at end)
     * @param newProduct the product to insert
     * @return the updated array with the new product inserted in sorted order
     * @throws IllegalStateException if the array has no room for insertion
     */
    public Product[] addProduct(Product[] products, Product newProduct) {
        if (products == null)   throw new IllegalArgumentException("products array must not be null");
        if (newProduct == null) throw new IllegalArgumentException("newProduct must not be null");

        // Find last valid (non-null) index
        int lastValid = -1;
        for (int i = products.length - 1; i >= 0; i--) {
            if (products[i] != null) { lastValid = i; break; }
        }

        // If array is full, create a larger array
        Product[] result = products;
        if (lastValid == products.length - 1) {
            result = Arrays.copyOf(products, products.length + 1);
        }

        // Binary search for insertion position
        int low = 0, high = lastValid, insertPos = lastValid + 1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (result[mid] == null || result[mid].getProductId() > newProduct.getProductId()) {
                insertPos = mid;
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }

        // Shift elements right to make room – O(n)
        System.arraycopy(result, insertPos, result, insertPos + 1,
                    lastValid - insertPos + 1);
        result[insertPos] = newProduct;

        // Update name index – O(1)
        nameIndex.put(newProduct.getProductName().toLowerCase(), newProduct);

        return result;
    }
}
