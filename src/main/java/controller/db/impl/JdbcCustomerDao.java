package controller.db.impl;


import controller.db.CustomerDao;
import model.Customer;
import utils.DatabaseConf;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.sql.DriverManager.getConnection;

/**
 * JdbcCustomerDao is an implementation of the {@link CustomerDao} interface that provides
 * data access operations for managing customer records in a relational database.
 * This class facilitates the creation, retrieval, update, and deletion (CRUD)
 * of customer data using JDBC for database interactions.
 *
 * <p>It interacts with the database to perform operations such as inserting new customers,
 * updating existing customer details, deleting customers, and fetching customer information
 * based on specific criteria such as ID or email.</p>
 *
 * <p>Each method in this class is designed to throw a {@link SQLException} if an error occurs
 * during database operations.</p>
 *
 * <p>This class requires proper initialization of the database connection settings
 * defined in the DatabaseConf class.</p>
 *
 * @see CustomerDao
 * @see Customer
 *
 * @author DaniAndries
 * @version 0.1
 */
public class JdbcCustomerDao implements CustomerDao {
    static final JdbcOrderDao jdbcCarDao = new JdbcOrderDao();

    //* int id; String dni; String customerName; String direction; String phone; String mail; String password; List<Order> orderList; boolean admin;
    private static final String INSERT_CLIENT = "INSERT INTO Customer (dni, customer_name, direction, phone, mail, password, admin) VALUES (?,?,?,?,?,?,?)";

    private static final String UPDATE_CLIENT = "UPDATE Customer SET customer.customer_name=?, customer.direction=?, customer.phone=?, customer.password=?, customer.admin=? WHERE customer.id = ?";

    private static final String DELETE_CLIENT = "DELETE FROM customer WHERE customer.ID = ?";

    private static final String SELECT_CLIENT = "SELECT customer.id, customer.dni, customer.customer_name, customer.direction, customer.phone, customer.mail, customer.password, customer.admin FROM customer WHERE customer.ID = ?";
    private static final String SELECT_CLIENT_MAIL = "SELECT customer.id, customer.dni, customer.customer_name, customer.direction, customer.phone, customer.mail, customer.password, customer.admin FROM customer WHERE customer.MAIl = ?";
    private static final String SELECT_ALL = "SELECT customer.id, customer.dni, customer.customer_name, customer.direction, customer.phone, customer.mail, customer.password, customer.admin FROM customer";

    /**
     * Default constructor for JdbcCustomerDao.
     * <p>
     * This constructor initializes a new instance of JdbcCustomerDao.
     * </p>
     */
    public JdbcCustomerDao() {
        // Empty constructor
    }

