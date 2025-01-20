import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import controller.FileManagement;
import controller.ProductController;
import model.Drink;
import model.Ingredient;
import model.Pasta;
import model.Pizza;
import model.Product;
import model.Size;

public class JpaProductController {

    // Controllers
    private ProductController productController = new ProductController();

    // Ingredients
    private Ingredient cheese = new Ingredient(1, "Cheese", new ArrayList<>(List.of("Lactose")));
    private Ingredient tomato = new Ingredient(2, "Tomato", new ArrayList<>());
    private Ingredient pepper = new Ingredient(3, "Pepper", new ArrayList<>());
    private Ingredient bacon = new Ingredient(4, "Bacon", new ArrayList<>(List.of("Sulfites")));
    private Ingredient mushroom = new Ingredient(5, "Mushroom", new ArrayList<>());

    // Listas de ingredientes
    private List<Ingredient> ingredientList1 = new ArrayList<>(List.of(cheese, tomato));
    private List<Ingredient> ingredientList2 = new ArrayList<>(List.of(tomato, bacon));
    private List<Ingredient> ingredientList3 = new ArrayList<>(List.of(mushroom, pepper));

    // Products
    private Pasta pasta1 = new Pasta(1, "Carbonara", 10.5, ingredientList1);
    private Pasta pasta2 = new Pasta(2, "Bolognese", 9.5, ingredientList2);
    private Pasta pasta3 = new Pasta(3, "Pesto", 8.0, ingredientList3);
    private Pizza pizza1 = new Pizza(4, "Pepperoni", 14.0, ingredientList1, Size.MEDIUM);
    private Drink drink1 = new Drink(5, "Coca-Cola", 2.5, Size.SMALL);

    /**
     * Tests the saveProduct method by saving a product and verifying it is
     * retrievable.
     *
     * @throws SQLException if a database error occurs.
     */
    @Test
    void testSaveProduct() throws SQLException {
        productController.saveProduct(pasta1);
        productController.saveProduct(pizza1);

        Pasta newPasta = (Pasta) productController.findProductById(1);

        System.out.println(newPasta);
        System.out.println(pasta1);

        // Verificamos que los alérgenos se guardaron correctamente
        assertEquals(cheese.getAllergens(), newPasta.getIngredients().getFirst().getAllergens());
        // Verificamos que los ingredientes son iguales
        assertEquals(pasta1.getIngredients(), newPasta.getIngredients());
        // Verificamos que los productos son iguales
        assertEquals(pasta1, newPasta);

    }

    /**
     * Tests the deleteProduct method by deleting a product and verifying it no
     * longer exists.
     *
     * @throws SQLException if a database error occurs.
     */
    @Test
    void testDeleteProduct() throws SQLException {
        productController.saveProduct(pizza1);
        pizza1.setId(4);
        productController.deleteProduct(pizza1);
        /**
         * ESTE TEST VARIA DEPENDIENDO DEL ID QUE ASIGNA JPA POR SI MISMO
         */
        assertEquals(4, productController.findAll().size());
    }

    /**
     * Tests finding a product by its ID.
     *
     * @throws SQLException if a database error occurs.
     */
    @Test
    void testFindProductById() throws SQLException {
        productController.saveProduct(pasta1);

        Pasta newPasta = (Pasta) productController.findProductById(1);

        assertEquals(pasta1, newPasta);
    }

    /**
     * Tests retrieving all products from the database.
     *
     * @throws SQLException if a database error occurs.
     */
    @Test
    void testFindAllProducts() throws SQLException {
        productController.saveProduct(pasta1);
        productController.saveProduct(pasta2);
        productController.saveProduct(pasta3);
        productController.saveProduct(pizza1);
        productController.saveProduct(drink1);

        List<Product> allProducts = new ArrayList<>(List.of(pasta1, pasta2, pasta3, pizza1, drink1));

        List<Product> newProducts = productController.findAll();

        assertEquals(allProducts.size(), newProducts.size());
    }

    /**
     * Tests retrieving allergens for a specific ingredient.
     *
     * @throws SQLException if a database error occurs.
     */
    @Test
    void testFindAlergensByIngredient() throws SQLException {
        productController.saveProduct(pasta1);
        productController.saveProduct(pasta2);
        productController.saveProduct(pasta3);
        productController.saveProduct(pizza1);
        productController.saveProduct(drink1);

        List<String> newAlergens = productController.findAlergensByIngredient(cheese);

        assertEquals(cheese.getAllergens(), newAlergens);
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
