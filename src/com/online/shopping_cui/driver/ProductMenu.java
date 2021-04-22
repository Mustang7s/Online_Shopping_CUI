/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.online.shopping_cui.driver;

import com.online.shopping_cui.enumerations.Category;
import com.online.shopping_cui.model.ProductList;
import com.online.shopping_cui.model.Product;
import com.online.shopping_cui.utilities.ProductFileIO;
import java.util.*;

/**
 * Product Menu Class - This class contains methods and attributes that handle the
 * product menu.
 *
 * @author Miguel Emmara - 18022146
 * @author Amos Foong - 18044418
 * @author Roxy Dao - 1073633
 * @version 1.01
 * @since 18/04/2021
 *
 */
public class ProductMenu {

    static String ERROR = "Please choose from the options below";
    static String BACK = "Select (0 to go back): ";
    static String VALIDNO = "Please enter a number";

    protected String[] categories = {"\t1. PC Parts\n", "\t2. Laptop\n", "\t3. Camera\n", "\t4. Printer\n", "\t5. Smartphone\n", "\t6. Misc"};

    protected static Scanner scanner;
    protected ProductList products;

    public ProductMenu() {
        this.scanner = new Scanner(System.in);
        this.products = ProductFileIO.importProductData();
    }

    public void displayProducts() {
        System.out.print(this.products.toString());
    }

    /**
     * Level 6a menu.
     */
    public void addProduct() {
        String productName;
        int productID = 0;
        double price = 0D;
        Category category = null;
        int productCategorySelection;
        Integer stock = 0;

        System.out.println("\nProduct Details (\"b\" to go back)");
        do {
            System.out.print("\tProduct Name: ");
            productName = scanner.nextLine();

            if (productName.isEmpty()) {
                System.err.println("Error: Name can't be empty.");
            } else if (productName.equalsIgnoreCase("b")) { // If user wishes to go back...
                return; // Exit method.
            }
        } while (productName.isEmpty());

        while (true) {
            try {
                System.out.print("\tProduct ID: ");
                productID = scanner.nextInt();
                scanner.nextLine();

                for (Product product : this.products.getProductList()) { // Traverse through the list of products.
                    if (productID == product.getProductID()) { // If product ID is already existent...
                        throw new IllegalArgumentException("Existing Product ID Detected!");
                    }
                }
                break;
            } catch (InputMismatchException e) {
                System.err.println("Please enter a valid Product ID");
                scanner.nextLine();
            } catch (IllegalArgumentException e) {
                System.err.println(e.getMessage());
            }
        }

        while (true) {
            try {
                System.out.print("\tPrice: $");
                price = scanner.nextDouble();
                scanner.nextLine();
                break;
            } catch (InputMismatchException e) {
                System.err.println(VALIDNO);
                scanner.nextLine();
            }
        }

        while (true) {
            try {
                System.out.println("\tProduct Category: ");
                for (String cat : categories) {
                    System.out.print("\t" + cat); // Print out the categories.
                }
                System.out.print("\n\tSelect: ");
                productCategorySelection = scanner.nextInt();
                scanner.nextLine();

                category = Category.values()[productCategorySelection - 1];

                if (category == null) {
                    throw new IndexOutOfBoundsException(ERROR);
                } else {
                    break;
                }
            } catch (IndexOutOfBoundsException e) {
                System.err.println(ERROR);
            } catch (InputMismatchException e) {
                System.err.println(VALIDNO);
                scanner.nextLine();
            }
        }

        while (true) {
            try {
                System.out.print("\tInitial Stock: ");
                stock = scanner.nextInt();
                scanner.nextLine();
                break;
            } catch (InputMismatchException e) {
                System.err.println(VALIDNO);
                scanner.nextLine();
            }
        }

        // Adds the product to the instantiated ProductList object.
        this.products.addSingleProduct(new Product(productName, productID, price, category, stock));
        System.out.println("Product added!");
    }

