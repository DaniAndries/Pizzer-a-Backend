package test.java;

import controller.ClientController;
import controller.OrderController;
import controller.ProductController;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.DatabaseConf;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class JpaOrderController {

    // Controllers
    private OrderController orderController;
    private ClientController clientController;
    private ProductController productController;
    // Clients
    Client client1 = new Client("12345678A", "Juan Pérez", "Calle Falsa 123, Madrid", "600123456", "juan.perez@example.com", "password123", false);
    Client client2 = new Client("87654321B", "Ana Gómez", "Av. Siempre Viva 742, Barcelona", "650654321", "ana.gomez@example.com", "securepass456", false);
    // List of OrderLines
    List<OrderLine> orderLines1 = new ArrayList<>();
    List<OrderLine> orderLines2 = new ArrayList<>();
    // Ingredients
    private Ingredient cheese = new Ingredient(1, "Cheese", List.of("Lactose"));
    private Ingredient tomato = new Ingredient(2, "Tomato", new ArrayList<>());
    private Ingredient pepper = new Ingredient(3, "Pepper", new ArrayList<>());
    private Ingredient bacon = new Ingredient(4, "Bacon", List.of("Sulfites"));
    private Ingredient mushroom = new Ingredient(5, "Mushroom", new ArrayList<>());
    // Lists of ingredients
    private List<Ingredient> ingredientList1 = List.of(cheese, tomato);
    private List<Ingredient> ingredientList2 = List.of(tomato, bacon);
    private List<Ingredient> ingredientList3 = List.of(mushroom, pepper);
    // Products
    private Pasta pasta1 = new Pasta(1, "Carbonara", 10.5, ingredientList1);
    private Pizza pizza1 = new Pizza(4, "Pepperoni", 14.0, ingredientList1, "MEDIUM");
    private Pasta pasta2 = new Pasta(2, "Bolognese", 9.5, ingredientList2);
    private Pasta pasta3 = new Pasta(3, "Pesto", 8.0, ingredientList3);
    private Drink drink1 = new Drink(5, "Coca-Cola", 2.5, Size.SMALL);
    // OrderLines
    private OrderLine orderLine1 = new OrderLine(4, pasta1);
    private OrderLine orderLine3 = new OrderLine(5, pizza1);
    private OrderLine orderLine4 = new OrderLine(2, pasta3);
    private OrderLine orderLine2 = new OrderLine(7, drink1);
    // Orders
    private Order order1 = new Order(1, new Date(), OrderState.PENDING, PaymentMethod.UNPAID, client1);
    private Order order2 = new Order(2, new Date(), OrderState.FINISHED, PaymentMethod.UNPAID, client2);


    /**
     * Default constructor for OrderControllerTest.
     * <p>
     * This constructor initializes a new instance of the OrderControllerTest class.
     * It can be used to create an instance of this class without providing
     * any specific parameters.
     * </p>
     */
    public JpaOrderController() {
        // Empty constructor
    }

    /**
     * Initializes the test environment before each test.
     * This includes resetting the database, initializing controllers, and registering clients.
     *
     * @throws SQLException if a database error occurs during setup.
     */
    @BeforeEach
    void setUp() throws SQLException {
        orderController = new OrderController();
        clientController = new ClientController();
        productController = new ProductController();
        DatabaseConf.dropAndCreateTables();
        clientController.registerClient(client1);
        clientController.registerClient(client2);
    }

    /**
     * Helper method to set up sample data for testing.
     * It populates the database with products, orders, and order lines.
     *
     * @throws SQLException if a database error occurs.
     */
    void setUpHelper() throws SQLException {
        productController.saveProduct(pasta1);
        productController.saveProduct(pasta2);
        productController.saveProduct(pasta3);
        productController.saveProduct(pizza1);
        productController.saveProduct(drink1);

        orderLines1.add(new OrderLine(4, pasta1));
        orderLines1.add(new OrderLine(5, pizza1));
        orderLines2.add(new OrderLine(7, drink1));
        orderLines2.add(new OrderLine(2, pasta3));

        order1.setOrderLines(orderLines1);
        order2.setOrderLines(orderLines2);

        client1.setOrderList(List.of(order1));
        client2.setOrderList(List.of(order2));

        orderController.saveOrder(order1);
        orderController.saveOrder(order2);
    }

    /**
     * Tests saving an order and validates its persistence and correctness.
     *
     * @throws SQLException if a database error occurs.
     */
    @Test
    void testSaveOrder() throws SQLException {
        productController.saveProduct(pasta1);
        orderLines1.add(new OrderLine(4, pasta1));
        order1.setOrderLines(orderLines1);

        orderController.saveOrder(order1);

        Order newOrder = orderController.selectOrder(order1.getId());

        assertNotNull(newOrder, "Retrieved order should not be null.");
        assertEquals(order1.getId(), newOrder.getId(), "Order ID mismatch.");
        assertEquals(order1.getOrderLines().size(), newOrder.getOrderLines().size(), "Order lines count mismatch.");
    }

    /**
     * Tests saving an order without order lines.
     *
     * @throws SQLException if a database error occurs.
     */
    @Test
    void testSaveOrderWithoutOrderLines() throws SQLException {
        order1.setOrderLines(new ArrayList<>());
        orderController.saveOrder(order1);

        Order retrievedOrder = orderController.selectOrder(order1.getId());
        assertNotNull(retrievedOrder);
        assertTrue(retrievedOrder.getOrderLines().isEmpty(), "Order lines should be empty.");
    }

    /**
     * Tests retrieving a non-existent order by ID.
     *
     * @throws SQLException if a database error occurs.
     */
    @Test
    void testSelectNonExistentOrder() throws SQLException {
        Order retrievedOrder = orderController.selectOrder(9999);
        assertNull(retrievedOrder, "Should return null for non-existent order.");
    }

    /**
     * Tests updating an order's state.
     *
     * @throws SQLException if a database error occurs.
     */
    @Test
    void testUpdateOrder() throws SQLException {
        orderController.saveOrder(order1);

        order1.setState(OrderState.DELIVERED);
        orderController.updateOrder(order1);

        Order newOrder = orderController.selectOrder(order1.getId());
        assertEquals(OrderState.DELIVERED, newOrder.getState());
    }

    /**
     * Tests deleting an order and verifies its removal.
     *
     * @throws SQLException if a database error occurs.
     */
    @Test
    void testDeleteOrder() throws SQLException {
        orderController.saveOrder(order1);

        orderController.deleteOrder(order1);

        Order newOrder = orderController.selectOrder(order1.getId());
        assertNull(newOrder, "Order should be null after deletion.");
    }

    /**
     * Tests adding a product to the client's cart.
     *
     * @throws SQLException if a database error occurs.
     */
    @Test
    void testAddToCart() throws SQLException {
        productController.saveProduct(pizza1);

        orderController.addToCart(pizza1, client1, 3);

        List<Order> newOrders = orderController.selectOrdersByClient(client1);

        assertNotNull(newOrders);
        assertFalse(newOrders.isEmpty(), "The orders list should not be empty after adding to cart.");
    }

    /**
     * Tests finalizing an order and validating its state.
     *
     * @throws SQLException if a database error occurs.
     */
    @Test
    void testFinalizeOrder() throws SQLException {
        setUpHelper();

        orderController.finalizeOrder(client1, PaymentMethod.CARD);

        Order finalizedOrder = orderController.selectOrder(order1.getId());
        assertEquals(OrderState.FINISHED, finalizedOrder.getState());
    }

    /**
     * Tests selecting orders by client.
     *
     * @throws SQLException if a database error occurs.
     */
    @Test
    void testselectOrdersByClient() throws SQLException, ParseException {
        setUpHelper();
        List<Order> orders = orderController.selectOrdersByClient(client1);


        assertNotNull(orders, "The orders list should not be null");
        assertFalse(orders.isEmpty(), "The orders list should not be empty");

        Order order = orders.getFirst();

        assertEquals(1, order.getId(), "The order ID should match");
        assertEquals(OrderState.PENDING, order.getState(), "The order state should be PENDING");
        assertEquals(client1, order.getClient(), "The client should match");

        Date orderDate = client1.getOrderList().getFirst().getOrderDate();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = sdf.format(orderDate);
        Date expectedDate = sdf.parse(formattedDate);

        assertEquals(expectedDate, order.getOrderDate(), "The order date should match");

        List<OrderLine> orderLines = order.getOrderLines();

        assertNotNull(orderLines, "Order lines should not be null");
        assertFalse(orderLines.isEmpty(), "Order lines should not be empty");

        OrderLine orderLine = orderLines.getFirst();
        assertEquals(1, orderLine.getId(), "The order line ID should match");
        assertEquals(4, orderLine.getAmount(), "The amount in the order line should be 4");
        assertEquals(client1.getOrderList().getFirst().getOrderLines().getFirst().getProducto(), orderLine.getProducto(), "The product in the order line should match");
    }
    /**
     * Tests selecting orders by their state for a specific client.
     *
     * @throws SQLException if a database error occurs.
     */
    @Test
    void testSelectOrdersByState() throws SQLException {
        setUpHelper();

        List<Order> orders = orderController.selectOrdersByState(OrderState.PENDING, client1);
        Order newOrder = orders.getFirst();

        assertNotNull(newOrder, "The retrieved order should not be null.");
        assertEquals(OrderState.PENDING, newOrder.getState(), "The order state should be PENDING.");
    }

    /**
     * Tests selecting order lines by a specific order.
     *
     * @throws SQLException if a database error occurs.
     */
    @Test
    void testSelectOrderLinesByOrder() throws SQLException {
        setUpHelper();

        List<OrderLine> orderLines = orderController.selectOrderLinesByOrder(order1);

        assertNotNull(orderLines, "Order lines should not be null.");
        assertEquals(order1.getOrderLines().size(), orderLines.size(), "Incorrect number of order lines retrieved.");

        // Validate individual order lines
        assertTrue(orderLines.contains(new OrderLine(4, pasta1)), "Order lines should include the expected pasta1 order line.");
        assertTrue(orderLines.contains(new OrderLine(5, pizza1)), "Order lines should include the expected pizza1 order line.");
    }

    /**
     * Tests selecting order lines for a non-existent order.
     *
     * @throws SQLException if a database error occurs.
     */
    @Test
    void testSelectOrderLinesByNonExistentOrder() throws SQLException {
        List<OrderLine> orderLines = orderController.selectOrderLinesByOrder(new Order(9999, new Date(), OrderState.PENDING, PaymentMethod.UNPAID, client1));

        assertNotNull(orderLines, "Order lines list should not be null.");
        assertTrue(orderLines.isEmpty(), "Order lines list should be empty for a non-existent order.");
    }

    /**
     * Tests canceling an order for a client.
     *
     * @throws SQLException if a database error occurs.
     */
    @Test
    void testCancelOrder() throws SQLException {
        orderController.saveOrder(order1);

        orderController.cancelOrder(client1);

        Order canceledOrder = orderController.selectOrder(order1.getId());
        assertEquals(OrderState.CANCELED, canceledOrder.getState(), "The order state should be CANCELED after cancellation.");
    }

    /**
     * Tests marking an order as delivered.
     *
     * @throws SQLException if a database error occurs.
     */
    @Test
    void testDeliverOrder() throws SQLException {
        orderController.saveOrder(order1);

        orderController.deliverOrder(order1.getId());

        Order deliveredOrder = orderController.selectOrder(order1.getId());
        assertEquals(OrderState.DELIVERED, deliveredOrder.getState(), "The order state should be DELIVERED after marking as delivered.");
    }
}
