package test.java;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import controller.FileManagement;
import controller.ProductController;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.DatabaseConf;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductControllerTest {
    private ProductController productController;
    private List<Product> products;

    // Ingredientes
    private Ingredient cheese = new Ingredient(1, "Cheese", List.of("Lactose"));
    private Ingredient tomato = new Ingredient(2, "Tomato", new ArrayList<>());
    private Ingredient pepper = new Ingredient(3, "Pepper", new ArrayList<>());
    private Ingredient bacon = new Ingredient(4, "Bacon", List.of("Sulfites"));
    private Ingredient mushroom = new Ingredient(5, "Mushroom", new ArrayList<>());

    // Listas de ingredientes
    private List<Ingredient> ingredientList1 = List.of(cheese, tomato);
    private List<Ingredient> ingredientList2 = List.of(tomato, bacon);
    private List<Ingredient> ingredientList3 = List.of(mushroom, pepper);
    private List<Ingredient> ingredientList4 = List.of(cheese, pepper);
    private List<Ingredient> ingredientList5 = List.of(cheese, bacon, tomato);

    // Productos
    private Pasta pasta1 = new Pasta(1, "Carbonara", 10.5, ingredientList1);
    private Pasta pasta2 = new Pasta(2, "Bolognese", 9.5, ingredientList2);
    private Pasta pasta3 = new Pasta(3, "Pesto", 8.0, ingredientList3);
    private Pizza pizza1 = new Pizza(4, "Pepperoni", 14.0, ingredientList1, "MEDIUM");
    private Drink drink1 = new Drink(5, "Coca-Cola", 2.5, Size.SMALL);

    @BeforeEach
    void setUp() throws SQLException {
        DatabaseConf.dropAndCreateTables();
        products = new ArrayList<>();
        products.add(pasta1);
        products.add(pasta2);
        products.add(pasta3);
        products.add(pizza1);
        products.add(drink1);

        productController = new ProductController();
    }

    @Test
    void testSaveProduct() {
        Pasta newPasta = new Pasta(6, "Alfredo", 12.0, ingredientList4);
        products.add(newPasta);

        assertTrue(products.contains(newPasta));
    }

    @Test
    void testDeleteProduct() {
        products.remove(pasta1);

        assertFalse(products.contains(pasta1));
    }

    @Test
    void testFindProductById() {
        Product foundProduct = products.stream().filter(product -> product.getId() == 4).findFirst().orElse(null);

        assertNotNull(foundProduct);
        assertEquals(pizza1, foundProduct);
    }

    @Test
    void testFindAllProducts() throws SQLException {
        List<Product> allProducts = productController.findAll();

        assertEquals(products.size(), allProducts.size());
    }

    @Test
    void testFindIngredientsById() {
        Ingredient foundIngredient = ingredientList1.stream().filter(ingredient -> ingredient.getId() == 1).findFirst().orElse(null);

        assertNotNull(foundIngredient);
        assertEquals(cheese, foundIngredient);
    }

    @Test
    void testFindAlergensByIngredient() {
        List<String> alergens = cheese.getAlergens();

        assertEquals(List.of("Lactose"), alergens);
    }

    @Test
    void testOpenCsvExport() {
        try {
            FileManagement.ingredientToCsv(List.of(cheese, tomato, pepper, bacon, mushroom));
        } catch (CsvRequiredFieldEmptyException | FileNotFoundException | CsvDataTypeMismatchException e) {
            fail("Excepción inesperada al exportar a CSV: " + e.getMessage());
        }
    }

    @Test
    void testOpenCsvImport() {
        try {
            List<Ingredient> importedIngredients = FileManagement.csvToIngredients();
            assertNotNull(importedIngredients);
        } catch (IOException e) {
            fail("Excepción inesperada al importar desde CSV: " + e.getMessage());
        }
    }
}
