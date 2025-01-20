package controller.db.impl;

import controller.db.OrderDao;
import model.*;
import utils.DatabaseConf;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.sql.DriverManager.getConnection;

/**
 * JdbcOrderDao is an implementation of the {@link OrderDao} interface that provides
 * data access operations for managing orders in a relational database.
 * This class handles the creation, retrieval, update, and deletion (CRUD)
 * of orders and their associated order lines, utilizing JDBC for database
 * interactions.
 *
 * <p>This class is responsible for executing SQL statements to manipulate
 * order data, including inserting new orders, updating existing orders,
 * deleting orders, and fetching order information based on various criteria.
 * It uses the JdbcCustomerDao and JdbcProductDao to manage related Customer and
 * product data.</p>
 *
 * <p>Each method in this class is designed to throw a {@link SQLException} if an
 * error occurs while interacting with the database.</p>
 *
 * <p>Usage of this class requires proper initialization of the database
 * connection settings provided in the DatabaseConf class.</p>
 *
 * @see OrderDao
 * @see Order
 * @see OrderLine
 * @see Customer
 * @see Product
 *
 * @author DaniAndries
 * @version 0.1
 */
public class JdbcOrderDao implements OrderDao {
    // * int id; OrderState state; Date orderDate; String paymentMethod; List<OrderLine> orderLines; Customer Customer;
    private static final String INSERT_ORDER = "INSERT INTO customer_order (state, order_date, payment_method, Customer) VALUES (?,?,?,?)";
    // ? int id; int amount; Product product;
    private static final String INSERT_ORDER_LINE = "INSERT INTO order_line (amount, product, customer_order) VALUES (?,?,?)";
    private static final String UPDATE_ORDER = "UPDATE customer_order SET customer_order.order_date=?, customer_order.payment_method=?, customer_order.state=? WHERE customer_order.id=?";
    private static final String UPDATE_ORDER_LINE = "UPDATE order_line SET order_line.amount=?, order_line.product=? WHERE order_line.id=?";
    private static final String DELETE_ORDER = "DELETE FROM customer_order WHERE customer_order.ID = ?";
    private static final String DELETE_ORDER_LINE = "DELETE FROM order_line WHERE order_line.ID = ?";
    private static final String SELECT_ORDER = "SELECT customer_order.id, state, customer_order.order_date, customer_order.payment_method, customer_order.Customer FROM customer_order WHERE customer_order.ID=?";
    private static final String SELECT_ORDER_BY_CLIENT = "SELECT customer_order.id, customer_order.order_date, customer_order.state, customer_order.payment_method, customer_order.Customer FROM customer_order WHERE customer_order.Customer=?";
    private static final String SELECT_ORDER_BY_STATE = "SELECT customer_order.id, customer_order.order_date, customer_order.state, customer_order.payment_method, customer_order.Customer FROM customer_order WHERE customer_order.state=? and customer_order.Customer=?";
    private static final String SELECT_ORDER_LINE = "SELECT order_line.id, order_line.amount, order_line.product FROM order_line WHERE order_line.ID=?";
    private static final String SELECT_ORDER_LINE_BY_ORDER = "SELECT order_line.id, order_line.amount, order_line.product FROM order_line WHERE order_line.customer_order = ?";
    JdbcCustomerDao jdbcCustomerDao = new JdbcCustomerDao();
    JdbcProductDao jdbcProductDao = new JdbcProductDao();

    /**
     * Default constructor for JdbcOrderDao.
     * <p>
     * This constructor initializes a new instance of JdbcOrderDao.
     * </p>
     */
    public JdbcOrderDao() {
        // Empty constructor
    }

    /**
     * Saves a new order to the database.
     *
     * @param order the order to be saved
     * @throws SQLException if a database access error occurs
     */
    // * "INSERT INTO customer_order (state, order_date, payment_method, Customer) VALUES (?,?,?,?)"
    @Override
    public void saveOrder(Order order) throws SQLException {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DatabaseConf.URL, DatabaseConf.USER, DatabaseConf.PASSWORD);
            conn.setAutoCommit(false);
            PreparedStatement stmtOrder = conn.prepareStatement(INSERT_ORDER, Statement.RETURN_GENERATED_KEYS);
            List<OrderLine> orderLines = new ArrayList<>();

            stmtOrder.setString(1, order.getState().toString());
            stmtOrder.setDate(2, new Date(order.getOrderDate().getTime()));
            stmtOrder.setString(3, order.getPaymentMethod().toString());
            stmtOrder.setInt(4, order.getCustomer().getId());

