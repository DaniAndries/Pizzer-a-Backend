package controller;

import controller.db.impl.JdbcClientDao;
import model.Client;
import model.Product;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * The ClientController class manages operations related to Client objects,
 * including user authentication, registration, and database interactions.
 *
 * @author DaniAndries
 * @version 0.1
 */
public class ClientController {
    private JdbcClientDao clientDao; // Data Access Object for Client
    private Client actualClient; // Currently logged-in client
    private List<Client> clients; // List of all clients

    /**
     * Constructor for ClientController that attempts to log in a client with provided credentials.
     *
     * @param email    The email of the client attempting to log in.
     * @param password The password of the client attempting to log in.
     * @throws SQLException If there is a database access error.
     */
    public ClientController(String email, String password) throws SQLException {
        this.clientDao = new JdbcClientDao();
        this.clients = new ArrayList<>();
        loginClient(email, password);
    }

    /**
     * Default constructor for ClientController.
     */
    public ClientController() {
        this.clientDao = new JdbcClientDao();
        this.clients = new ArrayList<>();
    }

    /**
     * Logs in a client using their email and password.
     *
     * @param mail     The email of the client.
     * @param password The password of the client.
     * @return The authenticated Client object.
     * @throws SQLException If there is a database access error.
     * @throws IllegalArgumentException If the email or password is incorrect.
     */
    public Client loginClient(String mail, String password) throws SQLException, IllegalArgumentException {
        Client client = findByMail(mail);
        if (client != null && client.getPassword().equals(password)) {
            actualClient = client; // Set the logged-in client
            return client;
        } else {
            throw new IllegalArgumentException("Wrong User or Password");
        }
    }

    /**
     * Registers a new client in the system.
     *
     * @param client The Client object to be registered.
     * @throws SQLException If there is a database access error.
     */
    public void registerClient(Client client) throws SQLException {
        clientDao.save(client);
    }

    /**
     * Deletes a client from the system.
     *
     * @param client The Client object to be deleted.
     * @throws SQLException If there is a database access error.
     */
    public void delete(Client client) throws SQLException {
        clientDao.delete(client);
    }

    /**
     * Updates the information of an existing client.
     *
     * @param client The Client object with updated information.
     * @throws SQLException If there is a database access error.
     */
    public void update(Client client) throws SQLException {
        clientDao.update(client);
    }

    /**
     * Finds a client by their ID.
     *
     * @param id The ID of the client to find.
     * @return The found Client object, or null if not found.
     * @throws SQLException If there is a database access error.
     */
    public Client findById(int id) throws SQLException {
        return clientDao.findById(id);
    }

    /**
     * Finds a client by their email address.
     *
     * @param mail The email address of the client to find.
     * @return The found Client object, or null if not found.
     * @throws SQLException If there is a database access error.
     */
    public Client findByMail(String mail) throws SQLException {
        return clientDao.findByMail(mail);
    }

    /**
     * Retrieves all clients from the database.
     *
     * @return A list of all Client objects.
     * @throws SQLException If there is a database access error.
     */
    public List<Client> findAll() throws SQLException {
        return clientDao.findAll();
    }

    /**
     * Adds a product to the current client's order.
     *
     * @param product The Product to be added to the order.
     * @return An OrderController object for managing the order.
     * @throws IllegalStateException If no client is logged in.
     */
    public OrderController addOrderLine(Product product) throws IllegalStateException {
        if (this.actualClient != null) {
            return new OrderController(); // Returns a new OrderController for further order management
        } else {
            throw new IllegalStateException("No client is currently logged in.");
        }
    }

    /**
     * Imports clients from an external file and adds them to the clients list.
     *
     * @throws IOException If there is an error reading the file.
     */
    public void importAdminClient() throws IOException {
        List<Client> newList = FileManagement.importClient();
        clients.addAll(newList); // Add the newly imported clients to the list
    }

    /**
     * Exports the current list of clients to an XML file using JAXB.
     *
     * @throws JAXBException If there is an error during XML processing.
     */
    public void jaxbExport() throws JAXBException {
        FileManagement.clientToXml(this.clients); // Export current clients to XML
    }

    /**
     * Imports clients from an XML file using JAXB.
     *
     * @throws IOException If there is an error reading the file.
     * @throws JAXBException If there is an error during XML processing.
     */
    public void jaxbImport() throws IOException, JAXBException {
        this.clients = FileManagement.xmlToClient(); // Import clients from XML
    }
}
