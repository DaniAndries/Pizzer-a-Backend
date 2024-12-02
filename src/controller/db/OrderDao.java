package controller.db;

import model.Client;
import model.Order;
import model.OrderLine;
import model.OrderState;

import java.sql.SQLException;
import java.util.List;

/**
 * The OrderDao interface provides methods for interacting with the database
 * regarding orders and order lines. It includes methods for saving, deleting,
 * updating, and selecting orders and order lines.
 *
 * @author DaniAndries
 * @version 0.1
 */
public interface OrderDao {

    /**
     * Saves a new order to the database.
     *
     * @param order The Order object to be saved.
     * @throws SQLException If there is a database access error.
     */
    void saveOrder(Order order) throws SQLException;

    /**
     * Saves a new order line associated with an order to the database.
     *
     * @param orderLine The OrderLine object to be saved.
     * @param order The Order object associated with the order line.
     * @throws SQLException If there is a database access error.
     */
    void saveOrderLine(OrderLine orderLine, Order order) throws SQLException;

    /**
     * Deletes an existing order from the database.
     *
     * @param order The Order object to be deleted.
     * @throws SQLException If there is a database access error.
     */
    void deleteOrder(Order order) throws SQLException;

    /**
     * Deletes an existing order line from the database.
     *
     * @param orderLine The OrderLine object to be deleted.
     * @throws SQLException If there is a database access error.
     */
    void deleteOrderLine(OrderLine orderLine) throws SQLException;

    /**
     * Updates an existing order in the database.
     *
     * @param order The Order object with updated information.
     * @throws SQLException If there is a database access error.
     */
    void updateOrder(Order order) throws SQLException;

    /**
     * Updates an existing order line in the database.
     *
     * @param orderLine The OrderLine object with updated information.
     * @throws SQLException If there is a database access error.
     */
    void updateOrderLine(OrderLine orderLine) throws SQLException;

    /**
     * Retrieves an order by its ID.
     *
     * @param id The ID of the order to be retrieved.
     * @return The Order object with the specified ID, or null if not found.
     * @throws SQLException If there is a database access error.
     */
    Order findtOrder(int id) throws SQLException;

    /**
     * Retrieves a list of orders associated with a specific client.
     *
     * @param client The Client whose orders are to be retrieved.
     * @return A list of Order objects associated with the client.
     * @throws SQLException If there is a database access error.
     */
    List<Order> findOrdersByClient(Client client) throws SQLException;

    /**
     * Retrieves an order by its state and associated client.
     *
     * @param state The OrderState to filter by.
     * @param client The Client associated with the order.
     * @return The Order object matching the specified state and client, or null if not found.
     * @throws SQLException If there is a database access error.
     */
    Order findOrderByState(OrderState state, Client client) throws SQLException;

    /**
     * Retrieves an order line by its ID.
     *
     * @param id The ID of the order line to be retrieved.
     * @return The OrderLine object with the specified ID, or null if not found.
     * @throws SQLException If there is a database access error.
     */
    OrderLine findOrderLine(int id) throws SQLException;

    /**
     * Retrieves a list of order lines associated with a specific order.
     *
     * @param order The Order whose order lines are to be retrieved.
     * @return A list of OrderLine objects associated with the order.
     * @throws SQLException If there is a database access error.
     */
    List<OrderLine> findOrderLinesByOrder(Order order) throws SQLException;
}
