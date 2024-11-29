package controller;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.opencsv.exceptions.CsvValidationException;
import controller.db.impl.JdbcProductDao;
import model.Ingredient;
import model.Product;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductController {
    JdbcProductDao productDao = new JdbcProductDao();
    private Product product;
    private List<Ingredient> ingredients = new ArrayList<>();

    public ProductController() {
    }

    public void saveProduct(Product product) throws SQLException {
        productDao.saveProduct(product);
    }

    public void deleteProduct(Product product) throws SQLException {
        productDao.deleteProduct(product);
    }

    public void deleteIngredient(Ingredient ingredient) throws SQLException {
        productDao.deleteIngredient(ingredient);
    }

    public void deleteAlergen(int id) throws SQLException {
        productDao.deleteAlergen(id);
    }

    public void updateProduct(Product product) throws SQLException {
        productDao.updateProduct(product);
    }

    public void updateIngredient(Ingredient ingredient) throws SQLException {
        productDao.updateIngredient(ingredient);
    }

    public void updateAlergen(String alergen, int id) throws SQLException {
        productDao.updateAlergen(alergen, id);
    }

    public Product findProductById(int id) throws SQLException {
        return productDao.findProductById(id);
    }

    public Ingredient findIngredientsById(int id) throws SQLException {
        return productDao.findIngredientsById(id);
    }

    public List<String> findAlergensByIngredient(Ingredient ingredient) throws SQLException {
        return productDao.findAlergensByIngredient(ingredient);
    }

    public List<Product> findAll() throws SQLException {
        return productDao.findAll();
    }

    public void openCsvExport() throws CsvRequiredFieldEmptyException, FileNotFoundException, CsvDataTypeMismatchException {
        FileManagement.ingredientToCsv(this.ingredients);
    }

    public List<Ingredient> openCsvImport() throws IOException, CsvValidationException {
        return FileManagement.csvToIngredients();
    }
}
