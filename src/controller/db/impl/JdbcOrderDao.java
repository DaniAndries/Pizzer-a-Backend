package controller.db.impl;

import controller.db.OrderDao;
import model.*;
import utils.DatabaseConf;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.sql.DriverManager.getConnection;

public class JdbcOrderDao implements OrderDao {
    JdbcClientDao jdbcClientDao = new JdbcClientDao();
    JdbcProductDao jdbcProductDao = new JdbcProductDao();

    // * int id; OrderState state; Date orderDate; float totalPrice; String paymentMethod; List<OrderLine> orderLines; Client client;
    private static final String INSERT_ORDER = "INSERT INTO customer_order (state, order_date, payment_method, client) VALUES (?,?,?,?)";
    // ? int id; int amount; Product product; float line_price;
    private static final String INSERT_ORDER_LINE = "INSERT INTO order_line (amount, product, line_price) VALUES (?,?,?)";

    private static final String UPDATE_ORDER = "UPDATE customer_order SET customer_order.order_date=?, customer_order.payment_method=?, customer_order.client=? WHERE customer_order.id=?";
    private static final String UPDATE_ORDER_LINE = "UPDATE order_line SET order_line.amount=?, order_line.product=?, order_line.line_price=? WHERE order_line.id=?";

    private static final String DELETE_ORDER = "DELETE FROM customer_order WHERE customer_order.ID = ?";
    private static final String DELETE_ORDER_LINE = "DELETE FROM order_line WHERE order_line.ID = ?";

    private static final String SELECT_ORDER = "SELECT customer_order.id, state, customer_order.order_date, customer_order.total_price, customer_order.payment_method, customer_order.client FROM customer_order WHERE customer_order.ID=?";
    private static final String SELECT_ORDER_BY_CLIENT = "SELECT customer_order.id, customer_order.order_date, customer_order.state, customer_order.payment_method, customer_order.client FROM customer_order WHERE customer_order.client=?";
    private static final String SELECT_ORDER_BY_STATE = "SELECT customer_order.id, customer_order.order_date, customer_order.state, customer_order.payment_method, customer_order.client FROM customer_order WHERE customer_order.state=? and customer_order.client=?";
    private static final String SELECT_ORDER_LINE = "SELECT order_line.id, order_line.amount, order_line.product, order_line.line_price FROM order_line WHERE order_line.ID=?";
    private static final String SELECT_ORDER_LINE_BY_ORDER = "SELECT order_line.id, order_line.amount, order_line.product, order_line.line_price FROM order_line WHERE order_line.ID=? AND order_line.order=customer_order.id";

