package model; /**
 * Supermarket Customer check-out and Cashier simulation
 * @author  hbo-ict@hva.nl
 */

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

    // TODO stap 1: implement relevant overrides of equals(), hashcode(), compareTo for
    //  model classes to be able to use them in sets, maps



    /**
     * calculate the total number of items purchased by this customer
     * @return
     */
    public int getNumberOfItems() {
        int numItems = 0;
        // TODO stap 2: Calculate the total number of items
        for (Integer amount : itemsCart.values()) {
            // Loop counts the total amount of each product to the sum
            numItems += amount;
        }
        return numItems;
    }


    public void addToCart(Product product, int number) {
        // TODO stap 2: When adding a number of products to the cart,
        //  the number should be adjusted when product already exists in cart

        if (itemsCart.containsKey(product)) {
            int currentAmount = itemsCart.get(product);
            itemsCart.put(product, currentAmount + number); // If the product already exists in the cart then adds the new amoun with currentAmount.
        } else {
            itemsCart.put(product, number); // If product doesn't exist in cart yet adds product + amount only. (no currentAmount here)
        }
    }

    public double calculateTotalBill() {
        double totalBill = 0.0;
        // TODO stap 4: Calculate the total cost of all items, use a stream
        return totalBill;
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
        return Objects.hash(queuedAt);
    }

    @Override
    public boolean equals (Object customer) {
        if (this == customer) {
            return true;
        } if (customer == null) {
            return false;
        } if (getClass() != customer.getClass()) {
            return false;
        }
        Customer otherCustomer = (Customer) customer;
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
