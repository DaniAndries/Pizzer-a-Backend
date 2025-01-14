package controller.db.impl;

import controller.db.ProductDao;
import model.Ingredient;
import model.Product;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class JpaProductDao implements ProductDao {
    @Override
    public void saveProduct(Product product) throws SQLException {

    }

    @Override
    public void saveProductIngredient(int ingredientId, int productId, Connection conn) throws SQLException {

    }

    @Override
    public void saveIngredient(Ingredient ingredient, int productId, Connection conn) throws SQLException {

    }

    @Override
    public void saveIngredientAlergen(int ingredientId, int alergen, Connection conn) throws SQLException {

    }

    @Override
    public void saveAlergen(String alergen, int ingredientId, Connection conn) throws SQLException {

    }

    @Override
    public void deleteProduct(Product product) throws SQLException {

    }

    @Override
    public void deleteIngredient(Ingredient ingredient) throws SQLException {

    }

    @Override
    public void deleteAlergen(int id) throws SQLException {

    }

    @Override
    public void updateProduct(Product product) throws SQLException {

    }

    @Override
    public void updateIngredient(Ingredient ingredient) throws SQLException {

    }

    @Override
    public void updateAlergen(String alergen, int id) throws SQLException {

    }

    @Override
    public Product findProductById(int id) throws SQLException {
        return null;
    }

    @Override
    public Ingredient findIngredientsById(int id) throws SQLException {
        return null;
    }

    @Override
    public Ingredient findIngredientsByName(String name, Connection conn) throws SQLException {
        return null;
    }

    @Override
    public String findAlergensByName(String name, Connection conn) throws SQLException {
        return "";
    }

    @Override
    public List<Ingredient> findIngredientsByProduct(int id) throws SQLException {
        return List.of();
    }

    @Override
    public List<String> findAlergensByIngredient(Ingredient ingredient) throws SQLException {
        return List.of();
    }

    @Override
    public List<Product> findAll() throws SQLException {
        return List.of();
    }
}
