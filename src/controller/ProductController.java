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

    public ProductController(List<Ingredient> ingredientList) {
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

    public void deleteAlergen(String alergen) throws SQLException {
        productDao.deleteAlergen(alergen);
    }

    public void update(Product product) throws SQLException {
        update(product);
    }

    public Product findProductById(int id) throws SQLException {
        return productDao.findProductById(id);
    }

    public Ingredient findIngredientsById(int id) throws SQLException {
        return productDao.findIngredientsById(id);
    }

    public List<String> findAlergensByIngredient(int id) throws SQLException {
        return productDao.findAlergensByIngredient(id);
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
