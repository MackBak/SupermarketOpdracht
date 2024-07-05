package model;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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

        Customer customerWithHighestBill = customers.stream() // Creating stream of customers
                        .filter(customer -> customer.calculateTotalBill() == findHighestBill())
                        .findFirst() // Finding first customer that matches the filter. Originally wanted to make a List but Gerke said it was ok to find the first that matches.
                        .orElse(null); // If no customer it ll return Null.
        
        System.out.printf("Customer that has the highest bill of %.2f euro: \n", findHighestBill());
        System.out.println(customerWithHighestBill != null ? customerWithHighestBill : "No customer! Check if anything went wrong!"); // Ternary statement instead of If/Else: If customer = notNull > Print the customer. If null > print no customer message.


        System.out.println(findMostPayingCustomer()); // TODO: I Think this can be removed??
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
        Map<String, Double> revenues = this.getRevenueByZipcode(); // Code was already supplied: Creates TreeMap (to sort automatically) called revenues.
        revenues.entrySet().stream() // Creating a stream from TreeMap revenues.
                        .sorted(Map.Entry.comparingByKey()) // Sorts the map, but I'm not sure if I need it since a TreeMap should sort automatically? TODO: TEST WITH REMOVING THIS!!!
                        .forEach(entry -> System.out.print(entry.getKey() + " " + entry.getValue())); // ForEach loop that prints all the keys (postalcode) and then the values (revenue)

        System.out.println();
        System.out.printf(">>> Revenues per interval of %d minutes\n", INTERVAL_IN_MINUTES);
        System.out.println();
        // TODO stap 5: show the revenues per time interval of 15 minutes, use forEach and lambda expression
        Map<LocalTime, Double> revenuePerInterval = calculateRevenuePerInterval(INTERVAL_IN_MINUTES); // Creating TreeMap in method calculateRevenuePerInterval. Using a TreeMap since I can easily sort by time.
        revenuePerInterval.forEach((time, revenue) -> System.out.printf("%-20s - %.2f\n", time.format(DateTimeFormatter.ofPattern("HH:mm")), revenue));
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
        return customers.stream() // Creating a stream of Customers
                .mapToDouble(Customer::calculateTotalBill) // Applying calculateTotalBill method to all the customers in the stream and converting this to a double.
                .max() // Finding this max value in the stream.
                .orElse(0.0); // If empty provided value of 0.0 so no crashes happen.
    }

    /**
     *
     * @return customer with highest bill
     */

    // TODO: Check with Gerke if this needs to become a list. 2 users with the same highest bill could become an issue I think.

    public Customer findMostPayingCustomer() {
        double biggestBill = findHighestBill(); // Using method findHighestBill to get the biggest bill.
        return customers.stream() // Creating a stream of customers.
                .filter (customer -> customer.calculateTotalBill() == biggestBill) // With this filter I only keep the customer that matches the biggestBill double.
                .findFirst() // Finds the first customer that matches the filter.
                .orElse(null); // Returns null if there's no customer.
         }

    /**
     * calculates the total revenue of all customers purchases
     * @return total revenue
     */
    public double findTotalRevenue() {
        return customers.stream()// Creating a stream of customers
                .mapToDouble(Customer::calculateTotalBill) // Applying calculateTtotalBill to all Customers so it gets the total bill.
                .sum(); // Finally returns the sum
    }

    /**
     * calculates the average revenue of all customers purchases
     * @return average revenue
     */
    public double findAverageRevenue() {
        return customers.isEmpty() ? 0.0 : findTotalRevenue() / customers.size(); // Returns 0 if no customers, using findTotalRevenue and dividing it by size of customer list to divide.
    }

    /**
     * calculates a map of aggregated revenues per zip code that is also ordered by zip code
     * @return Map with revenues per zip code
     */
    public Map<String, Double> getRevenueByZipcode() {
        return customers.stream() // Creating customer stream
                .collect(Collectors.groupingBy(Customer::getZipCode, // Grouping the stream of customer by zipCode
                        TreeMap::new, // Creating a new treeMap gather the results and have them sorted.
                        Collectors.summingDouble(Customer::calculateTotalBill))); // For all postcodes the totalBill will be added to the sum.
    }

    /**
     * finds the product(s) found in the most carts of customers
     * @return Set with products bought by most customers
     */
    public Set<Product> findMostPopularProducts() {
        Map<Product, Long> popularProduct = customers.stream() // Creates HashMap popularProcut. Will store the popularity in here. Keys = Product, Value = Long(Integer)
                .flatMap(customer -> customer.getItemsCart().keySet().stream()) // keySet.stream extract the key (so Product), flatMap combines the individual products to a single stream from all customers.
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting())); // Collecting all elements in the stream into the map. With function.identity each product is grouped by itself. With counting it counts the amount of times it's in the stream.

        long popularMax = popularProduct.values().stream() // Creating a stream for a Long(int) value
                .max(Long::compareTo) // Finding the max popularity in all the products in the stream
                .orElse(0L); // Returns 0 if no products again.

        return popularProduct.entrySet().stream() // Creates a stream of the popularProduct map (key value pair = Product & Long)
                .filter(entry -> entry.getValue() == popularMax) // Filtering all entries, will only keep the entries where popularity count equals popularMax.
                .map(Map.Entry::getKey) // Maps the product object from the filter
                .collect(Collectors.toSet()); // Collects the result of the most popular product in a set.
    }

    /**
     *
     * calculates a map of most bought products per zip code that is also ordered by zip code
     * if multiple products have the same maximum count, just pick one.
     * @return Map with most bought product per zip code
     */
    public Map<String, Product> findMostBoughtProductByZipcode() {
        Map<String, Product> mostBoughtByZipcode = new TreeMap<>(); // TreemMap to store postcodes as key.
        Map<String, Map<Product, Integer>> productsByZipcode = findNumberOfProductsByZipcode(); // Creating a HashMap with method findNumberOfProductsByZipCode. Keys will be postcodes(string), the value of the map is another map with Product& Integer.

        for (Map.Entry<String, Map<Product, Integer>> entry : productsByZipcode.entrySet()) { // For loop that goes over all the key value pairs in productsByZipCode.
            String postCode = entry.getKey(); // Gets the current postcode
            Map<Product, Integer> productCounter = entry.getValue(); // Gets the map of the product for the zipcode (the 2nd Map I created on line 292)

            int maxQuantity = productCounter.values().stream().max(Integer::compareTo).orElse(0); // Creating a stream of values from the productCounter map. max = find maximum quantitiy in the stream in Integer, orElse returns 0 if no one purchased anything.

            Product mostPurchased = productCounter.entrySet().stream() // Creating a stream of productCounter items.
                    .filter(product -> product.getValue() == maxQuantity) // Filter to only keep items where the quantity is equal to maxQuantity.
                    .map(Map.Entry::getKey) // Extracts a product from the filtered entry.
                    .findFirst() // Gets the first product that matches the filter.
                    .orElse(null); // Again returns null if nothing is purchased in a postcode.

            mostBoughtByZipcode.put(postCode, mostPurchased); // Storing the postCode(string) and mostPurchased (Product object)
        }
        return mostBoughtByZipcode;
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
