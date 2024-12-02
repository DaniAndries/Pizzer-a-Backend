package controller;

import controller.db.impl.JdbcClientDao;
import controller.db.impl.JdbcOrderDao;
import model.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderController {
    JdbcOrderDao orderDao = new JdbcOrderDao();
    JdbcClientDao clientDao = new JdbcClientDao();

    private Order actualOrder;

    public OrderController(Order actualOrder) {
        this.actualOrder = actualOrder;
    }

    public OrderController() {
    }

    public void saveOrder(Order order) throws SQLException {
        if (order == null || order.getClient() == null || order.getOrderDate() == null || order.getState() == null || order.getPaymentMethod() == null) {
            throw new IllegalArgumentException("Invalid order or missing fields.");
        }
        orderDao.saveOrder(order);
    }

    public void saveOrderLine(OrderLine orderLine, Order order) throws SQLException {
        orderDao.saveOrderLine(orderLine, order);
    }

    public void updateOrder(Order order) throws SQLException {
        orderDao.updateOrder(order);
    }

    public void updateOrderLine(OrderLine orderLine) throws SQLException {
        orderDao.updateOrderLine(orderLine);
    }

    public void deleteOrder(Order order) throws SQLException {
        orderDao.deleteOrder(order);
    }

    public void deleteOrderLine(OrderLine orderLine) throws SQLException {
        orderDao.deleteOrderLine(orderLine);
    }

    public Order selectOrder(int id) throws SQLException {
        return orderDao.selectOrder(id);
    }

    public List<Order> selectOrdersByClient(Client client) throws SQLException {
        if (client == null) {
            throw new IllegalArgumentException("Client cannot be null");
        }
        return orderDao.selectOrdersByClient(client);
    }

    public Order selectOrderByState(OrderState state, Client client) throws SQLException {
        return orderDao.selectOrderByState(state, client);
    }

    public OrderLine selectOrderLine(int id) throws SQLException {
        return orderDao.selectOrderLine(id);
    }

    public List<OrderLine> selectOrderLinesByOrder(Order order) throws SQLException {
        return orderDao.selectOrderLinesByOrder(order);
    }

    public void addToCart(Product product, Client clienteActual, int cantidad) throws IllegalStateException, SQLException {
        Order order = orderDao.selectOrderByState(OrderState.PENDING, clienteActual);
        OrderLine orderLine = new OrderLine(cantidad, product);

        if (order == null) {
            order = new Order(new Date(), OrderState.PENDING, orderLine, clienteActual);
            orderDao.saveOrder(order);
        } else {
            orderDao.updateOrder(order);
            order.addOrderLine(orderLine);
            orderDao.saveOrderLine(orderLine, order);
        }
    }

    public void finalizeOrder(Client client, PaymentMethod paymentMethod) throws SQLException {
        Order newOrder = orderDao.selectOrderByState(OrderState.PENDING, client);

        if (newOrder == null) {
            throw new IllegalArgumentException("No pending order found for the client to finalize.");
        }

        Payable payable = getPayableForMethod(paymentMethod);
        newOrder.setPaymentMethod(paymentMethod);
        newOrder.finalizeOrder(payable, paymentMethod);
        newOrder.setState(OrderState.FINISHED);

        orderDao.updateOrder(newOrder);
        addOrderToClient(client, newOrder);
    }

    private Payable getPayableForMethod(PaymentMethod paymentMethod) {
        if (paymentMethod == PaymentMethod.CARD) {
            return new PayByCard();
        } else if (paymentMethod == PaymentMethod.CASH) {
            return new PayByCash();
        } else {
            throw new IllegalArgumentException("Invalid payment method: " + paymentMethod);
        }
    }

    private void addOrderToClient(Client client, Order newOrder) throws SQLException {
        if (client.getOrderList() == null) {
            client.setOrderList(new ArrayList<>());
        }
        client.getOrderList().add(newOrder);
        clientDao.update(client);
    }

    public void deliverOrder(int id) throws SQLException {
        Order actualOrder = orderDao.selectOrder(id);
        actualOrder.setState(OrderState.DELIVERED);
        orderDao.updateOrder(actualOrder);
    }

    public void cancelOrder(Client client) throws SQLException {
        Order actualOrder = orderDao.selectOrderByState(OrderState.PENDING, client);
        actualOrder.setState(OrderState.CANCELED);
        orderDao.updateOrder(actualOrder);
    }

}
