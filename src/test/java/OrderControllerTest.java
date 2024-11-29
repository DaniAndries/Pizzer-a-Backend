package test.java;

import controller.OrderController;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.DatabaseConf;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OrderControllerTest {
    // Clients
    Client client1 = new Client("12345678A", "Juan Pérez", "Calle Falsa 123, Madrid", "600123456", "juan.perez@example.com", "password123", false);
    Client client2 = new Client("87654321B", "Ana Gómez", "Av. Siempre Viva 742, Barcelona", "650654321", "ana.gomez@example.com", "securepass456", false);
    private OrderController orderController;
    // Ingredients
    private Ingredient cheese = new Ingredient(1, "Cheese", List.of("Lactose"));
    private Ingredient tomato = new Ingredient(2, "Tomato", new ArrayList<>());
    private Ingredient pepper = new Ingredient(3, "Pepper", new ArrayList<>());
    private Ingredient bacon = new Ingredient(4, "Bacon", List.of("Sulfites"));
    private Ingredient mushroom = new Ingredient(5, "Mushroom", new ArrayList<>());
    // Lists of ingredients
    private List<Ingredient> ingredientList1 = List.of(cheese, tomato);
    // Products
    private Pasta pasta1 = new Pasta(1, "Carbonara", 10.5, ingredientList1);
    // OrderLists
    private OrderLine orderLine1 = new OrderLine(1, pasta1);
    private Pizza pizza1 = new Pizza(4, "Pepperoni", 14.0, ingredientList1, "MEDIUM");
    private OrderLine orderLine3 = new OrderLine(1, pizza1);
    private List<Ingredient> ingredientList2 = List.of(tomato, bacon);
    private Pasta pasta2 = new Pasta(2, "Bolognese", 9.5, ingredientList2);
    private List<Ingredient> ingredientList3 = List.of(mushroom, pepper);
    private Pasta pasta3 = new Pasta(3, "Pesto", 8.0, ingredientList3);
    private OrderLine orderLine4 = new OrderLine(2, pasta3);
    private List<Ingredient> ingredientList4 = List.of(cheese, pepper);
    private List<Ingredient> ingredientList5 = List.of(cheese, bacon, tomato);
    private Drink drink1 = new Drink(5, "Coca-Cola", 2.5, Size.SMALL);
    // Orders
    private Order order1 = new Order(new Date(), OrderState.PENDING, null, client1);
    private Order order2 = new Order(new Date(), OrderState.PAID, null, client2);
    private OrderLine orderLine2 = new OrderLine(2, drink1);

    @BeforeEach
    void setUp() throws SQLException {
        DatabaseConf.dropAndCreateTables();
        orderController = new OrderController();
    }

    void setUpHelper() throws SQLException {
        orderController.saveOrder(order1);
        orderController.saveOrder(order2);
        orderController.saveOrderLine(orderLine1, order1);
        orderController.saveOrderLine(orderLine3, order1);
        orderController.saveOrderLine(orderLine2, order2);
        orderController.saveOrderLine(orderLine4, order2);
    }

    @Test
    void testSaveOrder() throws SQLException {
        orderController.saveOrder(order1);

        Order newOrder = orderController.selectOrder(1);

        assertEquals(order1, newOrder);
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
        orderController.addToCart(pizza1, client1, 3);

        Order newOrder = orderController.selectOrderByClient(client1);
        OrderLine orderLine = orderController.selectOrderLinesByOrder(newOrder).getFirst();

        assertEquals(1, orderController.selectOrderLinesByOrder(orderController.selectOrderByClient(client1)));
        assertEquals(pizza1, orderController.selectOrderLinesByOrder(orderController.selectOrderByClient(client1)).getFirst().getProducto());
        assertEquals(3, orderController.selectOrderLinesByOrder(orderController.selectOrderByClient(client1)).getFirst().getAmount());
    }

    @Test
    void testFinalizeOrder() throws SQLException {
        orderController.saveOrder(order1);

        orderController.finalizeOrder(client1, PaymentMethod.CARD);

        Order finalizedOrder = orderController.selectOrder(order1.getId());
        assertEquals(OrderState.PAID, finalizedOrder.getState());
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
    void testSelectOrderByClient() throws SQLException {
        setUpHelper();

        Order clientOrder = orderController.selectOrderByClient(client1);

        assertNotNull(clientOrder);
        assertEquals(client1, clientOrder.getClient());
    }

    @Test
    void testSelectOrderByState() throws SQLException {
        setUpHelper();

        Order pendingOrder = orderController.selectOrderByState(OrderState.PENDING, client1);

        assertNotNull(pendingOrder);
        assertEquals(OrderState.PENDING, pendingOrder.getState());
    }

    @Test
    void testSelectOrderLinesByOrder() throws SQLException {
        setUpHelper();

        List<OrderLine> orderLines = orderController.selectOrderLinesByOrder(order1);

        assertNotNull(orderLines);
        assertEquals(1, orderLines.size());
        assertEquals(orderLine1, orderLines.get(0));
    }
}
