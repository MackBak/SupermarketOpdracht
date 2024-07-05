/**
 * Supermarket Customer check-out and Cashier simulation
 * @author:  Mack Bakkum - 500721202
 * @dates: 03 - 05 July 2024
 */

package model;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Customer implements Comparable<Customer> {
    private LocalTime queuedAt;      // time of arrival at cashier
    private String zipCode;          // zip-code of the customer
    private Map<Product, Integer> itemsCart = new HashMap<>();;     // items purchased by customer
    private int actualWaitingTime;   // actual waiting time in seconds before check-out
    private int actualCheckOutTime;  // actual check-out time at cashier in seconds

    public Customer() {
    }

    public Customer(LocalTime queuedAt, String zipCode) {
        this.queuedAt = queuedAt;
        this.zipCode = zipCode;
    }

    /**
     * calculate the total number of items purchased by this customer
     * @return
     */
    public int getNumberOfItems() {
        int numItems = 0;
        for (Integer amount : itemsCart.values()) {
            // The loop counts the total amount of each product and adds this to the sum.
            numItems += amount;
        }
        return numItems;
    }


    public void addToCart(Product product, int number) {
        if (itemsCart.containsKey(product)) {
            int currentAmount = itemsCart.get(product);
            itemsCart.put(product, currentAmount + number); // If the product already exists in the cart then adds the new amoun with currentAmount.
        } else {
            itemsCart.put(product, number); // If product doesn't exist in cart yet adds product + amount of it.
        }
    }

    public double calculateTotalBill() {
        return itemsCart.entrySet().stream() // With entrySet I get the Set that holds all key value pirs of itemsCart (HashMap)
                .mapToDouble(entry -> entry.getKey().getPrice() * entry.getValue()) // mapToDouble makes all elemnts in the stream a double. With the lambda expression I retrieve the product & price and is multiplied by amount of products.
                .sum(); // The result of the lambda expression above is the sum, this is returned.
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("queuedAt: " + queuedAt);
        result.append("\nzipCode: " + zipCode);
        result.append("\nPurchases:" );
        for (Product product : itemsCart.keySet()) {
            result.append("\n\t" + product + ": " + itemsCart.get(product));
        }
        result.append("\n");
        return result.toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(queuedAt); // Calculates a hash code for a a customer based on the queued time.
    }

    @Override
    public boolean equals (Object customer) {
        if (this == customer) {
            return true;
        } if (customer == null) { // If customer is null then can't be equal to current object anyways.
            return false;
        } if (getClass() != customer.getClass()) { // Used to verify comparison object is of the same class.
            return false;
        }
        Customer otherCustomer = (Customer) customer; // Object is the input, so I need to cast the customer to Customer type so queuedAt can be used.
        return Objects.equals(queuedAt, otherCustomer.queuedAt);
    }

    @Override
    public int compareTo(Customer other) {
        return queuedAt.compareTo(other.queuedAt); // Assignment says queuedAt is unique, so using it to compare.
    }


    public LocalTime getQueuedAt() {
        return queuedAt;
    }

    public String getZipCode() {
        return zipCode;
    }

    public Map<Product, Integer> getItemsCart() {
        return itemsCart;
    }
}
