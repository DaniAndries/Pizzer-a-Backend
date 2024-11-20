package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConf {

    public static final String URL = "jdbc:mysql://localhost:3306/RuskaRoma";
    public static final String USER = "root";
    public static final String PASSWORD = "root";

    /**
     * id
     * dni;
     * clientName;
     * direction;
     * phone;
     * mail;
     * password;
     * List<Order> orderList;
     * admin=false;
     */
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

    public static final String CREATE_TABLE_PRODUCT = "CREATE TABLE IF NOT EXISTS product (" + "id INT PRIMARY KEY AUTO_INCREMENT," +
            "product_name VARCHAR(255) NOT NULL," + "price DOUBLE(255) NOT NULL," + "plate_date DATE NULL ," + "car_owner INT," +
            "FOREIGN KEY (car_owner) REFERENCES client (id) ON DELETE NO ACTION ON UPDATE CASCADE" + ");";

    public static final String DROP_TABLE_PRODUCT = "DROP TABLE IF EXISTS product";

    public static final String CREATE_TABLE_ORDER = "CREATE TABLE IF NOT EXISTS order (" + "plate VARCHAR(255) PRIMARY KEY," +
            "brand VARCHAR(255) NOT NULL," + "model VARCHAR(255) NOT NULL," + "plate_date DATE NULL ," + "car_owner INT," +
            "FOREIGN KEY (car_owner) REFERENCES client (id) ON DELETE NO ACTION ON UPDATE CASCADE" + ");";

    public static final String DROP_TABLE_ORDER = "DROP TABLE IF EXISTS order";


    public static void createTables() throws SQLException {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD); Statement stmt = conn.createStatement()) {
            stmt.execute(CREATE_TABLE_CLIENT);
            System.out.println("Client table create success");

        }
    }

    public static void dropAndCreateTables() throws SQLException {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD); Statement stmt = conn.createStatement()) {
/*            stmt.execute(CREATE_TABLE_PRODUCT);
            System.out.println("Product table delete success");*/
            stmt.execute(DROP_TABLE_CLIENT);
            System.out.println("Client table delete success");


            stmt.execute(CREATE_TABLE_CLIENT);
            System.out.println("Client table create success");
/*            stmt.execute(CREATE_TABLE_PRODUCT);
            System.out.println("Product table create success");*/
        }
    }

}
