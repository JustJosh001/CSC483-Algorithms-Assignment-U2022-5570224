package com.csc483.assignment.search;

/**
 * Represents a product in the TechMart online store inventory.
 * Implements Comparable to support sorting by product ID.
 *
 * @author CSC483 Student
 * @version 1.0
 */
public class Product implements Comparable<Product> {

    /** Unique identifier for the product */
    private int productId;

    /** Name of the product */
    private String productName;

    /** Category the product belongs to */
    private String category;

    /** Price of the product in dollars */
    private double price;

    /** Number of units available in stock */
    private int stockQuantity;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    /**
     * Constructs a new Product with the given attributes.
     *
     * @param productId     unique identifier
     * @param productName   name of the product
     * @param category      product category
     * @param price         price in dollars
     * @param stockQuantity number of units in stock
     */
    public Product(int productId, String productName, String category,
                double price, int stockQuantity) {
        if (productId <= 0) throw new IllegalArgumentException("productId must be positive");
        if (productName == null || productName.isEmpty())
            throw new IllegalArgumentException("productName must not be null or empty");
        if (price < 0) throw new IllegalArgumentException("price must be non-negative");
        if (stockQuantity < 0) throw new IllegalArgumentException("stockQuantity must be non-negative");

        this.productId     = productId;
        this.productName   = productName;
        this.category      = category;
        this.price         = price;
        this.stockQuantity = stockQuantity;
    }

    // -------------------------------------------------------------------------
    // Getters & Setters
    // -------------------------------------------------------------------------

    public int    getProductId()     { return productId; }
    public String getProductName()   { return productName; }
    public String getCategory()      { return category; }
    public double getPrice()         { return price; }
    public int    getStockQuantity() { return stockQuantity; }

    public void setProductName(String productName)   { this.productName = productName; }
    public void setCategory(String category)         { this.category = category; }
    public void setPrice(double price)               { this.price = price; }
    public void setStockQuantity(int stockQuantity)  { this.stockQuantity = stockQuantity; }

    // -------------------------------------------------------------------------
    // Comparable
    // -------------------------------------------------------------------------

    /**
     * Compares this product to another by product ID (ascending).
     */
    @Override
    public int compareTo(Product other) {
        return Integer.compare(this.productId, other.productId);
    }

    // -------------------------------------------------------------------------
    // Object overrides
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Product)) return false;
        Product p = (Product) obj;
        return this.productId == p.productId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(productId);
    }

    @Override
    public String toString() {
        return String.format("Product{id=%d, name='%s', category='%s', price=%.2f, stock=%d}",
                productId, productName, category, price, stockQuantity);
    }
}
