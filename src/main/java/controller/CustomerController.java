package controller;

import controller.db.impl.JpaCustomerDao;
import model.Customer;
import model.Product;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * The customerController class manages operations related to customer objects,
 * including user authentication, registration, and database interactions.
 *
 * @author DaniAndries
 * @version 0.1
 */
public class CustomerController {
    private JpaCustomerDao customerDao; // Data Access Object for customer
    private Customer actualCustomer; // Currently logged-in customer
    private List<Customer> customers; // List of all customers

    /**
     * Constructor for customerController that attempts to log in a customer with provided credentials.
     *
     * @param email    The email of the customer attempting to log in.
     * @param password The password of the customer attempting to log in.
     * @throws SQLException If there is a database access error.
     */
    public CustomerController(String email, String password) throws SQLException {
        this.customerDao = new JpaCustomerDao();
        this.customers = new ArrayList<>();
        loginCustomer(email, password);
    }

    /**
     * Default constructor for customerController.
     */
    public CustomerController() {
        this.customerDao = new JpaCustomerDao();
        this.customers = new ArrayList<>();
    }

    /**
     * Logs in a customer using their email and password.
     *
     * @param mail     The email of the customer.
     * @param password The password of the customer.
     * @return The authenticated customer object.
     * @throws SQLException If there is a database access error.
     * @throws IllegalArgumentException If the email or password is incorrect.
     */
    public Customer loginCustomer(String mail, String password) throws SQLException, IllegalArgumentException {
        Customer customer = findByMail(mail);
        if (customer != null && customer.getPassword().equals(password)) {
            actualCustomer = customer; // Set the logged-in customer
            return customer;
        } else {
            throw new IllegalArgumentException("Wrong User or Password");
        }
    }

    /**
     * Registers a new customer in the system.
     *
     * @param customer The customer object to be registered.
     * @throws SQLException If there is a database access error.
     */
    public void registerCustomer(Customer customer) throws SQLException {
        customerDao.save(customer);
    }

    /**
     * Deletes a customer from the system.
     *
     * @param customer The customer object to be deleted.
     * @throws SQLException If there is a database access error.
     */
    public void delete(Customer customer) throws SQLException {
        customerDao.delete(customer);
    }

    /**
     * Updates the information of an existing customer.
     *
     * @param customer The customer object with updated information.
     * @throws SQLException If there is a database access error.
     */
    public void update(Customer customer) throws SQLException {
        customerDao.update(customer);
    }

    /**
     * Finds a customer by their ID.
     *
     * @param id The ID of the customer to find.
     * @return The found customer object, or null if not found.
     * @throws SQLException If there is a database access error.
     */
    public Customer findById(int id) throws SQLException {
        return customerDao.findById(id);
    }

    /**
     * Finds a customer by their email address.
     *
     * @param mail The email address of the customer to find.
     * @return The found customer object, or null if not found.
     * @throws SQLException If there is a database access error.
     */
    public Customer findByMail(String mail) throws SQLException {
        return customerDao.findByMail(mail);
    }

    /**
     * Retrieves all customers from the database.
     *
     * @return A list of all customer objects.
     * @throws SQLException If there is a database access error.
     */
    public List<Customer> findAll() throws SQLException {
        return customerDao.findAll();
    }

    /**
     * Adds a product to the current customer's order.
     *
     * @param product The Product to be added to the order.
     * @return An OrderController object for managing the order.
     * @throws IllegalStateException If no customer is logged in.
     */
    public OrderController addOrderLine(Product product) throws IllegalStateException {
        if (this.actualCustomer != null) {
            return new OrderController(); // Returns a new OrderController for further order management
        } else {
            throw new IllegalStateException("No customer is currently logged in.");
        }
    }

    /**
     * Imports customers from an external file and adds them to the customers list.
     *
     * @throws IOException If there is an error reading the file.
     */
    public void importAdminCustomer() throws IOException {
        List<Customer> newList = FileManagement.importCustomer();
        customers.addAll(newList); // Add the newly imported customers to the list
    }

    /**
     * Exports the current list of customers to an XML file using JAXB.
     *
     * @throws JAXBException If there is an error during XML processing.
     */
    public void jaxbExport() throws JAXBException {
        FileManagement.customerToXml(this.customers); // Export current customers to XML
    }

    /**
     * Imports customers from an XML file using JAXB.
     *
     * @throws IOException If there is an error reading the file.
     * @throws JAXBException If there is an error during XML processing.
     */
    public void jaxbImport() throws IOException, JAXBException {
        this.customers = FileManagement.xmlToCustomer(); // Import customers from XML
    }
}
