package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConf {

    public static final String URL = "jdbc:mysql://localhost:3306/RuskaRoma";
    public static final String USER = "root";
    public static final String PASSWORD = "root";

    // id; dni; clientName; direction; phone; mail; password; List<Order> orderList; admin=false;
    public static final String CREATE_TABLE_CLIENT = "CREATE TABLE IF NOT EXISTS client ( " +
            "id INT PRIMARY KEY AUTO_INCREMENT," +
            "dni VARCHAR(255) NOT NULL UNIQUE," +
            "client_name VARCHAR(255) NOT NULL," +
            "direction VARCHAR(255) NOT NULL," +
            "phone VARCHAR(255) NOT NULL," +
            "mail VARCHAR(255) NOT NULL  UNIQUE," +
            "password VARCHAR(255) NOT NULL," +
            "admin BOOL default FALSE" + ")";

    public static final String DROP_TABLE_CLIENT = "DROP TABLE IF EXISTS client";

    // id; product_name; price; size; type;
    public static final String CREATE_TABLE_PRODUCT = "CREATE TABLE IF NOT EXISTS product ("
            + "id INT PRIMARY KEY AUTO_INCREMENT," +
            "product_name VARCHAR(255) NOT NULL," +
            "price DOUBLE NOT NULL," +
            "size ENUM('SMALL', 'MEDIUM', 'BIG') DEFAULT NULL ," +
            "type ENUM('PIZZA', 'DRINK', 'PASTA') NOT NULL " + ");";

    public static final String DROP_TABLE_PRODUCT = "DROP TABLE IF EXISTS product";

    // id; product; ingredient;
    public static final String CREATE_TABLE_PRODUCT_INGREDIENT = "CREATE TABLE IF NOT EXISTS PRODUCT_INGREDIENT ("
            + "id INT PRIMARY KEY AUTO_INCREMENT," +
            "product INT NOT NULL," +
            "ingredient INT NOT NULL," +
            "FOREIGN KEY (product) REFERENCES product (id) ON DELETE NO ACTION ON UPDATE CASCADE, " +
            "FOREIGN KEY (ingredient) REFERENCES ingredient (id) ON DELETE NO ACTION ON UPDATE CASCADE" + ");";

    public static final String DROP_TABLE_PRODUCT_INGREDIENT = "DROP TABLE IF EXISTS PRODUCT_INGREDIENT";

    // id; ingredient_name;
    public static final String CREATE_TABLE_INGREDIENT = "CREATE TABLE IF NOT EXISTS INGREDIENT ("
            + "id INT PRIMARY KEY AUTO_INCREMENT," +
            "ingredient_name VARCHAR(255) NOT NULL" + ");";

    public static final String DROP_TABLE_INGREDIENT = "DROP TABLE IF EXISTS INGREDIENT";

    // id; ingredient_name;
    public static final String CREATE_TABLE_INGREDIENT_ALERGEN = "CREATE TABLE IF NOT EXISTS INGREDIENT_ALERGEN ("
            + "id INT PRIMARY KEY AUTO_INCREMENT," +
            "ingredient INT NOT NULL," +
            "ALERGEN INT NOT NULL," +
            "FOREIGN KEY (ingredient) REFERENCES ingredient (id) ON DELETE NO ACTION ON UPDATE CASCADE, " +
            "FOREIGN KEY (ALERGEN) REFERENCES ALERGEN (id) ON DELETE NO ACTION ON UPDATE CASCADE" + ");";

    public static final String DROP_TABLE_INGREDIENT_ALERGEN = "DROP TABLE IF EXISTS INGREDIENT_ALERGEN";

    // id; alergen_name;
    public static final String CREATE_TABLE_ALERGEN = "CREATE TABLE IF NOT EXISTS ALERGEN ("
            + "id INT PRIMARY KEY AUTO_INCREMENT," +
            "alergen_name VARCHAR(255) NOT NULL" + ");";

    public static final String DROP_TABLE_ALERGEN = "DROP TABLE IF EXISTS ALERGEN";

    // id; state; orderDate; totalPrice; paymentMethod; client;
    public static final String CREATE_TABLE_ORDER = "CREATE TABLE IF NOT EXISTS customer_order (" +
            "id INT PRIMARY KEY AUTO_INCREMENT," +
            "state ENUM('PENDING', 'PAID', 'DELIVERED', 'CANCELED') NOT NULL DEFAULT 'PENDING'," +
            "order_date DATE NOT NULL," +
            "total_price DOUBLE NOT NULL ," +
            "payment_method ENUM('CARD', 'CASH') NULL DEFAULT NULL," +
            "client INT NOT NULL," +
            "FOREIGN KEY (client) REFERENCES client (id) ON DELETE NO ACTION ON UPDATE CASCADE" +
            ");";

    public static final String DROP_TABLE_ORDER = "DROP TABLE IF EXISTS customer_order";

    // id; amount; price; product;
    public static final String CREATE_TABLE_ORDER_LINE = "CREATE TABLE IF NOT EXISTS order_line (" +
            "id INT PRIMARY KEY AUTO_INCREMENT," +
            "amount INT NOT NULL," +
            "line_price DOUBLE NOT NULL ," +
            "product INT NOT NULL," +
            "customer_order INT NOT NULL," +
            "FOREIGN KEY (customer_order) REFERENCES customer_order (id) ON DELETE NO ACTION ON UPDATE CASCADE, " +
            "FOREIGN KEY (product) REFERENCES product (id) ON DELETE NO ACTION ON UPDATE CASCADE" + ");";


    public static final String DROP_TABLE_ORDER_LINE = "DROP TABLE IF EXISTS order_line";

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


    public static void dropAndCreateTables() throws SQLException {
        dropTables();
        createTables();
    }
}
