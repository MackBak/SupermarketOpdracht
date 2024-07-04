package model;

import java.time.LocalTime;
import java.util.*;

/**
 * Supermarket Customer and purchase statistics
 * @author  hbo-ict@hva.nl
 */
public class Supermarket {

    private String name;                 // name of the case for reporting purposes
    private Set<Product> products;      // a set of products that is being sold in the supermarket
    private Set<Customer> customers;   // a set of customers that have visited the supermarket
    private LocalTime openTime;         // start time of the simulation
    private LocalTime closingTime;      // end time of the simulation
    private static final int INTERVAL_IN_MINUTES = 15; // to use for number of customers and revenues per 15 minute intervals

    public Supermarket() {
        initializeCollections();
    }

    public Supermarket(String name, LocalTime openTime, LocalTime closingTime) {
        this.name = name;
        this.setOpenTime(openTime);
        this.setClosingTime(closingTime);
        initializeCollections();
    }

    public void initializeCollections() {
        products = new HashSet<>(); // Initializing products as HashShet for products because products are unique.
        customers = new TreeSet<>(); // Iniializing customers as TreeSet because I need to sort them by order (queuedAt).
    }

    public int getTotalNumberOfItems() {
        int totalItems = 0;
        for (Customer customer : customers) {
            totalItems += customer.getNumberOfItems(); // Loops over all customers and then per customer calls the getNumberOfItems method and adds this to the sum.
        }
        return totalItems;
    }

    private void printErrorMessage() {
        System.out.println("No products or customers have been set up...");
    }

    private boolean checkSetupErrorProductCustomers() {
        return this.customers == null || this.products == null ||
                this.customers.size() == 0 || this.products.size() == 0;
    }

    /**
     * report statistics of the input data of customer
     */
    public void printCustomerStatistics() {
        if (checkSetupErrorProductCustomers()) {
            printErrorMessage();
            return;
        }
        System.out.printf("\n>>>>> Customer Statistics of '%s' between %s and %s <<<<<\n",
                this.name, this.openTime, this.closingTime);
        System.out.println();
        // TODO stap 4: calculate and show the customer(s) with the highest bill and most paying customer
        System.out.printf("Customer that has the highest bill of %.2f euro: \n", findHighestBill());
        System.out.println(findMostPayingCustomer());
        System.out.println();
    }

    /**
     * report statistics of data of products
     */
    public void printProductStatistics() {
        if (checkSetupErrorProductCustomers()) {
            printErrorMessage();
            return;
        }
        System.out.println("\n>>>>> Product Statistics of all purchases <<<<<");
        System.out.printf("%d customers have shopped %d items out of %d different products\n",
                this.customers.size(), this.getTotalNumberOfItems(), this.products.size());
        System.out.println();
        System.out.println(">>> Products and total number bought:");
        System.out.println();

        // TODO stap 3: display the description of the products and total number bought per product.
        Map <Product, Integer> productCounts = findNumberOfProductsBought();
        for (Map.Entry<Product, Integer> entry : productCounts.entrySet()) {
            System.out.println(entry.getKey().getDescription() + " " + entry.getValue());
        }

        System.out.println();
        System.out.println(">>> Products and zipcodes");
        System.out.println();

        // TODO stap 3: display the description of the products and per product all zipcodes where the product is bought.
        Map<Product, Set<String>> zipCodesPerProduct = findZipcodesPerProduct();
        for (Map.Entry<Product, Set<String>> entry : zipCodesPerProduct.entrySet()) {
            System.out.println(entry.getKey().getDescription() + ":");
            for (String zipCode : entry.getValue()) {
                System.out.println(" " + zipCode + ", ");
            }
            System.out.println(); // Empty println to move to next ln after printing the zip codes for the product.
        }


        System.out.println();
        System.out.println(">>> Most popular products");
        System.out.println();
        // TODO stap 5: display the product(s) that most customers bought
        System.out.println("Product(s) bought by most customers: ");

        System.out.println();
        System.out.println(">>> Most bought products per zipcode");
        System.out.println();
        // TODO stap 5: display most bought products per zipcode

        System.out.println();
    }

    /**
     * report statistics of the input data of customer
     */
    public void printRevenueStatistics() {
        System.out.println("\n>>>>> Revenue Statistics of all purchases <<<<<");
        // TODO stap 5: calculate and show the total revenue and the average revenue
        System.out.printf("\nTotal revenue = %.2f\nAverage revenue per customer = %.2f\n", findTotalRevenue(), findAverageRevenue());
        System.out.println();
        System.out.print(">>> Revenues per zip-code:\n");
        System.out.println();
        // TODO stap 5 calculate and show total revenues per zipcode, use forEach and lambda expression
        Map<String, Double> revenues = this.getRevenueByZipcode();

        System.out.println();
        System.out.printf(">>> Revenues per interval of %d minutes\n", INTERVAL_IN_MINUTES);
        System.out.println();
        // TODO stap 5: show the revenues per time interval of 15 minutes, use forEach and lambda expression

    }

