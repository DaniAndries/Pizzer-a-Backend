import controller.ClientController;
import controller.ProductController;
import controller.db.impl.JdbcClientDao;
import model.*;
import utils.DatabaseConf;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        JdbcClientDao jdbcClientDao = new JdbcClientDao();
        ClientController clientController = new ClientController();
        ProductController productController = new ProductController();


        List<Client> clientList = new ArrayList<>();

        Client auxiliar = new Client();

        // Ingredientes
        Ingredient cheese = new Ingredient(1, "Cheese", List.of("Lactose"));
        Ingredient tomato = new Ingredient(2, "Tomato", List.of("Sulfites"));
        Ingredient pepper = new Ingredient(3, "Pepper", new ArrayList<>());
        Ingredient bacon = new Ingredient(4, "Bacon", List.of("Sulfites"));
        Ingredient mushroom = new Ingredient(5, "Mushroom", new ArrayList<>());

        // Listas de ingredientes
        List<Ingredient> ingredientList1 = List.of(cheese, tomato);
        List<Ingredient> ingredientList2 = List.of(tomato, bacon);
        List<Ingredient> ingredientList3 = List.of(mushroom, pepper);
        List<Ingredient> ingredientList4 = List.of(cheese, pepper);
        List<Ingredient> ingredientList5 = List.of(cheese, bacon, tomato);

        // Productos
        Pasta pasta1 = new Pasta(1, "Carbonara", 10.5, ingredientList1);
        Pasta pasta2 = new Pasta(2, "Bolognese", 9.5, ingredientList2);
        Pasta pasta3 = new Pasta(3, "Pesto", 8.0, ingredientList3);
        Pizza pizza1 = new Pizza(4, "Pepperoni", 14.0, ingredientList1, "MEDIUM");
        Drink drink1 = new Drink(5, "Coca-Cola", 2.5, Size.SMALL);

        Client client1 = new Client("12345678A", "Juan Pérez", "Calle Falsa 123, Madrid", "600123456", "juan.perez@example.com", "password123", false);
        Client client2 = new Client("87654321B", "Ana Gómez", "Av. Siempre Viva 742, Barcelona", "650654321", "ana.gomez@example.com", "securepass456", false);
        Client client3 = new Client("45612378C", "Luis Martínez", "Plaza Mayor 5, Valencia", "610987654", "luis.martinez@example.com", "mypassword789", true);
        Client client4 = new Client("78965412D", "Marta López", "Calle Luna 8, Sevilla", "620112233", "marta.lopez@example.com", "martal123", false);
        Client client5 = new Client("32198765E", "Carlos García", "Paseo del Prado 15, Bilbao", "630445566", "carlos.garcia@example.com", "garcia456", true);

        auxiliar = client1;

        auxiliar.setClientName("Rafiki Kashimiri");

        try {
            DatabaseConf.dropAndCreateTables();

            productController.saveProduct(pasta1);
            productController.saveProduct(pasta2);
            productController.saveProduct(pasta3);
            productController.saveProduct(pizza1);
            productController.saveProduct(drink1);

            List<Product> products = productController.findAll();
            System.out.println("------------------------------------------------------------------------");

            products.forEach(System.out::println);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
