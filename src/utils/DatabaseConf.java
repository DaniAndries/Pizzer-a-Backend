package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Utility class for managing the database configuration and schema for the application.
 * It includes methods for creating, dropping, and resetting tables.
 */
public class DatabaseConf {
    /**
     * The URL of the database to connect to.
     */
    public static final String URL = "jdbc:mysql://localhost:3306/RuskaRoma";
    /**
     * The username for accessing the database.
     */
    public static final String USER = "root";
    /**
     * The password for accessing the database.
     */
    public static final String PASSWORD = "root";

    // SQL statements for creating and dropping tables

    // id; dni; clientName; direction; phone; mail; password; List<Order> orderList; admin=false;
    /**
     * SQL query to create the `client` table if it does not exist.
     */
    public static final String CREATE_TABLE_CLIENT = "CREATE TABLE IF NOT EXISTS client ( " + "id INT PRIMARY KEY AUTO_INCREMENT," + "dni VARCHAR(255) NOT NULL UNIQUE," + "client_name VARCHAR(255) NOT NULL," + "direction VARCHAR(255) NOT NULL," + "phone VARCHAR(255) NOT NULL," + "mail VARCHAR(255) NOT NULL  UNIQUE," + "password VARCHAR(255) NOT NULL," + "admin BOOL default FALSE" + ")";

    /**
     * SQL query to drop the `client` table if it exists.
     */
    public static final String DROP_TABLE_CLIENT = "DROP TABLE IF EXISTS client";

    // id; product_name; price; size; type;
    /**
     * SQL query to create the `product` table if it does not exist.
     */
    public static final String CREATE_TABLE_PRODUCT = "CREATE TABLE IF NOT EXISTS product (" + "id INT PRIMARY KEY AUTO_INCREMENT," + "product_name VARCHAR(255) NOT NULL," + "price DOUBLE NOT NULL," + "size ENUM('SMALL', 'MEDIUM', 'BIG') DEFAULT NULL ," + "type ENUM('PIZZA', 'DRINK', 'PASTA') NOT NULL " + ");";

    /**
     * SQL query to drop the `product` table if it exists.
     */
    public static final String DROP_TABLE_PRODUCT = "DROP TABLE IF EXISTS product";

    // id; product; ingredient;
    /**
     * SQL query to create the `product_ingredient` table if it does not exist.
     */
    public static final String CREATE_TABLE_PRODUCT_INGREDIENT = "CREATE TABLE IF NOT EXISTS PRODUCT_INGREDIENT (" + "id INT PRIMARY KEY AUTO_INCREMENT," + "product INT NOT NULL," + "ingredient INT NOT NULL," + "FOREIGN KEY (product) REFERENCES product (id) ON DELETE CASCADE ON UPDATE CASCADE, " + "FOREIGN KEY (ingredient) REFERENCES ingredient (id) ON DELETE CASCADE ON UPDATE CASCADE" + ");";

    /**
     * SQL query to drop the `product_ingredient` table if it exists.
     */
    public static final String DROP_TABLE_PRODUCT_INGREDIENT = "DROP TABLE IF EXISTS PRODUCT_INGREDIENT";

    // id; ingredient_name;
    /**
     * SQL query to create the `ingredient` table if it does not exist.
     */
    public static final String CREATE_TABLE_INGREDIENT = "CREATE TABLE IF NOT EXISTS INGREDIENT (" + "id INT PRIMARY KEY AUTO_INCREMENT," + "ingredient_name VARCHAR(255) UNIQUE NOT NULL" + ");";

    /**
     * SQL query to drop the `ingredient` table if it exists.
     */
    public static final String DROP_TABLE_INGREDIENT = "DROP TABLE IF EXISTS INGREDIENT";

    // id; ingredient_name;
    /**
     * SQL query to create the `ingredient_allergen` table if it does not exist.
     */
    public static final String CREATE_TABLE_INGREDIENT_ALERGEN = "CREATE TABLE IF NOT EXISTS INGREDIENT_ALERGEN (" + "id INT PRIMARY KEY AUTO_INCREMENT," + "ingredient INT NOT NULL," + "alergen INT NOT NULL," + "FOREIGN KEY (ingredient) REFERENCES ingredient (id) ON DELETE CASCADE ON UPDATE CASCADE, " + "FOREIGN KEY (alergen) REFERENCES alergen (id) ON DELETE CASCADE ON UPDATE CASCADE" + ");";

    /**
     * SQL query to drop the `ingredient_allergen` table if it exists.
     */
    public static final String DROP_TABLE_INGREDIENT_ALERGEN = "DROP TABLE IF EXISTS INGREDIENT_ALERGEN";

    // id; alergen_name;
    /**
     * SQL query to create the `allergen` table if it does not exist.
     */
    public static final String CREATE_TABLE_ALERGEN = "CREATE TABLE IF NOT EXISTS ALERGEN (" + "id INT PRIMARY KEY AUTO_INCREMENT," + "alergen_name VARCHAR(255) UNIQUE NOT NULL" + ");";

