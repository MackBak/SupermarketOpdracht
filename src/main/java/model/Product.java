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

    // TODO Stap 1: implement relevant overrides of equals(), hashcode(), compareTo for
    //  model classes to be able to use them in sets, maps

    @Override
    public int hashCode() {
        return Objects.hash(description);
    }

    @Override
    public boolean equals (Object product) {
        if (this == product) {
            return true;
        } if (product == null) {
            return false;
        } if (getClass() != product.getClass()) {
            return false;
        }
        Product otherProduct = (Product) product;
        return Objects.equals(description, otherProduct.description);
    }

    @Override
    public int compareTo(Product otherProduct) {
        return description.compareTo(otherProduct.description); // Assignment says descirption of product is unique so I'm using it to compare.
    }

    @Override
    public String toString() {
        return "ToString in product: " + description + " " + price + " " + code;
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