            stmtOrder.executeUpdate();

            try (ResultSet generatedKeys = stmtOrder.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    order.setId(generatedKeys.getInt(1));
                }
            }

            addOrderLines(order.getOrderLines(), order, conn);

            System.out.println("The Order: " + order.getId() + " has been created");
            conn.commit();
        } catch (SQLException e) {
            if (conn != null){
                conn.rollback();
            }

            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                conn.close();
            } else {
                System.out.println("sex");
            }
        }
    }

    @Override
    public void saveOrderLine(List<OrderLine> orderLine, Order order) throws SQLException {

    }

    /**
     * Adds order lines to the specified order.
     *
     * @param orderLines the list of order lines to add
     * @param order      the order to which the lines will be added
     * @throws SQLException if a database access error occurs
     */
    private void addOrderLines(List<OrderLine> orderLines, Order order, Connection conn ) throws SQLException {
        for (OrderLine orderLine : orderLines) {
            saveOrderLine(orderLine, order, conn);
        }
    }

    /**
     * Saves a new order line to the database and associates it with a specified order.
     * The method inserts a new record into the `order_line` table with the provided details.
     * If the operation is successful, the generated ID for the new order line is set on the
     * provided {@link OrderLine} object.
     *
     * @param orderLine the order line to be saved
     * @param order     the order associated with the order line
     * @throws SQLException if a database access error occurs
     */
    // * "INSERT INTO order_line (amount, product, customer_order) VALUES (?,?,?)"
    public void saveOrderLine(OrderLine orderLine, Order order) throws SQLException {
        try (Connection conn = getConnection(DatabaseConf.URL, DatabaseConf.USER, DatabaseConf.PASSWORD);
             PreparedStatement stmtOrder = conn.prepareStatement(INSERT_ORDER_LINE, Statement.RETURN_GENERATED_KEYS)) {

            stmtOrder.setInt(1, orderLine.getAmount());
            stmtOrder.setInt(2, orderLine.getProducto().getId());
            stmtOrder.setInt(3, order.getId());

            stmtOrder.executeUpdate();

            try (ResultSet generatedKeys = stmtOrder.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    orderLine.setId(generatedKeys.getInt(1));
                }
            }

            System.out.println("The OrderLine: " + order.getId() + " has been created");
        }
    }

    /**
     * Saves a new order line to the database and associates it with a specified order.
     * The method inserts a new record into the `order_line` table with the provided details.
     * If the operation is successful, the generated ID for the new order line is set on the
     * provided {@link OrderLine} object.
     *
     * @param orderLine the order line to be saved, containing the details of the line item
     * @param order     the order to which the order line will be associated
     * @param conn      the database connection used for executing the insert statement
     * @throws SQLException if a database access error occurs or the insert operation fails
     */
    // * "INSERT INTO order_line (amount, product, customer_order) VALUES (?,?,?)"
    @Override
    public void saveOrderLine(OrderLine orderLine, Order order, Connection conn) throws SQLException {
        try (PreparedStatement stmtOrder = conn.prepareStatement(INSERT_ORDER_LINE, Statement.RETURN_GENERATED_KEYS)) {

            stmtOrder.setInt(1, orderLine.getAmount());
            stmtOrder.setInt(2, orderLine.getProducto().getId());
            stmtOrder.setInt(3, order.getId());

            stmtOrder.executeUpdate();

            try (ResultSet generatedKeys = stmtOrder.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    orderLine.setId(generatedKeys.getInt(1));
                }
            }

            System.out.println("The OrderLine: " + order.getId() + " has been created");
        }
    }

    /**
     * Updates the details of an existing order.
     *
     * @param order the order to be updated
     * @throws SQLException if a database access error occurs
     */
    // * "UPDATE customer_order SET customer_order.order_date=?, customer_order.payment_method=?, customer_order.state=? WHERE customer_order.id=?"
    @Override
    public void updateOrder(Order order) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DatabaseConf.URL, DatabaseConf.USER, DatabaseConf.PASSWORD);
             PreparedStatement stmtOrder = conn.prepareStatement(UPDATE_ORDER)) {

            stmtOrder.setDate(1, new java.sql.Date(order.getOrderDate().getTime()));
            stmtOrder.setString(2, order.getPaymentMethod().toString());
            stmtOrder.setString(3, order.getState().toString()); // Asegúrate de que el estado se establezca aquí
            stmtOrder.setInt(4, order.getId());

            stmtOrder.executeUpdate(); // Usa executeUpdate para operaciones de modificación
            System.out.println("The order: " + order.getId() + " has been modified");
        }
    }

    /**
     * Updates an existing order line.
     *
     * @param orderLine the order line to be updated
     * @throws SQLException if a database access error occurs
     */
    // * "UPDATE order_line SET order_line.amount=?, order_line.product=? WHERE order_line.id=?"
    @Override
    public void updateOrderLine(OrderLine orderLine) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DatabaseConf.URL, DatabaseConf.USER, DatabaseConf.PASSWORD);
             PreparedStatement stmtOrderLine = conn.prepareStatement(UPDATE_ORDER_LINE)) {
            stmtOrderLine.setInt(1, orderLine.getAmount());
            stmtOrderLine.setInt(2, orderLine.getProducto().getId());
            stmtOrderLine.setInt(3, orderLine.getId());

            stmtOrderLine.execute();
            System.out.println("The orderLine: " + orderLine.getId() + " has been modified");
        }
    }

    /**
     * Deletes an order from the database.
     *
     * @param order the order to be deleted
     * @throws SQLException if a database access error occurs
     */
    // * "DELETE FROM customer_order WHERE customer_order.ID = ?"
    @Override
    public void deleteOrder(Order order) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DatabaseConf.URL, DatabaseConf.USER, DatabaseConf.PASSWORD);
             PreparedStatement stmtOrder = conn.prepareStatement(DELETE_ORDER)) {
            stmtOrder.setInt(1, order.getId());
            stmtOrder.execute();
            System.out.println("The order: " + order.getId() + " has been deleted");
        }
    }

    /**
     * Deletes an order line from the database.
     *
     * @param orderLine the order line to be deleted
     * @throws SQLException if a database access error occurs
     */
    // * "DELETE FROM order_line WHERE order_line.ID = ?"
    @Override
    public void deleteOrderLine(OrderLine orderLine) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DatabaseConf.URL, DatabaseConf.USER, DatabaseConf.PASSWORD);
             PreparedStatement stmtOrder = conn.prepareStatement(DELETE_ORDER_LINE)) {
            stmtOrder.setInt(1, orderLine.getId());
            stmtOrder.execute();
            System.out.println("The orderLine: " + orderLine.getId() + " has been deleted");
        }
    }

    /**
     * Selects an order by its ID.
     *
     * @param id the ID of the order to select
     * @return the selected order, or null if not found
     * @throws SQLException if a database access error occurs
     */
    // * "SELECT customer_order.id, state, customer_order.order_date, customer_order.payment_method, customer_order.Customer FROM customer_order WHERE customer_order.ID=?"
    @Override
    public Order findOrder(int id) throws SQLException {
        Order order;
        List<OrderLine> orderLines;
        try (Connection conn = getConnection(DatabaseConf.URL, DatabaseConf.USER, DatabaseConf.PASSWORD);
             PreparedStatement stmtOrder = conn.prepareStatement(SELECT_ORDER)) {
            stmtOrder.setInt(1, id);
            try (ResultSet rsOrder = stmtOrder.executeQuery()) {
                if (rsOrder.next()) {
                    order = new Order(
                            rsOrder.getInt("id"),
                            rsOrder.getDate("order_date"),
                            OrderState.valueOf(rsOrder.getString("state")),
                            PaymentMethod.valueOf(rsOrder.getString("payment_method")),
                            jdbcCustomerDao.findById(rsOrder.getInt("Customer"))
                    );
                    orderLines = findOrderLinesByOrder(order);
                    order.setOrderLines(orderLines);
                } else {
                    order = null;
                }
            }
            return order;
        }
    }

    /**
     * Selects all orders for a specific Customer.
     *
     * @param customer the Customer whose orders are to be selected
     * @return a list of orders associated with the Customer
     * @throws SQLException if a database access error occurs
     */
    // * "SELECT customer_order.id, customer_order.order_date, customer_order.state, customer_order.payment_method, customer_order.Customer FROM customer_order WHERE customer_order.Customer=?"
    @Override
    public List<Order> findOrdersByCustomer(Customer customer) throws SQLException {
        List<Order> orders = new ArrayList<>();
        Order order = null;
        try (Connection conn = getConnection(DatabaseConf.URL, DatabaseConf.USER, DatabaseConf.PASSWORD);
             PreparedStatement stmtOrder = conn.prepareStatement(SELECT_ORDER_BY_CLIENT)) {
            stmtOrder.setInt(1, customer.getId());
            try (ResultSet rsOrder = stmtOrder.executeQuery()) {
                if (rsOrder.next()) {
                    order = new Order(
                            rsOrder.getInt("id"),
                            rsOrder.getDate("order_date"),
                            OrderState.valueOf(rsOrder.getString("state")),
                            PaymentMethod.valueOf(rsOrder.getString("payment_method")),
                            jdbcCustomerDao.findById(rsOrder.getInt("Customer"))
                    );
                    order.setOrderLines(findOrderLinesByOrder(order));
                    orders.add(order);
                }
            }
        }
        return orders;
    }

    /**
     * Selects an order by its state and associated Customer.
     *
     * @param state  the state of the order to select
     * @param customer the Customer associated with the order
     * @return the selected order, or null if not found
     * @throws SQLException if a database access error occurs
     */
    // * "SELECT customer_order.id, customer_order.order_date, customer_order.state, customer_order.payment_method, customer_order.Customer FROM customer_order WHERE customer_order.state=? and customer_order.Customer=?"
    @Override
    public List<Order> findOrdersByState(OrderState state, Customer customer) throws SQLException {
        List<Order> orders = null;
        Order order;
        try (Connection conn = getConnection(DatabaseConf.URL, DatabaseConf.USER, DatabaseConf.PASSWORD);
             PreparedStatement stmtOrder = conn.prepareStatement(SELECT_ORDER_BY_STATE)) {
            stmtOrder.setString(1, state.toString());
            stmtOrder.setInt(2, customer.getId());
            try (ResultSet rsOrder = stmtOrder.executeQuery()) {
                while (rsOrder.next()) {
                    order = new Order(
                            rsOrder.getInt("id"),
                            rsOrder.getDate("order_date"),
                            OrderState.valueOf(rsOrder.getString("state")),
                            PaymentMethod.valueOf(rsOrder.getString("payment_method")),
                            jdbcCustomerDao.findById(rsOrder.getInt("Customer"))
                    );
                    order.setOrderLines(findOrderLinesByOrder(order));
                    if (orders == null) {
                        orders = new ArrayList<>();
                    }
                    orders.add(order);
                }
            }
            return orders;
        }
    }

    /**
     * Selects an order line by its ID.
     *
     * @param id the ID of the order line to select
     * @return the selected order line, or null if not found
     * @throws SQLException if a database access error occurs
     */
    // * "SELECT order_line.id, order_line.amount, order_line.product FROM Customer WHERE order_line.ID=?"
    @Override
    public OrderLine findOrderLine(int id) throws SQLException {
        OrderLine orderLine;
        try (Connection conn = getConnection(DatabaseConf.URL, DatabaseConf.USER, DatabaseConf.PASSWORD);
             PreparedStatement stmtOrderLine = conn.prepareStatement(SELECT_ORDER_LINE)) {
            stmtOrderLine.setInt(1, id);
            try (ResultSet rsOrder = stmtOrderLine.executeQuery()) {
                if (rsOrder.next()) {
                    orderLine = new OrderLine(
                            rsOrder.getInt("id"),
                            rsOrder.getInt("amount")
                    );
                    orderLine.setProducto(jdbcProductDao.findProductById(rsOrder.getInt("product")));
                } else {
                    orderLine = null;
                }
            }
            return orderLine;
        }
    }

    /**
     * Selects all order lines associated with a specific order.
     *
     * @param order the order whose lines are to be selected
     * @return a list of order lines associated with the order
     * @throws SQLException if a database access error occurs
     */
    // * "SELECT order_line.id, order_line.amount, order_line.product FROM order_line WHERE order_line.customer_order = ?;"
    @Override
    public List<OrderLine> findOrderLinesByOrder(Order order) throws SQLException {
        OrderLine orderLine;
        List<OrderLine> orderLines = new ArrayList<>();
        try (Connection conn = getConnection(DatabaseConf.URL, DatabaseConf.USER, DatabaseConf.PASSWORD);
             PreparedStatement stmtOrder = conn.prepareStatement(SELECT_ORDER_LINE_BY_ORDER)) {
            stmtOrder.setInt(1, order.getId());
            try (ResultSet rsOrderLine = stmtOrder.executeQuery()) {
                while (rsOrderLine.next()) {
                    orderLine = new OrderLine(
                            rsOrderLine.getInt("id"),
                            rsOrderLine.getInt("amount")
                    );
                    Product product = jdbcProductDao.findProductById(rsOrderLine.getInt("product"));
                    if (product != null) {
                        orderLine.setProducto(product);
                    }
                    orderLines.add(orderLine);
                }
            }
        }
        return orderLines;
    }
}
