package controller.db;

import model.Client;

import java.sql.SQLException;
import java.util.List;

/**
 * The ClientDao interface defines methods for interacting with the database
 * related to client entities. It provides an abstraction layer for CRUD operations
 * and facilitates data persistence and retrieval.
 *
 * @author DaniAndries
 * @version 0.1
 */
public interface ClientDao {

    /**
     * Saves a new client to the database.
     *
     * @param client The Client object to be saved.
     * @throws SQLException If there is an error accessing the database.
     */
    void save(Client client) throws SQLException;

    /**
     * Deletes an existing client from the database.
     *
     * @param client The Client object to be deleted.
     * @throws SQLException If there is an error accessing the database.
     */
    void delete(Client client) throws SQLException;

    /**
     * Updates the details of an existing client in the database.
     *
     * @param client The Client object containing updated information.
     * @throws SQLException If there is an error accessing the database.
     */
    void update(Client client) throws SQLException;

    /**
     * Finds a client by their unique identifier (ID).
     *
     * @param id The ID of the client to be retrieved.
     * @return The Client object associated with the given ID, or null if not found.
     * @throws SQLException If there is an error accessing the database.
     */
    Client findById(int id) throws SQLException;

    /**
     * Finds a client by their email (DNI).
     *
     * @param dni The email (DNI) of the client to be retrieved.
     * @return The Client object associated with the given email, or null if not found.
     * @throws SQLException If there is an error accessing the database.
     */
    Client findByMail(String dni) throws SQLException;

    /**
     * Retrieves a list of all clients from the database.
     *
     * @return A list of Client objects.
     * @throws SQLException If there is an error accessing the database.
     */
    List<Client> findAll() throws SQLException;
}
