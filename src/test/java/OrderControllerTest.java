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

public class OrderControllerTest {
    // Clients
    Client client1 = new Client("12345678A", "Juan Pérez", "Calle Falsa 123, Madrid", "600123456", "juan.perez@example.com", "password123", false);
    Client client2 = new Client("87654321B", "Ana Gómez", "Av. Siempre Viva 742, Barcelona", "650654321", "ana.gomez@example.com", "securepass456", false);
    // List of OrderLines
    List<OrderLine> orderLines1 = new ArrayList<>();
    List<OrderLine> orderLines2 = new ArrayList<>();
    private OrderController orderController;
    private ClientController clientController;
    private ProductController productController;
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
    private List<Ingredient> ingredientList4 = List.of(cheese, pepper);
    private List<Ingredient> ingredientList5 = List.of(cheese, bacon, tomato);
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


    @BeforeEach
    void setUp() throws SQLException {
        orderController = new OrderController();
        clientController = new ClientController();
        productController = new ProductController();
        DatabaseConf.dropAndCreateTables();
        clientController.registerClient(client1);
        clientController.registerClient(client2);
    }

    void setUpHelper() throws SQLException {
        productController.saveProduct(pasta1);
        productController.saveProduct(pasta2);
        productController.saveProduct(pasta3);
        productController.saveProduct(pizza1);
        productController.saveProduct(drink1);

        orderLines1.add(orderLine1);
        orderLines1.add(orderLine3);
        orderLines2.add(orderLine2);
        orderLines2.add(orderLine4);

        order1.setOrderLines(orderLines1);
        order2.setOrderLines(orderLines2);

        client1.setOrderList(List.of(order1));
        client2.setOrderList(List.of(order2));

        orderController.saveOrder(order1);
        orderController.saveOrder(order2);
    }

    @Test
    void testSaveOrder() throws SQLException {
        productController.saveProduct(pasta1);
        productController.saveProduct(pasta2);
        productController.saveProduct(pasta3);
        productController.saveProduct(pizza1);
        productController.saveProduct(drink1);
        // Prepare test data
        orderLines1.add(orderLine1);
        orderLines1.add(orderLine3);
        order1.setOrderLines(orderLines1);

        // Save the order
        orderController.saveOrder(order1);

        // Retrieve the order
        Order newOrder = orderController.selectOrder(order1.getId());

        // Validate the retrieved order
        assertNotNull(newOrder, "Retrieved order should not be null.");
        assertEquals(order1.getId(), newOrder.getId(), "Order ID mismatch.");
        assertEquals(order1.getState(), newOrder.getState(), "Order state mismatch.");
        assertEquals(order1.getPaymentMethod(), newOrder.getPaymentMethod(), "Payment method mismatch.");
        assertEquals(order1.getOrderLines().size(), newOrder.getOrderLines().size(), "Order lines count mismatch.");

        // Optionally validate each order line
        for (int i = 0; i < order1.getOrderLines().size(); i++) {
            assertEquals(order1.getOrderLines().get(i), newOrder.getOrderLines().get(i), "OrderLine mismatch at index " + i);
        }

        System.out.println(newOrder);
        System.out.println(order1);
    }

    @Test
    void testSaveOrderWithoutOrderLines() throws SQLException {
        order1.setOrderLines(new ArrayList<>());
        orderController.saveOrder(order1);

        Order retrievedOrder = orderController.selectOrder(order1.getId());
        assertNotNull(retrievedOrder);
        assertTrue(retrievedOrder.getOrderLines().isEmpty(), "Order lines should be empty.");
    }

    @Test
    void testSelectNonExistentOrder() throws SQLException {
        Order retrievedOrder = orderController.selectOrder(9999);
        assertNull(retrievedOrder, "Should return null for non-existent order.");
    }


    @Test
    void testUpdateOrder() throws SQLException {
        orderController.saveOrder(order1);

        order1.setState(OrderState.DELIVERED);
        orderController.updateOrder(order1);

        Order newOrder = orderController.selectOrder(order1.getId());
        assertEquals(OrderState.DELIVERED, newOrder.getState());
    }

    @Test
    void testDeleteOrder() throws SQLException {
        orderController.saveOrder(order1);

        Order newOrder = orderController.selectOrder(order1.getId());
        assertNotNull(newOrder);

        orderController.deleteOrder(order1);

        newOrder = orderController.selectOrder(order1.getId());
        assertNull(newOrder);
    }

