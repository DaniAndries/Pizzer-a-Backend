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
    private Pasta pasta2 = new Pasta(2, "Bolognese", 9.5, ingredientList2);
    private Pasta pasta3 = new Pasta(3, "Pesto", 8.0, ingredientList3);
    private Pizza pizza1 = new Pizza(4, "Pepperoni", 14.0, ingredientList1, "MEDIUM");
    private Drink drink1 = new Drink(5, "Coca-Cola", 2.5, Size.SMALL);

    @BeforeEach
    void setUp() throws SQLException {
        DatabaseConf.dropAndCreateTables();
        productController = new ProductController();
    }

    void setUpHelper() throws SQLException {
        productController.saveProduct(pasta1);
        productController.saveProduct(pasta2);
        productController.saveProduct(pasta3);
        productController.saveProduct(pizza1);
        productController.saveProduct(drink1);
    }

    List<Product> setUpProductList() throws SQLException {
        List<Product> products = new ArrayList<>();

        products.add(pasta1);
        products.add(pasta2);
        products.add(pasta3);
        products.add(pizza1);
        products.add(drink1);

        return products;
    }

    @Test
    void testSaveProduct() throws SQLException {
        productController.saveProduct(pasta1);

        Pasta newPasta = (Pasta) productController.findProductById(pasta1.getId());

        assertEquals(pasta1, newPasta);
    }

    @Test
    void testDeleteProduct() throws SQLException {
        productController.saveProduct(pasta2);

        Pasta newPasta = (Pasta) productController.findProductById(1);

        productController.deleteProduct(pasta2);

        newPasta = (Pasta) productController.findProductById(1);

        assertNull(newPasta);
    }

    @Test
    void testFindProductById() throws SQLException {
        productController.saveProduct(pasta3);

        Pasta newPasta = (Pasta) productController.findProductById(1);

        assertEquals(pasta3, newPasta);
    }

    @Test
    void testFindAllProducts() throws SQLException {
        setUpHelper();

        List<Product> allProducts = productController.findAll();

        assertEquals(setUpProductList().size(), allProducts.size());
    }

    @Test
    void testFindIngredientsById() throws SQLException {
        setUpHelper();

        Ingredient newIngredient = productController.findIngredientsById(2);

        assertEquals(tomato, newIngredient);
    }

    @Test
    void testFindAlergensByIngredient() throws SQLException {
        setUpHelper();

        List<String> newAlergens = productController.findAlergensByIngredient(cheese);

        assertEquals(cheese.getAlergens(), newAlergens);
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