    /**
     * SQL query to drop the `allergen` table if it exists.
     */
    public static final String DROP_TABLE_ALERGEN = "DROP TABLE IF EXISTS ALERGEN";

    // id; state; orderDate; paymentMethod; client;
    /**
     * SQL query to create the `customer_order` table if it does not exist.
     */
    public static final String CREATE_TABLE_ORDER = "CREATE TABLE IF NOT EXISTS customer_order (" + "id INT PRIMARY KEY AUTO_INCREMENT," + "state ENUM('PENDING', 'FINISHED', 'DELIVERED', 'CANCELED') NOT NULL DEFAULT 'PENDING'," + "order_date DATE NOT NULL," + "payment_method ENUM('CARD', 'CASH', 'UNPAID') NOT NULL DEFAULT 'UNPAID'," + "client INT NOT NULL," + "FOREIGN KEY (client) REFERENCES client (id) ON DELETE CASCADE ON UPDATE CASCADE" + ");";

    /**
     * SQL query to drop the `customer_order` table if it exists.
     */
    public static final String DROP_TABLE_ORDER = "DROP TABLE IF EXISTS customer_order";

    // id; amount; price; product;
    /**
     * SQL query to create the `order_line` table if it does not exist.
     */
    public static final String CREATE_TABLE_ORDER_LINE = "CREATE TABLE IF NOT EXISTS order_line (" + "id INT PRIMARY KEY AUTO_INCREMENT," + "amount INT NOT NULL," + "product INT NOT NULL," + "customer_order INT NOT NULL," + "FOREIGN KEY (customer_order) REFERENCES customer_order (id) ON DELETE CASCADE ON UPDATE CASCADE, " + "FOREIGN KEY (product) REFERENCES product (id) ON DELETE CASCADE ON UPDATE CASCADE" + ");";

    /**
     * Default constructor for DatabaseConf.
     * <p>
     * This constructor initializes a new instance of DatabaseConf.
     * It may be used to create an instance of the class without
     * any specific configuration parameters.
     * </p>
     */
    public DatabaseConf() {
        // Empty constructor
    }

    /**
     * SQL query to drop the `order_line` table if it exists.
     */
    public static final String DROP_TABLE_ORDER_LINE = "DROP TABLE IF EXISTS order_line";

    /**
     * Creates all necessary database tables for the application.
     * <p>
     * Executes SQL statements to ensure the schema is ready.
     * </p>
     *
     * @throws SQLException if a database access error occurs
     */
    public static void createTables() throws SQLException {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD); Statement stmt = conn.createStatement()) {
            stmt.execute(CREATE_TABLE_CLIENT);
            System.out.println("Client table create success");
            stmt.execute(CREATE_TABLE_PRODUCT);
            System.out.println("Product table create success");
            stmt.execute(CREATE_TABLE_ORDER);
            System.out.println("Order table create success");
            stmt.execute(CREATE_TABLE_ORDER_LINE);
            System.out.println("Order_Line table create success");
            stmt.execute(CREATE_TABLE_INGREDIENT);
            System.out.println("Ingredient table create success");
            stmt.execute(CREATE_TABLE_ALERGEN);
            System.out.println("Alergen Line table create success");
            stmt.execute(CREATE_TABLE_INGREDIENT_ALERGEN);
            System.out.println("Ingredient_Alergen Line table create success");
            stmt.execute(CREATE_TABLE_PRODUCT_INGREDIENT);
            System.out.println("Product_Ingredient Line table create success");
        }
    }

    /**
     * Drops all tables in the database.
     * <p>
     * Ensures a clean state by removing all existing tables.
     * </p>
     *
     * @throws SQLException if a database access error occurs
     */
    public static void dropTables() throws SQLException {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD); Statement stmt = conn.createStatement()) {
            stmt.execute(DROP_TABLE_ORDER_LINE);
            System.out.println("Order Line table delete success");
            stmt.execute(DROP_TABLE_ORDER);
            System.out.println("Order table delete success");
            stmt.execute(DROP_TABLE_PRODUCT_INGREDIENT);
            System.out.println("Product_Ingredient Line table delete success");
            stmt.execute(DROP_TABLE_INGREDIENT_ALERGEN);
            System.out.println("Ingredient_Alergen Line table delete success");
            stmt.execute(DROP_TABLE_INGREDIENT);
            System.out.println("Ingredient table delete success");
            stmt.execute(DROP_TABLE_ALERGEN);
            System.out.println("Alergen Line table delete success");
            stmt.execute(DROP_TABLE_PRODUCT);
            System.out.println("Product table delete success");
            stmt.execute(DROP_TABLE_CLIENT);
            System.out.println("Client table delete success");
        }
    }

    /**
     * Drops all tables and then recreates them.
     * <p>
     * Useful for resetting the database schema to a clean state.
     * </p>
     *
     * @throws SQLException if a database access error occurs
     */
    public static void dropAndCreateTables() throws SQLException {
        dropTables();
        createTables();
    }
}
