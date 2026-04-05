package com.csc483.assignment.test;

import com.csc483.assignment.search.Product;
import com.csc483.assignment.search.SearchAlgorithms;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 tests for {@link SearchAlgorithms} and {@link Product}.
 *
 * <p>Covers: sequential search, binary search, name search,
 * addProduct (sorted insertion), hybrid name search.
 *
 * @author Student ID: U2022/5570224
 */
@DisplayName("CSC483 – Search Algorithms Tests")
class SearchAlgorithmsTest {

    private Product[] unsortedProducts;
    private Product[] sortedProducts;
    private static final int SIZE = 10;

    @BeforeEach
    void setUp() {
        unsortedProducts = new Product[SIZE];
        // Unsorted IDs: 50 40 30 20 10 60 70 80 90 100
        int[] ids = {50, 40, 30, 20, 10, 60, 70, 80, 90, 100};
        String[] names = {"Alpha","Beta","Gamma","Delta","Epsilon",
                        "Zeta","Eta","Theta","Iota","Kappa"};
        for (int i = 0; i < SIZE; i++) {
            unsortedProducts[i] = new Product(ids[i], names[i], "Electronics",
                                               ids[i] * 1.5, ids[i] / 2);
        }

        sortedProducts = Arrays.copyOf(unsortedProducts, SIZE);
        Arrays.sort(sortedProducts);
    }

    // =========================================================================
    // Product class tests
    // =========================================================================

    @Test
    @DisplayName("Product – valid construction")
    void testProductValidConstruction() {
        Product p = new Product(1, "TestProduct", "Laptops", 999.99, 10);
        assertEquals(1,         p.getProductId());
        assertEquals("TestProduct", p.getProductName());
        assertEquals("Laptops", p.getCategory());
        assertEquals(999.99,    p.getPrice(), 0.001);
        assertEquals(10,        p.getStockQuantity());
    }

    @Test
    @DisplayName("Product – invalid ID throws exception")
    void testProductInvalidId() {
        assertThrows(IllegalArgumentException.class,
                    () -> new Product(-1, "Name", "Cat", 10.0, 5));
    }

    @Test
    @DisplayName("Product – null name throws exception")
    void testProductNullName() {
        assertThrows(IllegalArgumentException.class,
                    () -> new Product(1, null, "Cat", 10.0, 5));
    }

    @Test
    @DisplayName("Product – negative price throws exception")
    void testProductNegativePrice() {
        assertThrows(IllegalArgumentException.class,
                    () -> new Product(1, "Name", "Cat", -1.0, 5));
    }

    @Test
    @DisplayName("Product – equals by ID")
    void testProductEquals() {
        Product p1 = new Product(42, "NameA", "CatA", 10.0, 1);
        Product p2 = new Product(42, "NameB", "CatB", 20.0, 2);
        assertEquals(p1, p2);
    }

    @Test
    @DisplayName("Product – compareTo sorts ascending")
    void testProductCompareTo() {
        Product low  = new Product(5,  "A", "C", 1.0, 1);
        Product high = new Product(10, "B", "C", 2.0, 1);
        assertTrue(low.compareTo(high) < 0);
        assertTrue(high.compareTo(low) > 0);
        assertEquals(0, low.compareTo(new Product(5, "X", "Y", 3.0, 0)));
    }

    // =========================================================================
    // Sequential Search tests
    // =========================================================================

    @Test
    @DisplayName("Sequential – finds product at first position (best case)")
    void testSequentialBestCase() {
        int firstId = unsortedProducts[0].getProductId();
        Product result = SearchAlgorithms.sequentialSearchById(unsortedProducts, firstId);
        assertNotNull(result);
        assertEquals(firstId, result.getProductId());
    }

    @Test
    @DisplayName("Sequential – finds product in middle")
    void testSequentialMiddle() {
        int midId = unsortedProducts[SIZE / 2].getProductId();
        assertNotNull(SearchAlgorithms.sequentialSearchById(unsortedProducts, midId));
    }

    @Test
    @DisplayName("Sequential – returns null for missing ID (worst case)")
    void testSequentialWorstCase() {
        assertNull(SearchAlgorithms.sequentialSearchById(unsortedProducts, 999999));
    }

    @Test
    @DisplayName("Sequential – throws on null array")
    void testSequentialNullArray() {
        assertThrows(IllegalArgumentException.class,
                    () -> SearchAlgorithms.sequentialSearchById(null, 1));
    }

    @Test
    @DisplayName("Sequential – empty array returns null")
    void testSequentialEmptyArray() {
        assertNull(SearchAlgorithms.sequentialSearchById(new Product[0], 1));
    }

