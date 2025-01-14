package controller;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.opencsv.exceptions.CsvValidationException;
import controller.db.impl.JdbcProductDao;
import controller.db.impl.JpaProductDao;
import model.Ingredient;
import model.Product;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * The ProductController class manages operations related to products and ingredients.
 * It acts as an intermediary between the data access layer (JdbcProductDao) and the
 * business logic of the application, providing methods for CRUD operations and CSV import/export.
 *
 * @author DaniAndries
 * @version 0.1
 */
public class ProductController {
    private JpaProductDao productDao = new JpaProductDao(); // DAO for product-related database operations
    private Product product; // Currently managed product (if any)
    private List<Ingredient> ingredients = new ArrayList<>(); // List of ingredients associated with the product

    /**
     * Constructs a new ProductController instance.
     */
    public ProductController() {
    }

    /**
     * Saves a new product to the database.
     *
     * @param product the product to be saved
     * @throws SQLException if a database access error occurs
     */
    public void saveProduct(Product product) throws SQLException {
        productDao.saveProduct(product);
    }

    /**
     * Deletes a specified product from the database.
     *
     * @param product the product to be deleted
     * @throws SQLException if a database access error occurs
     */
    public void deleteProduct(Product product) throws SQLException {
        productDao.deleteProduct(product);
    }

    /**
     * Deletes a specified ingredient from the database.
     *
     * @param ingredient the ingredient to be deleted
     * @throws SQLException if a database access error occurs
     */
    public void deleteIngredient(Ingredient ingredient) throws SQLException {
        productDao.deleteIngredient(ingredient);
    }

    /**
     * Deletes an allergen by its ID from the database.
     *
     * @param id the ID of the allergen to be deleted
     * @throws SQLException if a database access error occurs
     */
    public void deleteAlergen(int id) throws SQLException {
        productDao.deleteAlergen(id);
    }

    /**
     * Updates an existing product in the database.
     *
     * @param product the product with updated information
     * @throws SQLException if a database access error occurs
     */
    public void updateProduct(Product product) throws SQLException {
        productDao.updateProduct(product);
    }

    /**
     * Updates an existing ingredient in the database.
     *
     * @param ingredient the ingredient with updated information
     * @throws SQLException if a database access error occurs
     */
    public void updateIngredient(Ingredient ingredient) throws SQLException {
        productDao.updateIngredient(ingredient);
    }

    /**
     * Updates an allergen's name by its ID.
     *
     * @param alergen the new name of the allergen
     * @param id      the ID of the allergen to be updated
     * @throws SQLException if a database access error occurs
     */
    public void updateAlergen(String alergen, int id) throws SQLException {
        productDao.updateAlergen(alergen, id);
    }

    /**
     * Retrieves a product by its ID from the database.
     *
     * @param id the ID of the product to be retrieved
     * @return the product associated with the given ID, or null if not found
     * @throws SQLException if a database access error occurs
     */
    public Product findProductById(int id) throws SQLException {
        return productDao.findProductById(id);
    }

    /**
     * Retrieves an ingredient by its ID from the database.
     *
     * @param id the ID of the ingredient to be retrieved
     * @return the ingredient associated with the given ID, or null if not found
     * @throws SQLException if a database access error occurs
     */
    public Ingredient findIngredientsById(int id) throws SQLException {
        return productDao.findIngredientsById(id);
    }

    /**
     * Finds all allergens associated with a specified ingredient.
     *
     * @param ingredient the ingredient whose allergens are to be found
     * @return a list of allergens associated with the specified ingredient
     * @throws SQLException if a database access error occurs
     */
    public List<String> findAlergensByIngredient(Ingredient ingredient) throws SQLException {
        return productDao.findAlergensByIngredient(ingredient);
    }

    /**
     * Retrieves a list of all products from the database.
     *
     * @return a list of all products
     * @throws SQLException if a database access error occurs
     */
    public List<Product> findAll() throws SQLException {
        return productDao.findAll();
    }

    /**
     * Exports the current list of ingredients to a CSV file.
     *
     * @throws CsvRequiredFieldEmptyException if a required field is empty
     * @throws FileNotFoundException if the file cannot be found
     * @throws CsvDataTypeMismatchException if data type mismatch occurs during export
     */
    public void openCsvExport() throws CsvRequiredFieldEmptyException, FileNotFoundException, CsvDataTypeMismatchException {
        FileManagement.ingredientToCsv(this.ingredients);
    }

    /**
     * Imports ingredients from a CSV file.
     *
     * @return a list of ingredients imported from the CSV file
     * @throws IOException if an I/O error occurs
     * @throws CsvValidationException if CSV validation fails
     */
    public List<Ingredient> openCsvImport() throws IOException, CsvValidationException {
        return FileManagement.csvToIngredients();
    }
}
