package com.adaptnxt.assignment;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ProductScraper {

    public static void main(String[] args) throws IOException {
        // URL of the page to scrape
        String url = "https://www.officedepot.com/a/browse/ergonomic-office-chairs/N=5+593065&cbxRefine=1429832/";

        // Get the HTML document from the URL
        Document doc = Jsoup.connect(url).get();

        // Get the product elements from the HTML document
        Elements productElements = doc.select(".product_listing");

        // Create a list to store the products
        List<Product> products = new ArrayList<>();

        // Loop through each product element and extract the product details
        for (Element productElement : productElements) {
            String name = productElement.select(".product_title").text();
            String price = productElement.select(".price_column").text();
            String itemNumber = productElement.select(".item_number").text();
            String modelNumber = productElement.select(".model_number").text();
            String category = productElement.select(".category").text();
            String description = productElement.select(".short_description").text();

            // Create a new Product object and add it to the list of products
            Product product = new Product(name, price, itemNumber, modelNumber, category, description);
            products.add(product);
        }

        // Sort the products by price in descending order
        Collections.sort(products);

        // Create a FileWriter object to write to the CSV file
        FileWriter writer = new FileWriter("top_10_products.csv");

        // Write the header row to the CSV file
        writer.write("Product Name,Product Price,Item Number,Model Number,Product Category,Product Description\n");

        // Write the top 10 products to the CSV file
        for (int i = 0; i < 10 && i < products.size(); i++) {
            Product product = products.get(i);
            String csvString = product.toCsvString();
            writer.write(csvString + "\n");
        }

        // Close the FileWriter object
        writer.close();

        // Print a message to indicate that the top 10 products have been exported
        System.out.println("Top 10 products exported to top_10_products.csv");
    }
}

class Product implements Comparable<Product> {
    String name;
    String price;
    String itemNumber;
    String modelNumber;
    String category;
    String description;

    public Product(String name, String price, String itemNumber, String modelNumber, String category, String description) {
        this.name = name;
        this.price = price;
        this.itemNumber = itemNumber;
        this.modelNumber = modelNumber;
        this.category = category;
        this.description = description;
    }

    @Override
    public int compareTo(Product other) {
        double thisPrice = Double.parseDouble(this.price.replaceAll("[^\\d.]", ""));
        double otherPrice = Double.parseDouble(other.price.replaceAll("[^\\d.]", ""));
        return Double.compare(otherPrice, thisPrice);
    }

    public String toCsvString() {
        return String.format("%s,%s,%s,%s,%s,%s", 
            this.name, 
            this.price, 
            this.itemNumber, 
            this.modelNumber, 
            this.category, 
            this.description
        );
    }
}