    // =========================================================================
    // Binary Search tests
    // =========================================================================

    @ParameterizedTest
    @ValueSource(ints = {10, 20, 30, 40, 50, 60, 70, 80, 90, 100})
    @DisplayName("Binary – finds every product in sorted array")
    void testBinaryFindsAll(int targetId) {
        Product result = SearchAlgorithms.binarySearchById(sortedProducts, targetId);
        assertNotNull(result);
        assertEquals(targetId, result.getProductId());
    }

    @Test
    @DisplayName("Binary – returns null for missing ID")
    void testBinaryMissingId() {
        assertNull(SearchAlgorithms.binarySearchById(sortedProducts, 999));
    }

    @Test
    @DisplayName("Binary – finds middle element (best case)")
    void testBinaryMiddleElement() {
        int midId = sortedProducts[SIZE / 2].getProductId();
        assertNotNull(SearchAlgorithms.binarySearchById(sortedProducts, midId));
    }

    @Test
    @DisplayName("Binary – throws on null array")
    void testBinaryNullArray() {
        assertThrows(IllegalArgumentException.class,
                    () -> SearchAlgorithms.binarySearchById(null, 1));
    }

    @Test
    @DisplayName("Binary – single-element array hit")
    void testBinarySingleElementHit() {
        Product[] single = {new Product(7, "Solo", "Cat", 5.0, 1)};
        assertNotNull(SearchAlgorithms.binarySearchById(single, 7));
    }

    @Test
    @DisplayName("Binary – single-element array miss")
    void testBinarySingleElementMiss() {
        Product[] single = {new Product(7, "Solo", "Cat", 5.0, 1)};
        assertNull(SearchAlgorithms.binarySearchById(single, 99));
    }

    // =========================================================================
    // searchByName tests
    // =========================================================================

    @Test
    @DisplayName("searchByName – case-insensitive hit")
    void testSearchByNameHit() {
        Product result = SearchAlgorithms.searchByName(unsortedProducts, "alpha");
        assertNotNull(result);
        assertEquals("Alpha", result.getProductName());
    }

    @Test
    @DisplayName("searchByName – miss returns null")
    void testSearchByNameMiss() {
        assertNull(SearchAlgorithms.searchByName(unsortedProducts, "Nonexistent"));
    }

    @Test
    @DisplayName("searchByName – throws on null name")
    void testSearchByNameNullName() {
        assertThrows(IllegalArgumentException.class,
                    () -> SearchAlgorithms.searchByName(unsortedProducts, null));
    }

    // =========================================================================
    // Hybrid / addProduct tests
    // =========================================================================

    @Test
    @DisplayName("Hybrid – builds index and finds by name")
    void testHybridNameSearch() {
        SearchAlgorithms sa = new SearchAlgorithms();
        sa.buildNameIndex(sortedProducts);
        Product result = sa.hybridNameSearch("Alpha");
        assertNotNull(result);
        assertEquals("Alpha", result.getProductName());
    }

    @Test
    @DisplayName("Hybrid – miss returns null")
    void testHybridNameSearchMiss() {
        SearchAlgorithms sa = new SearchAlgorithms();
        sa.buildNameIndex(sortedProducts);
        assertNull(sa.hybridNameSearch("DoesNotExist"));
    }

    @Test
    @DisplayName("addProduct – insertion maintains sorted order")
    void testAddProductMaintainsSortedOrder() {
        SearchAlgorithms sa = new SearchAlgorithms();
        sa.buildNameIndex(sortedProducts);

        Product newProd = new Product(55, "NewProduct", "Storage", 150.0, 5);
        Product[] updated = sa.addProduct(sortedProducts, newProd);

        // Check total size increased
        assertEquals(sortedProducts.length + 1, updated.length);

        // Check sorted order
        for (int i = 1; i < updated.length; i++) {
            if (updated[i] != null && updated[i - 1] != null) {
                assertTrue(updated[i].getProductId() >= updated[i - 1].getProductId(),
                        "Array not sorted at index " + i);
            }
        }

        // Verify new product is findable via binary search
        assertNotNull(SearchAlgorithms.binarySearchById(updated, 55));
    }

    @Test
    @DisplayName("addProduct – new product reflected in hybrid index")
    void testAddProductUpdatesIndex() {
        SearchAlgorithms sa = new SearchAlgorithms();
        sa.buildNameIndex(sortedProducts);

        Product newProd = new Product(55, "ZetaPrime", "Storage", 99.0, 3);
        sa.addProduct(sortedProducts, newProd);

        assertNotNull(sa.hybridNameSearch("ZetaPrime"));
    }
}
