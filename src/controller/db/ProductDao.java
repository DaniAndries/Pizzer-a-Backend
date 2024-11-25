package controller.db;

import model.Client;
import model.Ingredient;
import model.Product;

import java.sql.SQLException;
import java.util.List;

public interface ProductDao {
    void saveProduct(Product product) throws SQLException;

    void saveIngredient(Ingredient ingredient) throws SQLException;

    void saveAlergen(String alergen) throws SQLException;

    void deleteProduct(Product product) throws SQLException;

    void deleteIngredient(Ingredient ingredient) throws SQLException;

    void deleteAlergen(String alergen) throws SQLException;

    void updateProduct(Product product) throws SQLException;

    void updateIngredient(Ingredient ingredient) throws SQLException;

    void updateAlergen(String alergen) throws SQLException;

    Product findProductById(int id) throws SQLException;

    Ingredient findIngredientsById(int id) throws SQLException;

    List<String> findAlergensByIngredient(int id) throws SQLException;

    List<Product> findAll() throws SQLException;
}
