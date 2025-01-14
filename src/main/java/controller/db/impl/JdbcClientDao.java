package controller.db.impl;


import controller.db.ClientDao;
import controller.db.ProductDao;
import model.Client;
import utils.DatabaseConf;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.sql.DriverManager.getConnection;

/**
 * JdbcClientDao is an implementation of the {@link ClientDao} interface that provides
 * data access operations for managing client records in a relational database.
 * This class facilitates the creation, retrieval, update, and deletion (CRUD)
 * of client data using JDBC for database interactions.
 *
 * <p>It interacts with the database to perform operations such as inserting new clients,
 * updating existing client details, deleting clients, and fetching client information
 * based on specific criteria such as ID or email.</p>
 *
 * <p>Each method in this class is designed to throw a {@link SQLException} if an error occurs
 * during database operations.</p>
 *
 * <p>This class requires proper initialization of the database connection settings
 * defined in the DatabaseConf class.</p>
 *
 * @see ClientDao
 * @see Client
 *
 * @author DaniAndries
 * @version 0.1
 */
public class JdbcClientDao implements ClientDao {
    static final JdbcOrderDao jdbcCarDao = new JdbcOrderDao();

    //* int id; String dni; String clientName; String direction; String phone; String mail; String password; List<Order> orderList; boolean admin;
    private static final String INSERT_CLIENT = "INSERT INTO Client (dni, client_name, direction, phone, mail, password, admin) VALUES (?,?,?,?,?,?,?)";

    private static final String UPDATE_CLIENT = "UPDATE Client SET client.client_name=?, client.direction=?, client.phone=?, client.password=?, client.admin=? WHERE client.id = ?";

    private static final String DELETE_CLIENT = "DELETE FROM client WHERE client.ID = ?";

    private static final String SELECT_CLIENT = "SELECT client.id, client.dni, client.client_name, client.direction, client.phone, client.mail, client.password, client.admin FROM client WHERE client.ID = ?";
    private static final String SELECT_CLIENT_MAIL = "SELECT client.id, client.dni, client.client_name, client.direction, client.phone, client.mail, client.password, client.admin FROM client WHERE client.MAIl = ?";
    private static final String SELECT_ALL = "SELECT client.id, client.dni, client.client_name, client.direction, client.phone, client.mail, client.password, client.admin FROM client";

    /**
     * Default constructor for JdbcClientDao.
     * <p>
     * This constructor initializes a new instance of JdbcClientDao.
     * </p>
     */
    public JdbcClientDao() {
        // Empty constructor
    }

