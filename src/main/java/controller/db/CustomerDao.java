package controller.db;

import model.Customer;

import java.sql.SQLException;
import java.util.List;

/**
 * The CustomerDao interface defines methods for interacting with the database
 * related to Customer entities. It provides an abstraction layer for CRUD operations
 * and facilitates data persistence and retrieval.
 *
 * @author DaniAndries
 * @version 0.1
 */
public interface CustomerDao {

    /**
     * Saves a new Customer to the database.
     *
     * @param customer The Customer object to be saved.
     * @throws SQLException If there is an error accessing the database.
     */
    void save(Customer customer) throws SQLException;

    /**
     * Deletes an existing Customer from the database.
     *
     * @param customer The Customer object to be deleted.
     * @throws SQLException If there is an error accessing the database.
     */
    void delete(Customer customer) throws SQLException;

    /**
     * Updates the details of an existing Customer in the database.
     *
     * @param customer The Customer object containing updated information.
     * @throws SQLException If there is an error accessing the database.
     */
    void update(Customer customer) throws SQLException;

    /**
     * Finds a Customer by their unique identifier (ID).
     *
     * @param id The ID of the Customer to be retrieved.
     * @return The Customer object associated with the given ID, or null if not found.
     * @throws SQLException If there is an error accessing the database.
     */
    Customer findById(int id) throws SQLException;

    /**
     * Finds a Customer by their email (DNI).
     *
     * @param dni The email (DNI) of the Customer to be retrieved.
     * @return The Customer object associated with the given email, or null if not found.
     * @throws SQLException If there is an error accessing the database.
     */
    Customer findByMail(String dni) throws SQLException;

    /**
     * Retrieves a list of all Customers from the database.
     *
     * @return A list of Customer objects.
     * @throws SQLException If there is an error accessing the database.
     */
    List<Customer> findAll() throws SQLException;
}
