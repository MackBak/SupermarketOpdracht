package model; /**
 * Supermarket Customer check-out and Cashier simulation
 * @author  hbo-ict@hva.nl
 */


import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class Customer {
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
        return numItems;
    }


    public void addToCart(Product product, int number) {
        // TODO stap 2: When adding a number of products to the cart,
        //  the number should be adjusted when product already exists in cart
    }

    public double calculateTotalBill() {
        double totalBill = 0.0;
        // TODO stap 4: Calculate the total cost of all items, use a stream
        return totalBill;
    }

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