    // * "INSERT INTO customer_order (state, order_date, payment_method, client) VALUES (?,?,?,?)"
    @Override
    public void saveOrder(Order order) throws SQLException {
        try (Connection conn = getConnection(DatabaseConf.URL, DatabaseConf.USER, DatabaseConf.PASSWORD);
             PreparedStatement stmtOrder = conn.prepareStatement(INSERT_ORDER, Statement.RETURN_GENERATED_KEYS)) {
            List<OrderLine> orderLines = new ArrayList<>();

            stmtOrder.setString(1, order.getState().toString());
            stmtOrder.setDate(2, new Date(order.getOrderDate().getTime()));
            stmtOrder.setString(3, order.getPaymentMethod().toString());
            stmtOrder.setInt(4, order.getClient().getId());

            stmtOrder.executeUpdate();

            try (ResultSet generatedKeys = stmtOrder.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    order.setId(generatedKeys.getInt(1));
                }
            }

            System.out.println("The Order: " + order.getId() + " has been created");
        }
    }

    // * "INSERT INTO order_line (amount, product, line_price) VALUES (?,?,?)"
    @Override
    public void saveOrderLine(OrderLine orderLine, Order order) throws SQLException {
        try (Connection conn = getConnection(DatabaseConf.URL, DatabaseConf.USER, DatabaseConf.PASSWORD);
             PreparedStatement stmtOrder = conn.prepareStatement(INSERT_ORDER_LINE, Statement.RETURN_GENERATED_KEYS)) {
            List<OrderLine> orderLines = new ArrayList<>();

            stmtOrder.setInt(1, orderLine.getAmount());
            stmtOrder.setInt(2, order.getId());
            stmtOrder.setDouble(3, orderLine.getProducto().getPrice() * orderLine.getAmount());

            stmtOrder.executeUpdate();

            try (ResultSet generatedKeys = stmtOrder.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    order.setId(generatedKeys.getInt(1));
                }
            }

            System.out.println("The OrderLine: " + order.getId() + " has been created");
        }
    }

    // * "UPDATE customer_order SET customer_order.order_date=?, customer_order.payment_method=?WHERE customer_order.id=?"
    @Override
    public void updateOrder(Order order) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DatabaseConf.URL, DatabaseConf.USER, DatabaseConf.PASSWORD);
             PreparedStatement stmtOrder = conn.prepareStatement(UPDATE_ORDER)) {
            stmtOrder.setDate(1, (Date) order.getOrderDate());
            stmtOrder.setString(2, order.getPaymentMethod().toString());
            stmtOrder.setInt(3, order.getId());

            stmtOrder.execute();
            System.out.println("The order: " + order.getId() + " has been modified");
        }
    }

    // * "UPDATE order_line SET order_line.amount=?, order_line.product=?, order_line.line_price=? WHERE order_line.id=?"
    @Override
    public void updateOrderLine(OrderLine orderLine) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DatabaseConf.URL, DatabaseConf.USER, DatabaseConf.PASSWORD);
             PreparedStatement stmtOrderLine = conn.prepareStatement(UPDATE_ORDER_LINE)) {
            stmtOrderLine.setInt(1, orderLine.getAmount());
            stmtOrderLine.setInt(2, orderLine.getProducto().getId());
            stmtOrderLine.setDouble(3, orderLine.getProducto().getPrice() * orderLine.getAmount());

            stmtOrderLine.execute();
            System.out.println("The orderLine: " + orderLine.getId() + " has been modified");
        }
    }

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

    // * "SELECT customer_order.id, state, customer_order.order_date, customer_order.total_price, customer_order.payment_method, customer_order.client FROM customer_order WHERE customer_order.ID=?"
    @Override
    public Order selectOrder(int id) throws SQLException {
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
                            jdbcClientDao.findById(rsOrder.getInt("client"))
                    );
                    order.setOrderLines(selectOrderLinesByOrder(order));
                } else {
                    order = null;
                }
            }
            return order;
        }
    }

    // * "SELECT customer_order.id, customer_order.order_date, customer_order.state, customer_order.payment_method, customer_order.client FROM customer_order WHERE customer_order.client=?"
    @Override
    public Order selectOrderByClient(Client client) throws SQLException {
        Order order;
        try (Connection conn = getConnection(DatabaseConf.URL, DatabaseConf.USER, DatabaseConf.PASSWORD);
             PreparedStatement stmtOrder = conn.prepareStatement(SELECT_ORDER_BY_CLIENT)) {
            stmtOrder.setInt(1, client.getId());
            try (ResultSet rsOrder = stmtOrder.executeQuery()) {
                if (rsOrder.next()) {
                    order = new Order(
                            rsOrder.getInt("id"),
                            rsOrder.getDate("order_date"),
                            OrderState.valueOf(rsOrder.getString("state")),
                            PaymentMethod.valueOf(rsOrder.getString("payment_method")),
                            jdbcClientDao.findById(rsOrder.getInt("client"))
                    );
                    order.setOrderLines(selectOrderLinesByOrder(order));
                } else {
                    order = null;
                }
            }
            return order;
        }
    }

    // * "SELECT customer_order.id, customer_order.order_date, customer_order.state, customer_order.payment_method, customer_order.client FROM customer_order WHERE customer_order.state=? and customer_order.client=?"
    @Override
    public Order selectOrderByState(OrderState state, Client client) throws SQLException {
        Order order;
        try (Connection conn = getConnection(DatabaseConf.URL, DatabaseConf.USER, DatabaseConf.PASSWORD);
             PreparedStatement stmtOrder = conn.prepareStatement(SELECT_ORDER_BY_STATE)) {
            stmtOrder.setInt(1, client.getId());
            stmtOrder.setString(2, state.toString());
            try (ResultSet rsOrder = stmtOrder.executeQuery()) {
                if (rsOrder.next()) {
                    order = new Order(
                            rsOrder.getInt("id"),
                            rsOrder.getDate("order_date"),
                            OrderState.valueOf(rsOrder.getString("state")),
                            PaymentMethod.valueOf(rsOrder.getString("payment_method")),
                            jdbcClientDao.findById(rsOrder.getInt("client"))
                    );
                    order.setOrderLines(selectOrderLinesByOrder(order));
                } else {
                    order = null;
                }
            }
            return order;
        }
    }

    // * "SELECT order_line.id, order_line.amount, order_line.product, order_line.line_price FROM client WHERE order_line.ID=?"
    @Override
    public OrderLine selectOrderLine(int id) throws SQLException {
        OrderLine orderLine;
        try (Connection conn = getConnection(DatabaseConf.URL, DatabaseConf.USER, DatabaseConf.PASSWORD);
             PreparedStatement stmtOrderLine = conn.prepareStatement(SELECT_ORDER_LINE)) {
            stmtOrderLine.setInt(1, id);
            try (ResultSet rsOrder = stmtOrderLine.executeQuery()) {
                if (rsOrder.next()) {
                    orderLine = new OrderLine(
                            rsOrder.getInt("id"),
                            rsOrder.getInt("amount"),
                            rsOrder.getFloat("line_price")

                    );
                    orderLine.setProducto(jdbcProductDao.findProductById(rsOrder.getInt("product")));
                } else {
                    orderLine = null;
                }
            }
            return orderLine;
        }
    }

    // * "SELECT order_line.id, order_line.amount, order_line.product, order_line.line_price FROM order_line WHERE order_line.ID=? AND order_line.order=customer_order.id"
    @Override
    public List<OrderLine> selectOrderLinesByOrder(Order order) throws SQLException {
        OrderLine orderLine;
        List<OrderLine> orderLines = new ArrayList<>();
        try (Connection conn = getConnection(DatabaseConf.URL, DatabaseConf.USER, DatabaseConf.PASSWORD);
             PreparedStatement stmtOrder = conn.prepareStatement(SELECT_ORDER_LINE_BY_ORDER)) {
            stmtOrder.setInt(1, order.getId());
            try (ResultSet rsOrder = stmtOrder.executeQuery()) {
                while (rsOrder.next()) {
                    orderLine = new OrderLine(
                            rsOrder.getInt("id"),
                            rsOrder.getInt("amount"),
                            rsOrder.getFloat("line_price")
                    );
                    orderLine.setProducto(jdbcProductDao.findProductById(rsOrder.getInt("product")));
                    orderLines.add(orderLine);
                }
            }
            return orderLines;
        }
    }
}
