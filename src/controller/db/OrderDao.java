package controller.db;

import model.*;

import java.sql.SQLException;

public interface OrderDao {

    void saveOrder(Order order) throws SQLException;

    void saveOrderLine(OrderLine orderLine, Order order) throws SQLException;

    void deleteOrder(Order order) throws SQLException;

    void deleteOrderLine(OrderLine orderLine) throws SQLException;

    void updateOrder(Order order) throws SQLException;

    void updateOrderLine(OrderLine orderLine) throws SQLException;

    Order selectOrder(int id) throws SQLException;

    Order selectOrderByState(OrderState state , Client client) throws SQLException;

    Order selectOrderLine(int id) throws SQLException;
}
