package controller.db.impl;

import controller.db.ProductDao;
import model.*;
import utils.DatabaseConf;

import java.sql.*;
import java.util.List;

public class JdbcProductDao implements ProductDao {
    static final JdbcOrderDao jdbcCarDao = new JdbcOrderDao();

    private static final String INSERT_PRODUCT = "INSERT INTO product (product_name, price, size, type) VALUES (?,?,?,?)";
    private static final String INSERT_INGREDIENT = "INSERT INTO ingredient (ingredient_name) VALUES (?)";
    private static final String INSERT_ALERGEN = "INSERT INTO alergen (alergen_name) VALUES (?)";

    private static final String UPDATE_PRODUCT = "UPDATE product SET product.product_name=?, product.price=?, product.size=?, product.type=? WHERE product.id = ?";
    private static final String UPDATE_INGREDIENT = "UPDATE ingredient SET ingredient.ingredient_name=? WHERE product.id = ?";
    private static final String UPDATE_ALERGEN = "UPDATE alergen SET alergen_name.alergen_name_name=? WHERE product.id = ?";

    private static final String DELETE_PRODUCT = "DELETE FROM product WHERE product.ID = ?";
    private static final String DELETE_INGREDIENT = "DELETE FROM ingredient WHERE ingredient.ID = ?";
    private static final String DELETE_ALERGEN = "DELETE FROM alergen WHERE alergen.ID = ?";


    private static final String SELECT_PRODUCT = "SELECT product.id, product.product_name, product.price, product.size, product.type FROM product WHERE product.ID = ?";
    private static final String SELECT_INGREDIENT_BY_ID = "SELECT product.id, product.dni, product.client_name, product.direction, product.phone, product.mail, product.password, product.admin FROM client WHERE product.MAIl = ?";
    private static final String SELECT_ALERGENS_BY_INGREDIENTS = "SELECT product.id, product.product_name, product.price, product.size, product.type FROM product";

