package controller.db.impl;


import controller.db.ClientDao;
import model.Client;
import utils.DatabaseConf;

import java.sql.*;
import java.util.List;

import static java.sql.DriverManager.getConnection;

public class JdbcClientDao implements ClientDao {
    static final JdbcOrderDao jdbcCarDao = new JdbcOrderDao();
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
    private static final String INSERT_CLIENT = "INSERT INTO Client (dni, client_name, direction, phone, mail, password, admin) VALUES (?,?,?,?,?,?,?)";
    private static final String UPDATE_CLIENT = "UPDATE Client SET client.dni=?, client.client_name=?, client.direction=?, client.phone=?, client.mail=?, client.password=?, client.admin=?";
    private static final String DELETE_CLIENT = "DELETE FROM client WHERE client.ID = ?";
    private static final String SELECT_CLIENT = "SELECT client.id, client.dni, client.client_name, client.direction, client.phone, client.mail, client.password, client.admin FROM client WHERE client.ID = ?";
    private static final String SELECT_CLIENT_DNI = "SELECT client.id, client.dni, client.client_name, client.direction, client.phone, client.mail, client.password, client.admin FROM client WHERE client.DNI = ?";

    // INSERT INTO Client (dni, client_name, direction, phone, mail, password, admin) VALUES (?,?,?,?,?,?,?)
    @Override
    public void save(Client client) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DatabaseConf.URL, DatabaseConf.USER, DatabaseConf.PASSWORD);
             PreparedStatement stmtClient = conn.prepareStatement(INSERT_CLIENT, Statement.RETURN_GENERATED_KEYS)) {
            stmtClient.setString(1, client.getDni());
            stmtClient.setString(2, client.getClientName());
            stmtClient.setString(3, client.getDirection());
            stmtClient.setString(6, client.getPhone());
            stmtClient.setString(4, client.getMail());
            stmtClient.setString(5, client.getPassword());
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

    // UPDATE Client SET client.dni=?, client.client_name=?, client.direction=?, client.phone=?, client.mail=?, client.password=?, client.admin=?
    @Override
    public void update(Client client) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DatabaseConf.URL, DatabaseConf.USER, DatabaseConf.PASSWORD);
             PreparedStatement stmtClient = conn.prepareStatement(UPDATE_CLIENT)) {
            stmtClient.setString(1, client.getClientName());
            stmtClient.setString(2, client.getMail());
            stmtClient.setString(3, client.getPhone());
            stmtClient.setString(4, client.getDni());
            stmtClient.execute();
            System.out.println("The client: " + client.getDni() + " has been modified");
        }
    }

    // DELETE FROM client WHERE client.ID = ?
    @Override
    public void delete(Client client) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DatabaseConf.URL, DatabaseConf.USER, DatabaseConf.PASSWORD);
             PreparedStatement stmtClient = conn.prepareStatement(DELETE_CLIENT)) {
            stmtClient.setString(1, client.getDni());
            stmtClient.execute();
            System.out.println("The client: " + client.getDni() + " has been deleted");
        }
    }

    // SELECT client.id, client.dni, client.client_name, client.direction, client.phone, client.mail, client.password, client.admin FROM client WHERE client.ID = ?
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

    // SELECT client.id, client.dni, client.client_name, client.direction, client.phone, client.mail, client.password, client.admin FROM client WHERE client.DNI = ?
    @Override
    public Client findByMail(String dni) throws SQLException {
        Client client;
        try (Connection conn = getConnection(DatabaseConf.URL, DatabaseConf.USER, DatabaseConf.PASSWORD); PreparedStatement stmtclient = conn.prepareStatement(SELECT_CLIENT_DNI)) {
            stmtclient.setString(1, dni);
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

    @Override
    public List<Client> findAll(String dni) throws SQLException {
        return List.of();
    }
}
