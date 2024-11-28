package controller;

import controller.db.impl.JdbcClientDao;
import controller.db.impl.JdbcOrderDao;
import model.*;

import java.sql.SQLException;
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

    public Order selectOrderByClient(Client client) throws SQLException {
        return orderDao.selectOrderByClient(client);
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

        if (order.getOrderDate() == null) {
            orderDao.saveOrder(new Order(new Date(), OrderState.PENDING, orderLine, clienteActual));
        } else {
            order.addOrderLine(orderLine);
            orderDao.updateOrder(order);
        }

        orderDao.saveOrderLine(orderLine, order);
    }

    public void finalizeOrder(Client client, PaymentMethod paymentMethod) throws SQLException {
        Order actualOrder = orderDao.selectOrderByState(OrderState.PENDING, client);
        Payable payable;

        if (paymentMethod.toString().equals("CARD")){
            payable = new PayByCard();
            actualOrder.setPaymentMethod(PaymentMethod.CARD);
        } else if (paymentMethod.toString().equals("CASH")){
            payable = new PayByCash();
            actualOrder.setPaymentMethod(PaymentMethod.CARD);
        } else {
            throw new IllegalArgumentException("You cannot finalize an order without a payment method");
        }

        if (this.actualOrder.getState() == OrderState.PENDING) {
            this.actualOrder.finalizar(payable, paymentMethod);
            client.getOrderList().add(actualOrder);
            actualOrder.setState(OrderState.PAID);
            orderDao.updateOrder(actualOrder);
            clientDao.update(client);
        } else{
            throw new IllegalArgumentException("You cannot finalize an Order that has already been completed, delivered or canceled");
        }
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
