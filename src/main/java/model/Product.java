package model;

import java.util.Objects;

/**
 * Supermarket Customer check-out
 * @author  hbo-ict@hva.nl
 */

public class Product implements Comparable<Product> {
    private String code;            // a unique product code; identical codes designate identical products
    private String description;     // the product description, useful for reporting
    private double price;           // the product's price

    public Product() {
    }

    public Product(String code, String description, double price) {
        this.code = code;
        this.description = description;
        this.price = price;
    }

    @Override
    public int hashCode() {
        return Objects.hash(code); // Calculates a hash code for a product based on its unique code.
    }

    @Override
    public boolean equals (Object product) {
        if (this == product) return true;
        if (product == null || getClass() != product.getClass()) return false; // If product is null then can't be equal to current object. If not the same class it can also not be the same object.
        Product product2 = (Product) product;
        return Objects.equals(code, product2.code);
    }

    @Override
    public int compareTo(Product otherProduct) {
        return code.compareTo(otherProduct.code); // Using code to compare products as code is unique.
    }

    @Override
    public String toString() {
        return description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }
}
