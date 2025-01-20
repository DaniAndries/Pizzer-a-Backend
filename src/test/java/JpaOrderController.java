import controller.CustomerController;
import controller.OrderController;
import controller.ProductController;
import model.*;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JpaOrderController {

    // Controllers
    OrderController orderController = new OrderController();
    private CustomerController customerController = new CustomerController();
    private ProductController productController = new ProductController();
    // Customers
    Customer customer1 = new Customer("12345678A", "Juan Pérez", "Calle Falsa 123, Madrid", "600123456", "juan.perez@example.com", "password123", false);
    Customer customer2 = new Customer("87654321B", "Ana Gómez", "Av. Siempre Viva 742, Barcelona", "650654321", "ana.gomez@example.com", "securepass456", false);
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
    private Pizza pizza1 = new Pizza(4, "Pepperoni", 14.0, ingredientList1, Size.MEDIUM);
    private Pasta pasta2 = new Pasta(2, "Bolognese", 9.5, ingredientList2);
    private Pasta pasta3 = new Pasta(3, "Pesto", 8.0, ingredientList3);
    private Drink drink1 = new Drink(5, "Coca-Cola", 2.5, Size.SMALL);
    // OrderLines
    private OrderLine orderLine1 = new OrderLine(4, pasta1);
    private OrderLine orderLine3 = new OrderLine(5, pizza1);
    private OrderLine orderLine4 = new OrderLine(2, pasta3);
    private OrderLine orderLine2 = new OrderLine(7, drink1);
    // Orders
    private Order order1 = new Order(1, new Date(), OrderState.PENDING, PaymentMethod.UNPAID, customer1);
    private Order order2 = new Order(2, new Date(), OrderState.FINISHED, PaymentMethod.UNPAID, customer2);

    @Test
    public void saveOrder() throws SQLException {
        orderController.saveOrder(order1);

        Order pedidoPersistido = orderController.selectOrder(order1.getId());
        assertNotNull(pedidoPersistido);
        assertEquals(2, pedidoPersistido.getOrderLines().size());
    }

    @Test
    public void delete() throws SQLException {

        orderController.saveOrder(order2);
        orderController.deleteOrder(order2);

        Order pedidoPersistido = orderController.selectOrder(order2.getId());
        assertEquals(null, pedidoPersistido);
    }

    @Test
    public void update() throws SQLException {
        orderController.saveOrder(order1);
        order1.setState(OrderState.CANCELED);

        orderController.updateOrder(order1);
        assertEquals(OrderState.CANCELED, order1.getState());
    }

    @Test
    public void selectAll() throws SQLException {

        List<Order> listaObtenerPedidosByIdClient = orderController.selectOrdersByCustomer(customer1);
        assertEquals(2, listaObtenerPedidosByIdClient.size());

        order2.setState(OrderState.CANCELED);
        orderController.updateOrder(order2);

        List<Order> listaObtenerPedidosByState = orderController.selectOrdersByState(OrderState.CANCELED, customer2);

        List<OrderLine> listaObtenerLineasPedidosByState = orderController.selectOrderLinesByOrder(order2);
        assertEquals(2, listaObtenerLineasPedidosByState.size());
    }

}
