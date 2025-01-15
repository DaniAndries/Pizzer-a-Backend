import controller.CustomerController;
import model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JpaCustomerController {
    private CustomerController customerController;

    /**
     * Default constructor for customerControllerTest.
     * <p>
     * This constructor initializes a new instance of the customerControllerTest class.
     * It can be used to create an instance of this class without
     * providing any specific parameters.
     * </p>
     */
    public JpaCustomerController() {
        // Empty constructor
    }

    /**
     * Sets up the test environment by resetting the database and initializing the controller.
     *
     * @throws SQLException if a database error occurs.
     */
    @BeforeEach
    void setupDatabase() throws SQLException {
        customerController = new CustomerController();
    }

    /**
     * Tests registering a new customer and retrieving it by email.
     *
     * @throws SQLException if a database error occurs.
     */
    @Test
    void testRegistercustomer() throws SQLException {
        Customer customer = new Customer("12345678A", "Juan Pérez", "Calle Falsa 123, Madrid", "600123456", "juan.perez@example.com", "password123", false);
        customerController.registerCustomer(customer);

        Customer foundCustomer = customerController.findById(1);
        assertNotNull(foundCustomer, "customer should be found after registration.");
        assertEquals("Juan Pérez", foundCustomer.getCustomerName(), "customer name should match the registered value.");
    }

    /**
     * Tests updating an existing customer's details.
     *
     * @throws SQLException if a database error occurs.
     */
    @Test
    void testUpdatecustomer() throws SQLException {
        Customer customer = new Customer("12345678B", "Ana Gómez", "Av. Siempre Viva 742, Barcelona", "650654321", "ana.gomez@example.com", "securepass456", false);
        customerController.registerCustomer(customer);

        customer.setCustomerName("Ana González");
        customerController.update(customer);

        Customer updatedCustomer = customerController.findById(1);
        assertNotNull(updatedCustomer, "customer should still exist after the update.");
        assertEquals("Ana González", updatedCustomer.getCustomerName(), "customer name should match the updated value.");
    }

    /**
     * Tests deleting a registered customer.
     *
     * @throws SQLException if a database error occurs.
     */
    @Test
    void testDeletecustomer() throws SQLException {
        Customer customer = new Customer("45612378C", "Luis Martínez", "Plaza Mayor 5, Valencia", "610987654", "luis.martinez@example.com", "mypassword789", true);
        customerController.registerCustomer(customer);

        customerController.delete(customer);

        Customer deletedCustomer = customerController.findById(1);
        assertNull(deletedCustomer, "customer should not exist after deletion.");
    }

    /**
     * Tests finding a customer by their unique ID.
     *
     * @throws SQLException if a database error occurs.
     */
    @Test
    void testFindById() throws SQLException {
        Customer customer = new Customer("78965412D", "Marta López", "Calle Luna 8, Sevilla", "620112233", "marta.lopez@example.com", "martal123", false);
        customerController.registerCustomer(customer);

        Customer foundCustomer = customerController.findById(1);
        assertNotNull(foundCustomer, "customer should be found by ID.");
        assertEquals("Marta López", foundCustomer.getCustomerName(), "customer name should match the registered value.");
    }

    /**
     * Tests finding a customer by their mail.
     *
     * @throws SQLException if a database error occurs.
     */
    @Test
    void testFindByMail() throws SQLException {
        Customer customer = new Customer("78965412D", "Marta López", "Calle Luna 8, Sevilla", "620112233", "marta.lopez@example.com", "martal123", false);
        customerController.registerCustomer(customer);

        Customer foundCustomer = customerController.findByMail("marta.lopez@example.com");
        assertNotNull(foundCustomer, "customer should be found by ID.");
        System.out.println(foundCustomer);
        assertEquals("marta.lopez@example.com", foundCustomer.getMail(), "customer mail should match the registered value.");
    }

    /**
     * Tests retrieving all registered customers.
     *
     * @throws SQLException if a database error occurs.
     */
    @Test
    void testFindAllcustomers() throws SQLException {
        Customer customer1 = new Customer("32198765E", "Carlos García", "Paseo del Prado 15, Bilbao", "630445566", "carlos.garcia@example.com", "garcia456", true);
        Customer customer2 = new Customer("65432198F", "Laura Fernández", "Gran Vía 18, Zaragoza", "640987123", "laura.fernandez@example.com", "laura2023", false);

        customerController.registerCustomer(customer1);
        customerController.registerCustomer(customer2);

        List<Customer> customers = customerController.findAll();
        assertEquals(2, customers.size());
    }

    @Test
    void shouldRegistercustomerSuccessfully() throws SQLException {
        Customer customer = new Customer("12345678A", "Juan Pérez", "Calle Falsa 123, Madrid", "600123456", "juan.perez@example.com", "password123", false);

        customerController.registerCustomer(customer);

        Customer foundCustomer = customerController.findByMail("juan.perez@example.com");
        assertNotNull(foundCustomer, "Expected customer to be found after registration");
        assertEquals("Juan Pérez", foundCustomer.getCustomerName(), "customer name should match after registration");
    }
}
