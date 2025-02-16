package main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The Main class serves as the entry point for the Pizzeria application.
 * <p>
 * This class contains the main method which initializes the application by
 * dropping and recreating the necessary database tables.
 * </p>
 *
 * @author DaniAndries
 * @version 0.1
 */
@SpringBootApplication
public class MainApplication {
    /**
     * The main method that executes the application.
     * <p>
     * This method establishes a connection to the database and
     * ensures that all required tables are present by dropping
     * and recreating them.
     * </p>
     *
     * @param args Command-line arguments passed to the application.
     */

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }
}