    // * "INSERT INTO product (product_name, price, size, type) VALUES (?,?,?,?)"
    @Override
    public void saveProduct(Product product) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DatabaseConf.URL, DatabaseConf.USER, DatabaseConf.PASSWORD);
             PreparedStatement stmtProduct = conn.prepareStatement(INSERT_PRODUCT, Statement.RETURN_GENERATED_KEYS)) {
            List<Ingredient> ingredients = List.of();

            stmtProduct.setString(1, product.getName());
            stmtProduct.setDouble(2, product.getPrice());

            if (product instanceof Pizza pizza) {
                stmtProduct.setString(3, pizza.getSize().toString());
                stmtProduct.setString(4, "PIZZA");
                ingredients = pizza.getIngredients();
            }

            if (product instanceof Pasta pasta) {
                stmtProduct.setString(3, "NULL");
                stmtProduct.setString(4, "PASTA");
                ingredients = pasta.getIngredients();
            }

            if (product instanceof Drink drink) {
                stmtProduct.setString(3, drink.getSize().toString());
                stmtProduct.setString(4, "DRINK");
            }

            stmtProduct.executeUpdate();

            try (ResultSet generatedKeys = stmtProduct.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    product.setId(generatedKeys.getInt(1));
                }
            }

            if (ingredients != null && !ingredients.isEmpty()) addIngredient(ingredients);

            System.out.println("The client: " + product.getId() + " has been created");
        }
    }

    private void addIngredient(List<Ingredient> ingredients) throws SQLException {
        for (Ingredient ingredient : ingredients) {
            saveIngredient(ingredient);
        }
    }

    // * "INSERT INTO ingredient (ingredient_name) VALUES (?)"
    @Override
    public void saveIngredient(Ingredient ingredient) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DatabaseConf.URL, DatabaseConf.USER, DatabaseConf.PASSWORD);
             PreparedStatement stmtIngredient = conn.prepareStatement(INSERT_INGREDIENT, Statement.RETURN_GENERATED_KEYS)) {
            stmtIngredient.setString(1, ingredient.getName());

            stmtIngredient.executeUpdate();

            try (ResultSet generatedKeys = stmtIngredient.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    ingredient.setId(generatedKeys.getInt(1));
                }
            }

            addAlergen(ingredient.getAlergens());

            System.out.println("The client: " + ingredient.getId() + " has been created");
        }
    }

    private void addAlergen(List<String> alergens) throws SQLException {
        for (String alergen : alergens) {
            saveAlergen(alergen);
        }
    }

    // * "INSERT INTO alergen (alergen_name) VALUES (?)"
    @Override
    public void saveAlergen(String alergen) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DatabaseConf.URL, DatabaseConf.USER, DatabaseConf.PASSWORD);
             PreparedStatement stmtAlergen = conn.prepareStatement(INSERT_ALERGEN, Statement.RETURN_GENERATED_KEYS)) {
            stmtAlergen.setString(1, alergen);

            stmtAlergen.executeUpdate();

            try (ResultSet generatedKeys = stmtAlergen.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    alergen.setId(generatedKeys.getInt(1));
                }
            }

            System.out.println("The client: " + alergen.getId() + " has been created");
        }
    }

    // * "UPDATE product SET product.product_name=?, product.price=?, product.size=?, product.type=? WHERE product.id = ?"
    @Override
    public void updateProduct(Product product) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DatabaseConf.URL, DatabaseConf.USER, DatabaseConf.PASSWORD);
             PreparedStatement stmtProduct = conn.prepareStatement(UPDATE_PRODUCT)) {
            stmtProduct.setString(1, product.getName());
            stmtProduct.setDouble(2, product.getPrice());
            stmtProduct.setInt(5, product.getId());

            if (product instanceof Pizza pizza) {
                stmtProduct.setString(3, pizza.getSize().toString());
                stmtProduct.setString(4, "PIZZA");
            }

            if (product instanceof Pasta pasta) {
                stmtProduct.setString(3, "NULL");
                stmtProduct.setString(4, "PASTA");
            }

            if (product instanceof Drink drink) {
                stmtProduct.setString(3, drink.getSize().toString());
                stmtProduct.setString(4, "DRINK");
            }

            stmtProduct.execute();
            System.out.println("The client: " + product.getId() + " has been modified");
        }
    }

    // * "UPDATE ingredient SET ingredient.ingredient_name=? WHERE product.id = ?"
    @Override
    public void updateIngredient(Ingredient ingredient) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DatabaseConf.URL, DatabaseConf.USER, DatabaseConf.PASSWORD);
             PreparedStatement stmtClient = conn.prepareStatement(UPDATE_INGREDIENT)) {
            stmtClient.setString(1, ingredient.getName());
            stmtClient.setInt(2, ingredient.getId());

            stmtClient.execute();
            System.out.println("The client: " + ingredient.getId() + " has been modified");
        }
    }

    // * "UPDATE alergen SET alergen.alergen_name=? WHERE product.id = ?"
    @Override
    public void updateAlergen(String alergen) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DatabaseConf.URL, DatabaseConf.USER, DatabaseConf.PASSWORD);
             PreparedStatement stmtClient = conn.prepareStatement(UPDATE_ALERGEN)) {
            stmtClient.setString(1, alergen.getName());
            stmtClient.setString(2, alergen.getSurname());

            stmtClient.execute();
            System.out.println("The client: " + alergen.getDni() + " has been modified");
        }
    }

    // * "DELETE FROM product WHERE product.ID = ?"
    @Override
    public void deleteProduct(Product product) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DatabaseConf.URL, DatabaseConf.USER, DatabaseConf.PASSWORD);
             PreparedStatement stmtClient = conn.prepareStatement(DELETE_PRODUCT)) {
            stmtClient.setInt(1, product.getId());
            stmtClient.execute();
            System.out.println("The client: " + product.getId() + " has been deleted");
        }
    }

    // * "DELETE FROM ingredient WHERE ingredient.ID = ?"
    @Override
    public void deleteIngredient(Ingredient ingredient) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DatabaseConf.URL, DatabaseConf.USER, DatabaseConf.PASSWORD);
             PreparedStatement stmtClient = conn.prepareStatement(DELETE_INGREDIENT)) {
            stmtClient.setInt(1, ingredient.getId());
            stmtClient.execute();
            System.out.println("The client: " + ingredient.getId() + " has been deleted");
        }
    }

    // * "DELETE FROM alergen WHERE alergen.ID = ?"
    @Override
    public void deleteAlergen(String alergen) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DatabaseConf.URL, DatabaseConf.USER, DatabaseConf.PASSWORD);
             PreparedStatement stmtClient = conn.prepareStatement(DELETE_ALERGEN)) {
            stmtClient.setString(1, alergen.getId());
            stmtClient.execute();
            System.out.println("The client: " + alergen.getId() + " has been deleted");
        }
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
    public List<String> findAlergensByIngredient(int id) throws SQLException {
        return null;
    }

    @Override
    public List<Product> findAll() throws SQLException {
        return List.of();
    }
}