    /**
     * Saves a new customer record to the database.
     *
     * @param customer the customer object containing details to be saved
     * @throws SQLException if a database access error occurs
     */
    // * INSERT INTO Customer (dni, customer_name, direction, phone, mail, password, admin) VALUES (?,?,?,?,?,?,?)
    @Override
    public void save(Customer customer) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DatabaseConf.URL, DatabaseConf.USER, DatabaseConf.PASSWORD);
             PreparedStatement stmtCustomer = conn.prepareStatement(INSERT_CLIENT, Statement.RETURN_GENERATED_KEYS)) {
            stmtCustomer.setString(1, customer.getDni());
            stmtCustomer.setString(2, customer.getCustomerName());
            stmtCustomer.setString(3, customer.getDirection());
            stmtCustomer.setString(4, customer.getPhone());
            stmtCustomer.setString(5, customer.getMail());
            stmtCustomer.setString(6, customer.getPassword());
            stmtCustomer.setBoolean(7, customer.isAdmin());

            stmtCustomer.executeUpdate();

            try (ResultSet generatedKeys = stmtCustomer.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    customer.setId(generatedKeys.getInt(1));
                }
            }
            System.out.println("The customer: " + customer.getDni() + " has been created");
        }
    }

    /**
     * Updates an existing customer record in the database.
     *
     * @param customer the customer object containing updated details
     * @throws SQLException if a database access error occurs
     */
    // * UPDATE Customer SET customer.customer_name=?, customer.direction=?, customer.phone=?, customer.password=?, customer.admin=? WHERE customer.id = ?
    @Override
    public void update(Customer customer) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DatabaseConf.URL, DatabaseConf.USER, DatabaseConf.PASSWORD);
             PreparedStatement stmtCustomer = conn.prepareStatement(UPDATE_CLIENT)) {
            stmtCustomer.setString(1, customer.getCustomerName());
            stmtCustomer.setString(2, customer.getDirection());
            stmtCustomer.setString(3, customer.getPhone());
            stmtCustomer.setString(4, customer.getPassword());
            stmtCustomer.setBoolean(5, customer.isAdmin());
            stmtCustomer.setInt(6, customer.getId());
            stmtCustomer.execute();
            System.out.println("The customer: " + customer.getDni() + " has been modified");
        }
    }

    /**
     * Deletes a customer record from the database.
     *
     * @param customer the customer object to be deleted
     * @throws SQLException if a database access error occurs
     */
    // * DELETE FROM customer WHERE customer.ID = ?
    @Override
    public void delete(Customer customer) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DatabaseConf.URL, DatabaseConf.USER, DatabaseConf.PASSWORD);
             PreparedStatement stmtCustomer = conn.prepareStatement(DELETE_CLIENT)) {
            stmtCustomer.setInt(1, customer.getId());
            stmtCustomer.execute();
            System.out.println("The customer: " + customer.getDni() + " has been deleted");
        }
    }
    /**
     * Finds a customer by their unique ID.
     *
     * @param id the ID of the customer to be retrieved
     * @return the customer object if found, or null if no customer with the specified ID exists
     * @throws SQLException if a database access error occurs
     */
    // * SELECT customer.id, customer.dni, customer.customer_name, customer.direction, customer.phone, customer.mail, customer.password, customer.admin FROM customer WHERE customer.ID = ?
    @Override
    public Customer findById(int id) throws SQLException {
        Customer customer;
        try (Connection conn = getConnection(DatabaseConf.URL, DatabaseConf.USER, DatabaseConf.PASSWORD); PreparedStatement stmtcustomer = conn.prepareStatement(SELECT_CLIENT)) {
            stmtcustomer.setInt(1, id);
            try (ResultSet rsCustomer = stmtcustomer.executeQuery()) {
                if (rsCustomer.next()) {
                    customer = new Customer(
                            rsCustomer.getInt("id"),
                            rsCustomer.getString("dni"),
                            rsCustomer.getString("customer_name"),
                            rsCustomer.getString("direction"),
                            rsCustomer.getString("phone"),
                            rsCustomer.getString("mail"),
                            rsCustomer.getString("password"),
                            rsCustomer.getBoolean("admin")
                    );
                    return customer;
                }
            }
            return null;
        }
    }

    /**
     * Finds a customer by their email address.
     *
     * @param mail the email of the customer to be retrieved
     * @return the customer object if found, or null if no customer with the specified email exists
     * @throws SQLException if a database access error occurs
     */
    // * SELECT customer.id, customer.dni, customer.customer_name, customer.direction, customer.phone, customer.mail, customer.password, customer.admin FROM customer WHERE customer.MAIl = ?
    @Override
    public Customer findByMail(String mail) throws SQLException {
        Customer customer;
        try (Connection conn = getConnection(DatabaseConf.URL, DatabaseConf.USER, DatabaseConf.PASSWORD); PreparedStatement stmtcustomer = conn.prepareStatement(SELECT_CLIENT_MAIL)) {
            stmtcustomer.setString(1, mail);
            try (ResultSet rsCustomer = stmtcustomer.executeQuery()) {
                if (rsCustomer.next()) {
                    customer = new Customer(
                            rsCustomer.getInt("id"),
                            rsCustomer.getString("dni"),
                            rsCustomer.getString("customer_name"),
                            rsCustomer.getString("direction"),
                            rsCustomer.getString("phone"),
                            rsCustomer.getString("mail"),
                            rsCustomer.getString("password"),
                            rsCustomer.getBoolean("admin")
                    );
                    return customer;
                }
            }
            return null;
        }
    }

    /**
     * Retrieves all customers from the database.
     *
     * @return a list of customer objects
     * @throws SQLException if a database access error occurs
     */
    // * "SELECT customer.id, customer.dni, customer.customer_name, customer.direction, customer.phone, customer.mail, customer.password, customer.admin FROM customer"
    @Override
    public List<Customer> findAll() throws SQLException {
        List<Customer> customerList =new ArrayList<>();
        Customer customer;
        try (Connection conn = getConnection(DatabaseConf.URL, DatabaseConf.USER, DatabaseConf.PASSWORD); PreparedStatement stmtcustomer = conn.prepareStatement(SELECT_ALL)) {
            try (ResultSet rsCustomer = stmtcustomer.executeQuery()) {
                while (rsCustomer.next()) {
                    customer = new Customer(
                            rsCustomer.getInt("id"),
                            rsCustomer.getString("dni"),
                            rsCustomer.getString("customer_name"),
                            rsCustomer.getString("direction"),
                            rsCustomer.getString("phone"),
                            rsCustomer.getString("mail"),
                            rsCustomer.getString("password"),
                            rsCustomer.getBoolean("admin")

                    );
                    customerList.add(customer);
                }
                return customerList;
            }
        }
    }
}
