package controller.db;

import model.Ingredient;
import model.Product;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * The ProductDao interface provides methods for interacting with the database
 * regarding products and ingredients. It includes methods for saving, updating,
 * deleting, and finding products and ingredients.
 *
 * @author DaniAndries
 * @version 0.1
 */
public interface ProductDao {

    /**
     * Saves a new product to the database.
     *
     * @param product The Product object to be saved.
     * @throws SQLException If there is a database access error.
     */
    void saveProduct(Product product) throws SQLException;

    /**
     * Saves an ingredient associated with a product to the database.
     *
     * @param ingredientId The ID of the ingredient to be saved.
     * @param productId The ID of the product to which the ingredient is associated.
     * @param conn The database connection.
     * @throws SQLException If there is a database access error.
     */
    void saveProductIngredient(int ingredientId, int productId, Connection conn) throws SQLException;

    /**
     * Saves an ingredient for a specific product to the database.
     *
     * @param ingredient The Ingredient object to be saved.
     * @param productId The ID of the product associated with the ingredient.
     * @param conn The database connection.
     * @throws SQLException If there is a database access error.
     */
    void saveIngredient(Ingredient ingredient, int productId, Connection conn) throws SQLException;

    /**
     * Saves an allergen associated with an ingredient to the database.
     *
     * @param ingredientId The ID of the ingredient associated with the allergen.
     * @param alergen The allergen to be saved.
     * @param conn The database connection.
     * @throws SQLException If there is a database access error.
     */
    void saveIngredientAlergen(int ingredientId, int alergen, Connection conn) throws SQLException;

    /**
     * Saves a new allergen to the database.
     *
     * @param alergen The allergen to be saved.
     * @param ingredientId The ID of the ingredient associated with the allergen.
     * @param conn The database connection.
     * @throws SQLException If there is a database access error.
     */
    void saveAlergen(String alergen, int ingredientId, Connection conn) throws SQLException;

    /**
     * Deletes a product from the database.
     *
     * @param product The Product object to be deleted.
     * @throws SQLException If there is a database access error.
     */
    void deleteProduct(Product product) throws SQLException;

    /**
     * Deletes an ingredient from the database.
     *
     * @param ingredient The Ingredient object to be deleted.
     * @throws SQLException If there is a database access error.
     */
    void deleteIngredient(Ingredient ingredient) throws SQLException;

    /**
     * Deletes an allergen from the database by its ID.
     *
     * @param id The ID of the allergen to be deleted.
     * @throws SQLException If there is a database access error.
     */
    void deleteAlergen(int id) throws SQLException;

    /**
     * Updates an existing product in the database.
     *
     * @param product The Product object with updated information.
     * @throws SQLException If there is a database access error.
     */
    void updateProduct(Product product) throws SQLException;

    /**
     * Updates an existing ingredient in the database.
     *
     * @param ingredient The Ingredient object with updated information.
     * @throws SQLException If there is a database access error.
     */
    void updateIngredient(Ingredient ingredient) throws SQLException;

    /**
     * Updates an allergen in the database.
     *
     * @param alergen The new allergen name.
     * @param id The ID of the allergen to be updated.
     * @throws SQLException If there is a database access error.
     */
    void updateAlergen(String alergen, int id) throws SQLException;

    /**
     * Finds a product by its ID.
     *
     * @param id The ID of the product to be found.
     * @return The Product object with the specified ID, or null if not found.
     * @throws SQLException If there is a database access error.
     */
    Product findProductById(int id) throws SQLException;

    /**
     * Finds an ingredient by its ID.
     *
     * @param id The ID of the ingredient to be found.
     * @return The Ingredient object with the specified ID, or null if not found.
     * @throws SQLException If there is a database access error.
     */
    Ingredient findIngredientsById(int id) throws SQLException;

    /**
     * Finds an ingredient by its name.
     *
     * @param name The name of the ingredient to be found.
     * @param conn The database connection.
     * @return The Ingredient object with the specified name, or null if not found.
     * @throws SQLException If there is a database access error.
     */
    Ingredient findIngredientsByName(String name, Connection conn) throws SQLException;

    /**
     * Finds allergens associated with an ingredient by its name.
     *
     * @param name The name of the ingredient.
     * @param conn The database connection.
     * @return A string representation of allergens associated with the ingredient.
     * @throws SQLException If there is a database access error.
     */
    String findAlergensByName(String name, Connection conn) throws SQLException;

    /**
     * Finds all ingredients associated with a specific product.
     *
     * @param id The ID of the product whose ingredients are to be found.
     * @return A list of Ingredient objects associated with the product.
     * @throws SQLException If there is a database access error.
     */
    List<Ingredient> findIngredientsByProduct(int id) throws SQLException;

    /**
     * Finds allergens associated with a specific ingredient.
     *
     * @param ingredient The Ingredient object whose allergens are to be found.
     * @return A list of allergen names associated with the ingredient.
     * @throws SQLException If there is a database access error.
     */
    List<String> findAlergensByIngredient(Ingredient ingredient) throws SQLException;

    /**
     * Finds all products in the database.
     *
     * @return A list of all Product objects.
     * @throws SQLException If there is a database access error.
     */
    List<Product> findAll() throws SQLException;
}