    /**
     * @return Map with total number of purchases per product
     */
    public Map<Product, Integer> findNumberOfProductsBought() {

        // TODO stap 3: create an appropriate data structure for the products and their numbers bought
        //  and calculate the contents

        Map<Product, Integer> productCounter = new HashMap<>();
        for (Customer customer : customers) {
            for (Map.Entry<Product, Integer> entry : customer.getItemsCart().entrySet()) {
                Product product = entry.getKey();
                int quantity = entry.getValue();
                productCounter.put(product, productCounter.getOrDefault(product, 0) + quantity);
            }
        }
        return productCounter;
    }

    /**
     * builds a map of products and set of zipcodes where product has been bought
     * @return Map with set of zipcodes per product
     */
    public Map<Product, Set<String>> findZipcodesPerProduct() {
        // TODO stap 3: create an appropriate data structure for the products and their zipcodes
        //  and find all the zipcodes per product
        Map<Product, Set<String>> zipCodesPerProduct = new HashMap<>();
        for (Customer customer : customers) {
            String zipCode = customer.getZipCode();
            for (Product product : customer.getItemsCart().keySet()) {

                // Original code: zipCodesPerProduct.put(product, zipCodesPerProduct.getOrDefault(product, new HashSet<>()));
                zipCodesPerProduct.computeIfAbsent(product, k -> new HashSet<>()).add(zipCode);
            }
        }
        return zipCodesPerProduct;
    }

    /**
     * builds a map of zipcodes with maps of products with number bougth
     * @return Map with map of product and number per zipcode
     */
    public Map<String, Map<Product, Integer>> findNumberOfProductsByZipcode() {
        Map<String, Map<Product, Integer>> productsByZipCode = new HashMap<>();
        for (Customer customer : customers) {
            String zipCode = customer.getZipCode();

            // Retrieving or creating new map for the postal code:
            Map<Product, Integer> productCounter = productsByZipCode.computeIfAbsent(zipCode, k -> new HashMap<>());

            // Iterates through the items in the shopping cart:
            for (Map.Entry<Product, Integer> entry : customer.getItemsCart().entrySet()) {
                Product product = entry.getKey();
                int quantity = entry.getValue();

                // Putting new products in map or updating existing count.
                productCounter.put(product, productCounter.getOrDefault(product, 0) + quantity);
            }
        }
        return productsByZipCode;
    }

    /**
     *
     * @return value of the highest bill
     */
    public double findHighestBill() {
        // TODO stap 4: use a stream to find the highest bill
        return 0.0;
    }

    /**
     *
     * @return customer with highest bill
     */
    public Customer findMostPayingCustomer() {
        // TODO stap 4: use a stream and the highest bill to find the most paying customer
        return null;
    }

    /**
     * calculates the total revenue of all customers purchases
     * @return total revenue
     */
    public double findTotalRevenue() {
        // TODO Stap 5: use a stream to find the total of all bills
        return 0.0;
    }

    /**
     * calculates the average revenue of all customers purchases
     * @return average revenue
     */
    public double findAverageRevenue() {
        // TODO Stap 5: use a stream to find the average of the bills
        return 0.0;
    }

    /**
     * calculates a map of aggregated revenues per zip code that is also ordered by zip code
     * @return Map with revenues per zip code
     */
    public Map<String, Double> getRevenueByZipcode() {
        // TODO Stap 5: create an appropriate data structure for the revenue
        //  use stream and collector to find the content

        return null;
    }

    /**
     * finds the product(s) found in the most carts of customers
     * @return Set with products bought by most customers
     */
    public Set<Product> findMostPopularProducts() {
        // TODO Stap 5: create an appropriate data structure for the most popular products and find its contents

        return null;
    }

    /**
     *
     * calculates a map of most bought products per zip code that is also ordered by zip code
     * if multiple products have the same maximum count, just pick one.
     * @return Map with most bought product per zip code
     */
    public Map<String, Product> findMostBoughtProductByZipcode() {
        // TODO Stap 5: create an appropriate data structure for the mostBought
        //  and calculate its contents

        return null;
    }

    /**
     *
     * calculates a map of revenues per time interval based on the length of the interval in minutes
     * @return Map with revenues per interval
     */
    public Map<LocalTime, Double> calculateRevenuePerInterval(int minutes) {
        // TODO Stap 5: create an appropiate data structure for the revenue per time interval
        //  Start time of an interval is a key. Find the total revenue for each interval

        return null;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public Set<Customer> getCustomers() {
        return customers;
    }

    public void setOpenTime(LocalTime openTime) {
        this.openTime = openTime;
    }

    public void setClosingTime(LocalTime closingTime) {
        this.closingTime = closingTime;
    }


}
