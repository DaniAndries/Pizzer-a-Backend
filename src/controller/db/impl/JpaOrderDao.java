package controller.db.impl;

import controller.db.OrderDao;
import controller.db.ProductDao;
import model.Client;
import model.Order;
import model.OrderLine;
import model.OrderState;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class JpaOrderDao implements OrderDao {
    @Override
    public void saveOrder(Order order) throws SQLException {

    }

    @Override
    public void saveOrderLine(OrderLine orderLine, Order order) throws SQLException {

    }

    @Override
    public void saveOrderLine(OrderLine orderLine, Order order, Connection conn) throws SQLException {

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
    public Order findtOrder(int id) throws SQLException {
        return null;
    }

    @Override
    public List<Order> findOrdersByClient(Client client) throws SQLException {
        return List.of();
    }

    @Override
    public List<Order> findOrdersByState(OrderState state, Client client) throws SQLException {
        return List.of();
    }

    @Override
    public OrderLine findOrderLine(int id) throws SQLException {
        return null;
    }

    @Override
    public List<OrderLine> findOrderLinesByOrder(Order order) throws SQLException {
        return List.of();
    }
}