    @Test
    void testAddToCart() throws SQLException {
        productController.saveProduct(pizza1);

        orderController.addToCart(pizza1, client1, 3);

        List<Order> newOrders = orderController.selectOrdersByClient(client1);

        assertNotNull(newOrders);
        assertFalse(newOrders.isEmpty(), "The orders list should not be empty after adding to cart");

        Order newOrder = newOrders.getFirst();

        List<OrderLine> orderLines = orderController.selectOrderLinesByOrder(newOrder);
        assertNotNull(orderLines);
        assertFalse(orderLines.isEmpty(), "The order lines list should not be empty after adding to cart");

        OrderLine newOrderLine = orderLines.getFirst();
        assertEquals(1, orderLines.size(), "There should be exactly one order line");
        assertEquals(pizza1, newOrderLine.getProducto(), "The product in the order line should be pizza1");
        assertEquals(3, newOrderLine.getAmount(), "The amount in the order line should be 3");
    }

    @Test
    void testFinalizeOrderOrder() throws SQLException {
        orderLines1.add(orderLine1);
        orderLines1.add(orderLine3);

        order1.setOrderLines(orderLines1);

        productController.saveProduct(pasta1);
        productController.saveProduct(pizza1);

        orderController.saveOrder(order1);

        orderController.finalizeOrder(client1, PaymentMethod.CARD);

        Order finalizedOrder = orderController.selectOrder(order1.getId());
        assertEquals(OrderState.FINISHED, finalizedOrder.getState());
    }

    @Test
    void testCancelOrder() throws SQLException {
        orderController.saveOrder(order1);

        orderController.cancelOrder(client1);

        Order canceledOrder = orderController.selectOrder(order1.getId());
        assertEquals(OrderState.CANCELED, canceledOrder.getState());
    }

    @Test
    void testDeliverOrder() throws SQLException {
        orderController.saveOrder(order1);

        orderController.deliverOrder(order1.getId());

        Order deliveredOrder = orderController.selectOrder(order1.getId());
        assertEquals(OrderState.DELIVERED, deliveredOrder.getState());
    }

    @Test
    void testselectOrdersByClient() throws SQLException, ParseException {
        setUpHelper();
        List<Order> orders = orderController.selectOrdersByClient(client1);


        assertNotNull(orders, "The orders list should not be null");
        assertFalse(orders.isEmpty(), "The orders list should not be empty");

        Order order = orders.get(0);

        assertEquals(1, order.getId(), "The order ID should match");
        assertEquals(OrderState.PENDING, order.getState(), "The order state should be PENDING");
        assertEquals(client1, order.getClient(), "The client should match");

        Date expectedDate = new SimpleDateFormat("yyyy-MM-dd").parse("2024-12-02");
        assertEquals(expectedDate, order.getOrderDate(), "The order date should match");

        List<OrderLine> orderLines = order.getOrderLines();

        assertNotNull(orderLines, "Order lines should not be null");
        assertFalse(orderLines.isEmpty(), "Order lines should not be empty");

        OrderLine orderLine = orderLines.getFirst();
        assertEquals(1, orderLine.getId(), "The order line ID should match");
        assertEquals(4, orderLine.getAmount(), "The amount in the order line should be 4");
        assertEquals(client1.getOrderList().getFirst().getOrderLines().getFirst().getProducto(), orderLine.getProducto(), "The product in the order line should match");
    }

    @Test
    void testSelectOrderByState() throws SQLException {
        setUpHelper();

        Order newOrder = orderController.selectOrderByState(OrderState.PENDING, client1);

        assertNotNull(newOrder);
        assertEquals(OrderState.PENDING, newOrder.getState());
    }

    @Test
    void testSelectOrderLinesByOrder() throws SQLException {
        setUpHelper();

        // Select order lines for order1
        List<OrderLine> orderLines = orderController.selectOrderLinesByOrder(order1);

        // Validate the result
        assertNotNull(orderLines, "Order lines should not be null.");
        assertEquals(order1.getOrderLines().size(), orderLines.size(), "Incorrect number of order lines for order1.");

        // Validate individual order lines
        assertTrue(orderLines.contains(orderLine1), "Order lines should include orderLine1.");
        assertTrue(orderLines.contains(orderLine3), "Order lines should include orderLine3.");
    }

    @Test
    void testSelectOrderLinesByNonExistentOrder() throws SQLException {
        List<OrderLine> orderLines = orderController.selectOrderLinesByOrder(new Order(9999, new Date(), OrderState.PENDING, PaymentMethod.UNPAID, client1));
        assertNotNull(orderLines, "Order lines list should not be null.");
        assertTrue(orderLines.isEmpty(), "Order lines list should be empty for a non-existent order.");
    }
}
