import controller.CustomerController;
import model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import utils.DatabaseConf;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * CustomerControllerTest is a test class for testing the functionality
 * of the CustomerController class.
 * <p>
 * This class contains test methods to validate the behavior of
 * CustomerController, ensuring that it handles customer operations correctly.
 * </p>
 *
 * @author DaniAndries
 * @version 0.1
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JdbcCustomerControllerTest {

    private CustomerController customerController;

    /**
     * Default constructor for CustomerControllerTest.
     * <p>
     * This constructor initializes a new instance of the CustomerControllerTest class.
     * It can be used to create an instance of this class without
     * providing any specific parameters.
     * </p>
     */
    public JdbcCustomerControllerTest() {
        // Empty constructor
    }

    /**
     * Sets up the test environment by resetting the database and initializing the controller.
     *
     * @throws SQLException if a database error occurs.
     */
    @BeforeEach
    void setupDatabase() throws SQLException {
        DatabaseConf.dropAndCreateTables();
        customerController = new CustomerController();
    }

    /**
     * Tests registering a new customer and retrieving it by email.
     *
     * @throws SQLException if a database error occurs.
     */
    @Test
    void testRegisterCustomer() throws SQLException {
        Customer customer = new Customer("12345678A", "Juan Pérez", "Calle Falsa 123, Madrid", "600123456", "juan.perez@example.com", "password123", false);
        customerController.registerCustomer(customer);

        Customer foundCustomer = customerController.findByMail("juan.perez@example.com");
        assertNotNull(foundCustomer, "Customer should be found after registration.");
        assertEquals("Juan Pérez", foundCustomer.getCustomerName(), "Customer name should match the registered value.");
    }

    /**
     * Tests updating an existing customer's details.
     *
     * @throws SQLException if a database error occurs.
     */
    @Test
    void testUpdateCustomer() throws SQLException {
        Customer customer = new Customer("12345678B", "Ana Gómez", "Av. Siempre Viva 742, Barcelona", "650654321", "ana.gomez@example.com", "securepass456", false);
        customerController.registerCustomer(customer);

        customer.setCustomerName("Ana González");
        customerController.update(customer);

        Customer updatedCustomer = customerController.findByMail("ana.gomez@example.com");
        assertNotNull(updatedCustomer, "Customer should still exist after the update.");
        assertEquals("Ana González", updatedCustomer.getCustomerName(), "Customer name should match the updated value.");
    }

    /**
     * Tests deleting a registered customer.
     *
     * @throws SQLException if a database error occurs.
     */
    @Test
    void testDeleteCustomer() throws SQLException {
        Customer customer = new Customer("45612378C", "Luis Martínez", "Plaza Mayor 5, Valencia", "610987654", "luis.martinez@example.com", "mypassword789", true);
        customerController.registerCustomer(customer);

        customerController.delete(customer);

        Customer deletedCustomer = customerController.findByMail("luis.martinez@example.com");
        assertNull(deletedCustomer, "Customer should not exist after deletion.");
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
        assertNotNull(foundCustomer, "Customer should be found by ID.");
        assertEquals("Marta López", foundCustomer.getCustomerName(), "Customer name should match the registered value.");
    }

    /**
     * Tests retrieving all registered customers.
     *
     * @throws SQLException if a database error occurs.
     */
    void testFindAllCustomers() throws SQLException {
        Customer customer1 = new Customer("32198765E", "Carlos García", "Paseo del Prado 15, Bilbao", "630445566", "carlos.garcia@example.com", "garcia456", true);
        Customer customer2 = new Customer("65432198F", "Laura Fernández", "Gran Vía 18, Zaragoza", "640987123", "laura.fernandez@example.com", "laura2023", false);

        customerController.registerCustomer(customer1);
        customerController.registerCustomer(customer2);

        List<Customer> customers = customerController.findAll();
        assertEquals(2, customers.size());
    }

    @Test
    void shouldRegisterCustomerSuccessfully() throws SQLException {
        Customer customer = new Customer("12345678A", "Juan Pérez", "Calle Falsa 123, Madrid", "600123456", "juan.perez@example.com", "password123", false);

        customerController.registerCustomer(customer);

        Customer foundCustomer = customerController.findByMail("juan.perez@example.com");
        assertNotNull(foundCustomer, "Expected customer to be found after registration");
        assertEquals("Juan Pérez", foundCustomer.getCustomerName(), "Customer name should match after registration");
    }
}
