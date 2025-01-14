package controller;

import controller.db.impl.JdbcClientDao;
import controller.db.impl.JdbcOrderDao;
import controller.db.impl.JpaClientDao;
import controller.db.impl.JpaOrderDao;
import model.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The OrderController class handles operations related to orders and order lines.
 * It provides methods for creating, updating, deleting, and retrieving orders,
 * as well as managing the relationship between clients and their orders.
 *
 * @author DaniAndries
 * @version 0.1
 */
public class OrderController {
    private JpaOrderDao orderDao = new JpaOrderDao(); // DAO for order-related database operations
    private JpaClientDao clientDao = new JpaClientDao(); // DAO for client-related database operations

    private Order actualOrder; // Currently active order (if any)

    /**
     * Constructs a new OrderController instance with the specified active order.
     *
     * @param actualOrder the order to be set as the current active order
     */
    public OrderController(Order actualOrder) {
        this.actualOrder = actualOrder;
    }

    /**
     * Constructs a new OrderController instance with no active order.
     */
    public OrderController() {
    }

    /**
     * Saves a new order to the database.
     *
     * @param order the order to be saved
     * @throws SQLException             if a database access error occurs
     * @throws IllegalArgumentException if the order is null or has missing required fields
     */
    public void saveOrder(Order order) throws SQLException {
        if (order == null || order.getClient() == null || order.getOrderDate() == null || order.getState() == null || order.getPaymentMethod() == null) {
            throw new IllegalArgumentException("Invalid order or missing fields.");
        }
        orderDao.saveOrder(order);
    }

    /**
     * Saves a line item for an order in the database.
     *
     * @param orderLine the order line to be saved
     * @param order     the order to which the line item belongs
     * @throws SQLException if a database access error occurs
     */
    public void saveOrderLine(OrderLine orderLine, Order order) throws SQLException {
        orderDao.saveOrderLine(orderLine, order);
    }

    /**
     * Updates an existing order in the database.
     *
     * @param order the order with updated information
     * @throws SQLException if a database access error occurs
     */
    public void updateOrder(Order order) throws SQLException {
        orderDao.updateOrder(order);
    }

    /**
     * Updates an existing order line in the database.
     *
     * @param orderLine the order line with updated information
     * @throws SQLException if a database access error occurs
     */
    public void updateOrderLine(OrderLine orderLine) throws SQLException {
        orderDao.updateOrderLine(orderLine);
    }

    /**
     * Deletes a specified order from the database.
     *
     * @param order the order to be deleted
     * @throws SQLException if a database access error occurs
     */
    public void deleteOrder(Order order) throws SQLException {
        orderDao.deleteOrder(order);
    }

    /**
     * Deletes a specified order line from the database.
     *
     * @param orderLine the order line to be deleted
     * @throws SQLException if a database access error occurs
     */
    public void deleteOrderLine(OrderLine orderLine) throws SQLException {
        orderDao.deleteOrderLine(orderLine);
    }

    /**
     * Retrieves an order by its ID from the database.
     *
     * @param id the ID of the order to be retrieved
     * @return the order associated with the given ID, or null if not found
     * @throws SQLException if a database access error occurs
     */
    public Order selectOrder(int id) throws SQLException {
        return orderDao.findtOrder(id);
    }

    /**
     * Retrieves a list of orders associated with a specific client.
     *
     * @param client the client whose orders are to be retrieved
     * @return a list of orders associated with the client
     * @throws SQLException             if a database access error occurs
     * @throws IllegalArgumentException if the client is null
     */
    public List<Order> selectOrdersByClient(Client client) throws SQLException {
        if (client == null) {
            throw new IllegalArgumentException("Client cannot be null");
        }
        return orderDao.findOrdersByClient(client);
    }

    /**
     * Retrieves an order by its state for a specific client.
     *
     * @param state  the state of the order to be retrieved
     * @param client the client associated with the order
     * @return the order associated with the specified state and client, or null if not found
     * @throws SQLException if a database access error occurs
     */
    public List<Order> selectOrdersByState(OrderState state, Client client) throws SQLException {
        return orderDao.findOrdersByState(state, client);
    }

    /**
     * Retrieves an order line by its ID from the database.
     *
     * @param id the ID of the order line to be retrieved
     * @return the order line associated with the given ID, or null if not found
     * @throws SQLException if a database access error occurs
     */
    public OrderLine selectOrderLine(int id) throws SQLException {
        return orderDao.findOrderLine(id);
    }

    /**
     * Retrieves a list of order lines associated with a specific order.
     *
     * @param order the order whose lines are to be retrieved
     * @return a list of order lines associated with the specified order
     * @throws SQLException if a database access error occurs
     */
    public List<OrderLine> selectOrderLinesByOrder(Order order) throws SQLException {
        return orderDao.findOrderLinesByOrder(order);
    }