    /**
     * Level 6b menu.
     */
    public void removeProduct() {
        int pIndRmv = 0;
        boolean success = false;
        Product pToRmv = null;

        do {
            System.out.print(this.products.toString());
            System.out.print(BACK);
            try {
                pIndRmv = (scanner.nextInt() - 1); // Gets the user-selection and modify it to enable indexed access.
                scanner.nextLine();

                if (pIndRmv == -1) { // If user wishes to go back (0 pressed)...
                    return; // Exit this method.
                }

                pToRmv = this.products.getProductList().get(pIndRmv); // Gets the specific Product object that is to be removed.
                this.products.removeProduct(pToRmv.getCategory(), pToRmv); // Removes the product from the ProductList Object.

                if (pIndRmv < 0 || pIndRmv > this.products.getProductList().size()) { // If user's selection is out of bounds...
                    throw new IndexOutOfBoundsException(ERROR);
                } else { // Otherwise
                    success = true;
                    System.out.println("Product removed!");
                }
            } catch (IndexOutOfBoundsException e) {
                System.err.println(e.getMessage());
            } catch (InputMismatchException e) {
                System.err.println(ERROR);
                scanner.nextLine();
            }
        } while (!success);
    }

    /**
     * Level 6c menu.
     */
    public void editProduct() {
        ArrayList<Product> pList = this.products.getProductList();

        int pIndEdit = -10;
        boolean editSuccess = false;
        Product pToEdit = null;
        do {
            System.out.print(this.products.toString());
            System.out.print(BACK);
            try {
                pIndEdit = (scanner.nextInt() - 1); // Gets the user-selection and modify it to enable indexed access.
                scanner.nextLine();

                if (pIndEdit == -1) { // If user wishes to go back (0 pressed)...
                    return; // Exit this method.
                } else if (pIndEdit < 0 || pIndEdit > this.products.getProductList().size()) { // If user's selection is out of bounds...
                    throw new IndexOutOfBoundsException(ERROR);
                } else {
                    pToEdit = pList.get(pIndEdit); // Saves the reference of the product to be edited.
                }

                boolean edit2Success = false;
                do {
                    System.out.println("\nWhich would you like to edit? (0 to exit)");
                    System.out.println("\n\t1. Product Name");
                    System.out.println("\t2. Product ID");
                    System.out.println("\t3. Price");
                    System.out.println("\t4. Category");
                    System.out.println("\t5. Stock");
                    System.out.print("> ");
                    int editType = scanner.nextInt();
                    scanner.nextLine();

                    switch (editType) {
                        case 0:
                            edit2Success = true;
                            break;
                        case 1:
                            System.out.print("New Name: ");
                            pToEdit.setProductName(scanner.nextLine()); // Modifies the name.
                            break;
                        case 2:
                            System.out.print("New Product ID: ");
                            pToEdit.setProductID(scanner.nextInt()); // Modifies the Product ID.
                            scanner.nextLine();
                            break;
                        case 3:
                            System.out.print("New Price: $");
                            pToEdit.setPrice(scanner.nextDouble()); // Modifies the price.
                            scanner.nextLine();
                            break;
                        case 4:
                            System.out.println("Categories: ");
                            for (String cat : categories) {
                                System.out.print(cat);
                            }
                            System.out.print("> ");
                            pToEdit.setCategory(Category.values()[scanner.nextInt() - 1]); // Modifies the Category.
                            scanner.nextLine();
                            break;
                        case 5:
                            System.out.print("Stock: ");
                            pToEdit.setStock(new Integer(scanner.nextInt())); // Modifies the stock.
                            scanner.nextLine();
                            break;
                        default:
                            throw new IndexOutOfBoundsException(ERROR);
                    }
                } while (!edit2Success);
            } catch (IndexOutOfBoundsException e) {
                System.err.println(e.getMessage());
            } catch (InputMismatchException e) {
                System.err.println(ERROR);
                scanner.nextLine();
            }
        } while (!editSuccess);

        System.out.println("Product edited!");
    }
}