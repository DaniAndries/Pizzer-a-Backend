package controller.db.impl;

import controller.db.OrderDao;
import model.*;
import utils.DatabaseConf;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.sql.DriverManager.getConnection;

public class JdbcOrderDao implements OrderDao {
    // ? int id; int amount; Product product; float linePrice;
    private static final String INSERT_ORDER = "INSERT INTO customer_order (state, order_date, total_price, payment_method, client) VALUES (?,?,?,?,?)";

    // * int id; OrderState state; Date orderDate; float totalPrice; String paymentMethod; List<OrderLine> orderLines; Client client;
    private static final String INSERT_ORDER_LINE = "INSERT INTO order_line (amount, product, linePrice) VALUES (?,?,?)";

    private static final String UPDATE_ORDER = "UPDATE customer_order SET customer_order.order_date=?, customer_order.total_price=?, customer_order.payment_method=?, customer_order.client=? WHERE customer_order.id=?";
    private static final String UPDATE_ORDER_LINE = "UPDATE order_line SET order_line.amount=?, order_line.product=?, order_line.linePrice=? WHERE order_line.id=?";

    private static final String DELETE_ORDER = "DELETE FROM customer_order WHERE customer_order.ID = ?";
    private static final String DELETE_ORDER_LINE = "DELETE FROM order_line WHERE order_line.ID = ?";

    private static final String SELECT_ORDER = "SELECT customer_order.id, customer_order.order_date, customer_order.total_price, customer_order.payment_method, customer_order.client FROM customer_order WHERE customer_order.ID=?";
    private static final String SELECT_PENDING_ORDER = "SELECT customer_order.id, customer_order.order_date, customer_order.state, customer_order.payment_method, customer_order.total_price, customer_order.client FROM customer_order WHERE customer_order.state=? and customer_order.client=?";
    private static final String SELECT_ORDER_LINE = "SELECT order_line.id, order_line.amount, order_line.product, order_line.linePrice FROM client WHERE order_line.ID=?";
    JdbcClientDao jdbcClientDao = new JdbcClientDao();

    // * "INSERT INTO customer_order (state, order_date, payment_method, client) VALUES (?,?,?,?,?)"
    @Override
    public void saveOrder(Order order) throws SQLException {
        try (Connection conn = getConnection(DatabaseConf.URL, DatabaseConf.USER, DatabaseConf.PASSWORD);
             PreparedStatement stmtOrder = conn.prepareStatement(INSERT_ORDER, Statement.RETURN_GENERATED_KEYS)) {
            List<OrderLine> orderLines = new ArrayList<>();

            stmtOrder.setString(1, order.getState().toString());
            stmtOrder.setDate(2, (Date) order.getOrderDate());
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

    // * "INSERT INTO order_line (amount, product, linePrice) VALUES (?,?,?)"
    @Override
    public void saveOrderLine(OrderLine orderLine, Order order) throws SQLException {
        try (Connection conn = getConnection(DatabaseConf.URL, DatabaseConf.USER, DatabaseConf.PASSWORD);
             PreparedStatement stmtOrder = conn.prepareStatement(INSERT_ORDER, Statement.RETURN_GENERATED_KEYS)) {
            List<OrderLine> orderLines = new ArrayList<>();

            stmtOrder.setString(1, order.getState().toString());
            stmtOrder.setDate(2, (Date) order.getOrderDate());
            stmtOrder.setDouble(3, order.getTotalPrice());
            stmtOrder.setString(4, order.getPaymentMethod().toString());
            stmtOrder.setInt(5, order.getClient().getId());

            stmtOrder.executeUpdate();

            try (ResultSet generatedKeys = stmtOrder.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    order.setId(generatedKeys.getInt(1));
                }
            }

            System.out.println("The Order: " + order.getId() + " has been created");
        }
    }


    @Override
    public void deleteOrder(Order order) throws SQLException {

    }

    @Override
    public void deleteOrderLine(OrderLine orderLine) throws SQLException {

    }

    @Override
    public void updateOrder(Order order) throws SQLException {

    }

    @Override
    public void updateOrderLine(OrderLine orderLine) throws SQLException {

    }

    @Override
    public Order selectOrder(int id) throws SQLException {
        return null;
    }

    // * "SELECT customer_order.id, customer_order.order_date, customer_order.state, customer_order.payment_method, customer_order.total_price, customer_order.client FROM customer_order WHERE customer_order.state=? and customer_order.client=?"
    @Override
    public Order selectOrderByState(OrderState state ,Client client) throws SQLException {
        Order order;
        try (Connection conn = getConnection(DatabaseConf.URL, DatabaseConf.USER, DatabaseConf.PASSWORD);
             PreparedStatement stmtOrder = conn.prepareStatement(SELECT_PENDING_ORDER)) {
            stmtOrder.setInt(1, client.getId());
            stmtOrder.setString(2, state.toString());
            try (ResultSet rsOrder = stmtOrder.executeQuery()) {
                if (rsOrder.next()) {
                    order = new Order(
                            rsOrder.getInt("id"),
                            rsOrder.getDate("order_date"),
                            OrderState.valueOf(rsOrder.getString("state")),
                            PaymentMethod.valueOf(rsOrder.getString("payment_method")),
                            rsOrder.getDouble("total_price"),
                            jdbcClientDao.findById(rsOrder.getInt("client"))
                    );
                } else {
                    order = null;
                }
            }
            return order;
        }
    }

    @Override
    public Order selectOrderLine(int id) throws SQLException {
        return null;
    }
}
