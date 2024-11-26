package controller.db;

import model.Client;
import model.Ingredient;
import model.Product;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface ProductDao {
    void saveProduct(Product product) throws SQLException;

    void saveProductIngredient(int ingredientId, int productId, Connection conn) throws SQLException;

    void saveIngredient(Ingredient ingredient, int productId, Connection conn) throws SQLException;

    void saveIngredientAlergen(int ingredientId, int alergen, Connection conn) throws SQLException;

    void saveAlergen(String alergen, int ingredientId, Connection conn) throws SQLException;

    void deleteProduct(Product product) throws SQLException;

    void deleteIngredient(Ingredient ingredient) throws SQLException;

    void deleteAlergen(int id) throws SQLException;

    void updateProduct(Product product) throws SQLException;

    void updateIngredient(Ingredient ingredient) throws SQLException;

    void updateAlergen(String alergen, int id) throws SQLException;

    Product findProductById(int id) throws SQLException;

    Ingredient findIngredientsById(int id) throws SQLException;

    Ingredient findIngredientsByName(String name, Connection conn) throws SQLException;

    String findAlergensByName(String name, Connection conn) throws SQLException;

    List<Ingredient> findIngredientsByProduct(int id) throws SQLException;

    List<String> findAlergensByIngredient(int id) throws SQLException;

    List<Product> findAll() throws SQLException;
}