    /**
     * Adds a product to the client's cart. If no pending order exists, a new order is created.
     *
     * @param product       the product to be added to the cart
     * @param clienteActual the client who is adding the product
     * @param cantidad      the quantity of the product to be added
     * @throws IllegalStateException if there is an issue with the current state of the order
     * @throws SQLException          if a database access error occurs
     */
    public void addToCart(Product product, Client clienteActual, int cantidad) throws IllegalStateException, SQLException {
        List<Order> orders = orderDao.findOrdersByState(OrderState.PENDING, clienteActual);
        OrderLine orderLine = new OrderLine(cantidad, product);
        Order order;

        if (orders == null || orders.isEmpty()) {
            order = new Order(new Date(), OrderState.PENDING, orderLine, clienteActual);
            orderDao.saveOrder(order);
        } else {
            order = orders.getFirst();
            order.addOrderLine(orderLine);
            orderDao.updateOrder(order);
            orderDao.saveOrderLine(orderLine, order);
        }
    }

    /**
     * Finalizes an order for a client and sets its state to FINISHED.
     *
     * @param client        the client who is finalizing the order
     * @param paymentMethod the payment method used for the order
     * @throws SQLException             if a database access error occurs
     * @throws IllegalArgumentException if no pending order is found for the client
     */
    public void finalizeOrder(Client client, PaymentMethod paymentMethod) throws SQLException {
        List<Order> orders = orderDao.findOrdersByState(OrderState.PENDING, client);
        Order newOrder;

        if (orders == null || orders.isEmpty()) {
            throw new IllegalArgumentException("No pending order found for the client to finalize.");
        }

        newOrder = orders.getFirst();

        Payable payable = getPayableForMethod(paymentMethod);
        newOrder.setPaymentMethod(paymentMethod);
        newOrder.finalizeOrder(payable, paymentMethod);
        newOrder.setState(OrderState.FINISHED);

        orderDao.updateOrder(newOrder);
        addOrderToClient(client, newOrder);
    }

    /**
     * Retrieves the appropriate payment handler for a specified payment method.
     *
     * @param paymentMethod the payment method to retrieve the handler for
     * @return a Payable instance corresponding to the payment method
     * @throws IllegalArgumentException if the payment method is invalid
     */
    private Payable getPayableForMethod(PaymentMethod paymentMethod) {
        if (paymentMethod == PaymentMethod.CARD) {
            return new PayByCard();
        } else if (paymentMethod == PaymentMethod.CASH) {
            return new PayByCash();
        } else {
            throw new IllegalArgumentException("Invalid payment method: " + paymentMethod);
        }
    }

    /**
     * Adds a finalized order to the client's order list and updates the client in the database.
     *
     * @param client   the client to whom the order will be added
     * @param newOrder the new order to be added to the client's order list
     * @throws SQLException if a database access error occurs
     */
    private void addOrderToClient(Client client, Order newOrder) throws SQLException {
        List<Order> orderList = client.getOrderList();

        if (orderList == null) {
            // Initialize the list if null
            orderList = new ArrayList<>();
            client.setOrderList(orderList);
        } else {
            // Check if the list is mutable
            try {
                orderList.add(newOrder); // Attempt to add the new order
            } catch (UnsupportedOperationException e) {
                // If not modifiable, create a new mutable list
                orderList = new ArrayList<>(orderList);
                orderList.add(newOrder); // Add the new order
                client.setOrderList(orderList); // Update the client with the new list
            }
        }

        // Update the client in the database
        clientDao.update(client);
    }

    /**
     * Marks an order as delivered in the database.
     *
     * @param id the ID of the order to be delivered
     * @throws SQLException if a database access error occurs
     */
    public void deliverOrder(int id) throws SQLException {
        Order actualOrder = orderDao.findtOrder(id);
        actualOrder.setState(OrderState.DELIVERED);
        orderDao.updateOrder(actualOrder);
    }

    /**
     * Cancels a pending order for a specific client.
     *
     * @param client the client whose pending order is to be canceled
     * @throws SQLException if a database access error occurs
     */
    public void cancelOrder(Client client) throws SQLException {
        List<Order> orders = orderDao.findOrdersByState(OrderState.PENDING, client);
        Order actualOrder;

        if (orders != null || !orders.isEmpty()) {
            actualOrder = orders.getFirst();
            actualOrder.setState(OrderState.CANCELED);
            orderDao.updateOrder(actualOrder);
        } else {
            throw new IllegalArgumentException("No pending order found to cancel.");
        }
    }
}
