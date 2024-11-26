package controller.db;

import model.Order;

import java.sql.Connection;
import java.sql.SQLException;

public interface OrderDao {

    void saveOrder(Order order) throws SQLException;

    void deleteOrder(Order order) throws SQLException;

    void updateProduct(Order order) throws SQLException;

    Order selectOrder(int id) throws SQLException;
}