    /**
     * Saves a new client record to the database.
     *
     * @param client the client object containing details to be saved
     * @throws SQLException if a database access error occurs
     */
    // * INSERT INTO Client (dni, client_name, direction, phone, mail, password, admin) VALUES (?,?,?,?,?,?,?)
    @Override
    public void save(Client client) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DatabaseConf.URL, DatabaseConf.USER, DatabaseConf.PASSWORD);
             PreparedStatement stmtClient = conn.prepareStatement(INSERT_CLIENT, Statement.RETURN_GENERATED_KEYS)) {
            stmtClient.setString(1, client.getDni());
            stmtClient.setString(2, client.getClientName());
            stmtClient.setString(3, client.getDirection());
            stmtClient.setString(4, client.getPhone());
            stmtClient.setString(5, client.getMail());
            stmtClient.setString(6, client.getPassword());
            stmtClient.setBoolean(7, client.isAdmin());

            stmtClient.executeUpdate();

            try (ResultSet generatedKeys = stmtClient.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    client.setId(generatedKeys.getInt(1));
                }
            }
            System.out.println("The client: " + client.getDni() + " has been created");
        }
    }

    /**
     * Updates an existing client record in the database.
     *
     * @param client the client object containing updated details
     * @throws SQLException if a database access error occurs
     */
    // * UPDATE Client SET client.client_name=?, client.direction=?, client.phone=?, client.password=?, client.admin=? WHERE client.id = ?
    @Override
    public void update(Client client) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DatabaseConf.URL, DatabaseConf.USER, DatabaseConf.PASSWORD);
             PreparedStatement stmtClient = conn.prepareStatement(UPDATE_CLIENT)) {
            stmtClient.setString(1, client.getClientName());
            stmtClient.setString(2, client.getDirection());
            stmtClient.setString(3, client.getPhone());
            stmtClient.setString(4, client.getPassword());
            stmtClient.setBoolean(5, client.isAdmin());
            stmtClient.setInt(6, client.getId());
            stmtClient.execute();
            System.out.println("The client: " + client.getDni() + " has been modified");
        }
    }

    /**
     * Deletes a client record from the database.
     *
     * @param client the client object to be deleted
     * @throws SQLException if a database access error occurs
     */
    // * DELETE FROM client WHERE client.ID = ?
    @Override
    public void delete(Client client) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DatabaseConf.URL, DatabaseConf.USER, DatabaseConf.PASSWORD);
             PreparedStatement stmtClient = conn.prepareStatement(DELETE_CLIENT)) {
            stmtClient.setInt(1, client.getId());
            stmtClient.execute();
            System.out.println("The client: " + client.getDni() + " has been deleted");
        }
    }
    /**
     * Finds a client by their unique ID.
     *
     * @param id the ID of the client to be retrieved
     * @return the client object if found, or null if no client with the specified ID exists
     * @throws SQLException if a database access error occurs
     */
    // * SELECT client.id, client.dni, client.client_name, client.direction, client.phone, client.mail, client.password, client.admin FROM client WHERE client.ID = ?
    @Override
    public Client findById(int id) throws SQLException {
        Client client;
        try (Connection conn = getConnection(DatabaseConf.URL, DatabaseConf.USER, DatabaseConf.PASSWORD); PreparedStatement stmtclient = conn.prepareStatement(SELECT_CLIENT)) {
            stmtclient.setInt(1, id);
            try (ResultSet rsClient = stmtclient.executeQuery()) {
                if (rsClient.next()) {
                    client = new Client(
                            rsClient.getInt("id"),
                            rsClient.getString("dni"),
                            rsClient.getString("client_name"),
                            rsClient.getString("direction"),
                            rsClient.getString("phone"),
                            rsClient.getString("mail"),
                            rsClient.getString("password"),
                            rsClient.getBoolean("admin")
                    );
                    return client;
                }
            }
            return null;
        }
    }

    /**
     * Finds a client by their email address.
     *
     * @param mail the email of the client to be retrieved
     * @return the client object if found, or null if no client with the specified email exists
     * @throws SQLException if a database access error occurs
     */
    // * SELECT client.id, client.dni, client.client_name, client.direction, client.phone, client.mail, client.password, client.admin FROM client WHERE client.MAIl = ?
    @Override
    public Client findByMail(String mail) throws SQLException {
        Client client;
        try (Connection conn = getConnection(DatabaseConf.URL, DatabaseConf.USER, DatabaseConf.PASSWORD); PreparedStatement stmtclient = conn.prepareStatement(SELECT_CLIENT_MAIL)) {
            stmtclient.setString(1, mail);
            try (ResultSet rsClient = stmtclient.executeQuery()) {
                if (rsClient.next()) {
                    client = new Client(
                            rsClient.getInt("id"),
                            rsClient.getString("dni"),
                            rsClient.getString("client_name"),
                            rsClient.getString("direction"),
                            rsClient.getString("phone"),
                            rsClient.getString("mail"),
                            rsClient.getString("password"),
                            rsClient.getBoolean("admin")
                    );
                    return client;
                }
            }
            return null;
        }
    }

    /**
     * Retrieves all clients from the database.
     *
     * @return a list of client objects
     * @throws SQLException if a database access error occurs
     */
    // * "SELECT client.id, client.dni, client.client_name, client.direction, client.phone, client.mail, client.password, client.admin FROM client"
    @Override
    public List<Client> findAll() throws SQLException {
        List<Client> clientList =new ArrayList<>();
        Client client;
        try (Connection conn = getConnection(DatabaseConf.URL, DatabaseConf.USER, DatabaseConf.PASSWORD); PreparedStatement stmtclient = conn.prepareStatement(SELECT_ALL)) {
            try (ResultSet rsClient = stmtclient.executeQuery()) {
                while (rsClient.next()) {
                    client = new Client(
                            rsClient.getInt("id"),
                            rsClient.getString("dni"),
                            rsClient.getString("client_name"),
                            rsClient.getString("direction"),
                            rsClient.getString("phone"),
                            rsClient.getString("mail"),
                            rsClient.getString("password"),
                            rsClient.getBoolean("admin")

                    );
                    clientList.add(client);
                }
                return clientList;
            }
        }
    }
}
