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

public class JpaProductController {

    // Controllers
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
    // Products
    private Pasta pasta1 = new Pasta(1, "Carbonara", 10.5, ingredientList1);
    private Pasta pasta2 = new Pasta(2, "Bolognese", 9.5, ingredientList2);
    private Pasta pasta3 = new Pasta(3, "Pesto", 8.0, ingredientList3);
    private Pizza pizza1 = new Pizza(4, "Pepperoni", 14.0, ingredientList1, Size.MEDIUM);
    private Drink drink1 = new Drink(5, "Coca-Cola", 2.5, Size.SMALL);


    /**
     * Sets up the test environment by initializing the ProductController and resetting the database.
     *
     * @throws SQLException if a database error occurs during setup.
     */
    @BeforeEach
    void setUp() throws SQLException {
        productController = new ProductController();
    }

    /**
     * Helper method to populate the database with sample products.
     *
     * @throws SQLException if a database error occurs during setup.
     */
    void setUpHelper() throws SQLException {
        productController.saveProduct(pasta1);
        productController.saveProduct(pasta2);
        productController.saveProduct(pasta3);
        productController.saveProduct(pizza1);
        productController.saveProduct(drink1);
    }

    /**
     * Helper method to create a list of sample products.
     *
     * @return a list of sample {@link Product} objects.
     * @throws SQLException if a database error occurs during product retrieval.
     */
    List<Product> setUpProductList() throws SQLException {
        List<Product> products = new ArrayList<>();

        products.add(pasta1);
        products.add(pasta2);
        products.add(pasta3);
        products.add(pizza1);
        products.add(drink1);

        return products;
    }

    /**
     * Tests the saveProduct method by saving a product and verifying it is retrievable.
     *
     * @throws SQLException if a database error occurs.
     */
    @Test
    void testSaveProduct() throws SQLException {
        productController.saveProduct(pasta1);

        Pasta newPasta = (Pasta) productController.findProductById(pasta1.getId());

        assertEquals(pasta1, newPasta);
    }

    /**
     * Tests the deleteProduct method by deleting a product and verifying it no longer exists.
     *
     * @throws SQLException if a database error occurs.
     */
    @Test
    void testDeleteProduct() throws SQLException {
        productController.saveProduct(pasta2);

        Pasta newPasta = (Pasta) productController.findProductById(1);

        productController.deleteProduct(pasta2);

        newPasta = (Pasta) productController.findProductById(1);

        assertNull(newPasta);
    }

    /**
     * Tests finding a product by its ID.
     *
     * @throws SQLException if a database error occurs.
     */
    @Test
    void testFindProductById() throws SQLException {
        productController.saveProduct(pasta3);

        Pasta newPasta = (Pasta) productController.findProductById(1);

        assertEquals(pasta3, newPasta);
    }

    /**
     * Tests retrieving all products from the database.
     *
     * @throws SQLException if a database error occurs.
     */
    @Test
    void testFindAllProducts() throws SQLException {
        setUpHelper();

        List<Product> allProducts = productController.findAll();

        assertEquals(setUpProductList().size(), allProducts.size());
    }

    /**
     * Tests finding an ingredient by its ID.
     *
     * @throws SQLException if a database error occurs.
     */
    @Test
    void testFindIngredientsById() throws SQLException {
        setUpHelper();

        Ingredient newIngredient = productController.findIngredientsById(2);

        assertEquals(tomato, newIngredient);
    }

    /**
     * Tests retrieving allergens for a specific ingredient.
     *
     * @throws SQLException if a database error occurs.
     */
    @Test
    void testFindAlergensByIngredient() throws SQLException {
        setUpHelper();

        List<String> newAlergens = productController.findAlergensByIngredient(cheese);

        assertEquals(cheese.getAllergens(), newAlergens);
    }

    /**
     * Tests saving a product and verifies it is saved successfully.
     *
     * @throws SQLException if a database error occurs.
     */
    @Test
    void shouldSaveProductSuccessfully() throws SQLException {
        Pasta originalPasta = new Pasta(1, "Carbonara", 10.5, ingredientList1);

        productController.saveProduct(originalPasta);

        Pasta retrievedPasta = (Pasta) productController.findProductById(originalPasta.getId());
        assertNotNull(retrievedPasta, "Expected pasta to be saved and retrieved successfully");
        assertEquals(originalPasta, retrievedPasta, "The retrieved pasta should match the saved pasta");
    }

    /**
     * Tests deleting a product and verifies it is removed successfully.
     *
     * @throws SQLException if a database error occurs.
     */
    @Test
    void shouldDeleteProductSuccessfully() throws SQLException {

        productController.saveProduct(pasta2);

        productController.deleteProduct(pasta2);

        Pasta deletedPasta = (Pasta) productController.findProductById(pasta2.getId());
        assertNull(deletedPasta, "Expected product to be deleted and not found");
    }

    /**
     * Tests that deleting a non-existent product throws an exception.
     */
    @Test
    void shouldThrowExceptionWhenDeletingNonExistentProduct() {
        Pasta nonExistentPasta = new Pasta(99, "Non-Existent", 10.0, ingredientList1);

        assertThrows(IllegalArgumentException.class, () -> productController.deleteProduct(nonExistentPasta),
                "Expected IllegalArgumentException when attempting to delete a non-existent product");
    }

    /**
     * Tests exporting ingredients to a CSV file.
     */
    @Test
    void shouldExportIngredientsToCsv() {
        List<Ingredient> ingredientsToExport = List.of(cheese, tomato, pepper);

        assertDoesNotThrow(() -> FileManagement.ingredientToCsv(ingredientsToExport),
                "Exporting ingredients to CSV should not throw an exception");
    }

    /**
     * Tests importing ingredients from a CSV file.
     */
    @Test
    void shouldImportIngredientsFromCsv() {
        List<Ingredient> importedIngredients = assertDoesNotThrow(FileManagement::csvToIngredients,
                "Importing ingredients from CSV should not throw an exception");

        assertNotNull(importedIngredients, "Expected imported ingredients list to not be null");
        assertFalse(importedIngredients.isEmpty(), "Expected imported ingredients list to not be empty");
    }

    /**
     * Tests exporting ingredients to CSV without exceptions.
     */
    @Test
    void testOpenCsvExport() {
        try {
            FileManagement.ingredientToCsv(List.of(cheese, tomato, pepper, bacon, mushroom));
        } catch (CsvRequiredFieldEmptyException | FileNotFoundException | CsvDataTypeMismatchException e) {
            fail("Excepción inesperada al exportar a CSV: " + e.getMessage());
        }
    }

    /**
     * Tests importing ingredients from CSV without exceptions.
     */
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
