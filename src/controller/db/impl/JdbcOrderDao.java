package controller.db.impl;

import controller.db.OrderDao;
import model.*;
import utils.DatabaseConf;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.sql.DriverManager.getConnection;

public class JdbcOrderDao implements OrderDao {

    // int id; OrderState state; Date orderDate; float totalPrice; String paymentMethod; List<OrderLine> orderLines; Client client;
    private static final String INSERT_ORDER = "INSERT INTO customer_order (state, order_date, total_price, payment_method, client) VALUES (?,?,?,?,?)";

    // * "INSERT INTO customer_order (state, order_date, total_price, payment_method, client) VALUES (?,?,?,?,?)"
    @Override
    public void saveOrder(Order order) throws SQLException {
        try (Connection conn = getConnection(DatabaseConf.URL, DatabaseConf.USER, DatabaseConf.PASSWORD);
             PreparedStatement stmtOrder = conn.prepareStatement(INSERT_ORDER, Statement.RETURN_GENERATED_KEYS)) {
            List<OrderLine> orderLines = new ArrayList<>();

            stmtOrder.setString(1, order.getState().toString());
            stmtOrder.setDate(2, (Date) order.getOrderDate());
            stmtOrder.setDouble(3, order.getTotalPrice());
            stmtOrder.setString(4, order.getPaymentMethod());
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
    public void updateProduct(Order order) throws SQLException {

    }

    @Override
    public Order selectOrder(int id) throws SQLException {
        return null;
    }
}
