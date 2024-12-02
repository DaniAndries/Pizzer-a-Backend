package controller.db;

import model.*;

import java.sql.SQLException;
import java.util.List;

public interface OrderDao {

    void saveOrder(Order order) throws SQLException;

    void saveOrderLine(OrderLine orderLine, Order order) throws SQLException;

    void deleteOrder(Order order) throws SQLException;

    void deleteOrderLine(OrderLine orderLine) throws SQLException;

    void updateOrder(Order order) throws SQLException;

    void updateOrderLine(OrderLine orderLine) throws SQLException;

    Order selectOrder(int id) throws SQLException;

    List<Order> selectOrdersByClient(Client client) throws SQLException;

    Order selectOrderByState(OrderState state , Client client) throws SQLException;

    OrderLine selectOrderLine(int id) throws SQLException;

    List<OrderLine> selectOrderLinesByOrder(Order order) throws SQLException;
}
