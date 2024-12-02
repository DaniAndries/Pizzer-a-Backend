package controller.db.impl;

import controller.db.ProductDao;
import model.*;
import utils.DatabaseConf;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.sql.DriverManager.getConnection;

public class JdbcProductDao implements ProductDao {
    static final JdbcOrderDao jdbcCarDao = new JdbcOrderDao();

    private static final String INSERT_PRODUCT = "INSERT INTO product (product_name, price, size, type) VALUES (?,?,?,?)";
    private static final String INSERT_INGREDIENT = "INSERT INTO ingredient (ingredient_name) VALUES (?)";
    private static final String INSERT_ALERGEN = "INSERT INTO alergen (alergen_name) VALUES (?)";

    private static final String INSERT_PRODUCT_INGREDIENT = "INSERT INTO PRODUCT_INGREDIENT (product, ingredient) VALUES (?,?)";
    private static final String INSERT_INGREDIENT_ALERGEN = "INSERT INTO INGREDIENT_ALERGEN (ingredient, alergen) VALUES (?,?)";

    private static final String UPDATE_PRODUCT = "UPDATE product SET product.product_name=?, product.price=?, product.size=?, product.type=? WHERE product.id = ?";
    private static final String UPDATE_INGREDIENT = "UPDATE ingredient SET ingredient.ingredient_name=? WHERE product.id = ?";
    private static final String UPDATE_ALERGEN = "UPDATE alergen SET alergen_name.alergen_name_name=? WHERE product.id = ?";

    private static final String DELETE_PRODUCT = "DELETE FROM product WHERE product.ID = ?";
    private static final String DELETE_INGREDIENT = "DELETE FROM ingredient WHERE ingredient.ID = ?";
    private static final String DELETE_ALERGEN = "DELETE FROM alergen WHERE alergen.ID = ?";


    private static final String SELECT_PRODUCT_BY_ID = "SELECT product.id, product.product_name, product.price, product.size, product.type FROM product WHERE product.ID = ?";
    private static final String SELECT_INGREDIENT_BY_ID = "SELECT ingredient.id, ingredient.ingredient_name FROM ingredient WHERE ingredient.id = ?";
    private static final String SELECT_INGREDIENT_BY_NAME = "SELECT ingredient.id, ingredient.ingredient_name FROM ingredient WHERE ingredient.ingredient_name = ?";
    private static final String SELECT_ALERGEN_BY_NAME = "SELECT alergen.alergen_name FROM alergen WHERE alergen.alergen_name = ?";
    private static final String SELECT_INGREDIENTS_BY_PRODUCT = "SELECT ingredient.id, ingredient.ingredient_name FROM product_ingredient INNER JOIN ingredient ON product_ingredient.ingredient = ingredient.id WHERE product_ingredient.product = ? ORDER BY ingredient.id";
    private static final String SELECT_ALERGENS_BY_INGREDIENT = "SELECT alergen.id, alergen.alergen_name FROM ingredient_alergen INNER JOIN alergen ON ingredient_alergen.alergen = alergen.id WHERE ingredient_alergen.ingredient = ? ORDER BY alergen.id";
    private static final String SELECT_ALL = "SELECT product.id, product.product_name, product.price, product.size, product.type FROM product";

    // * "INSERT INTO product (product_name, price, size, type) VALUES (?,?,?,?)"
    @Override
    public void saveProduct(Product product) throws SQLException {
        try (Connection conn = getConnection(DatabaseConf.URL, DatabaseConf.USER, DatabaseConf.PASSWORD);
             PreparedStatement stmtProduct = conn.prepareStatement(INSERT_PRODUCT, Statement.RETURN_GENERATED_KEYS)) {
            List<Ingredient> ingredients = new ArrayList<>();

            stmtProduct.setString(1, product.getName());
            stmtProduct.setDouble(2, product.getPrice());

            if (product instanceof Pizza pizza) {
                stmtProduct.setString(3, pizza.getSize().toString());
                stmtProduct.setString(4, "PIZZA");
                ingredients = pizza.getIngredients();
            }

            if (product instanceof Pasta pasta) {
                stmtProduct.setNull(3, Types.NULL);
                stmtProduct.setString(4, "PASTA");
                ingredients = pasta.getIngredients();
            }

            if (product instanceof Drink drink) {
                stmtProduct.setString(3, drink.getSize().toString().toUpperCase());
                stmtProduct.setString(4, "DRINK");
            }

            stmtProduct.executeUpdate();

            try (ResultSet generatedKeys = stmtProduct.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    product.setId(generatedKeys.getInt(1));
                }
            }

            if (ingredients != null && !ingredients.isEmpty()) addIngredient(ingredients, product.getId(), conn);

            System.out.println("The product: " + product.getId() + " has been created");
        }
    }

    private void addIngredient(List<Ingredient> ingredients, int productId, Connection conn) throws SQLException {
        for (Ingredient ingredient : ingredients) {
            saveIngredient(ingredient, productId, conn);
        }
    }

    // * "INSERT INTO ingredient (ingredient_name) VALUES (?)"
    @Override
    public void saveIngredient(Ingredient ingredient, int productId, Connection conn) throws SQLException {
        try (PreparedStatement stmtIngredient = conn.prepareStatement(INSERT_INGREDIENT, Statement.RETURN_GENERATED_KEYS)) {
            Ingredient existingIngredient = findIngredientsByName(ingredient.getName(), conn);


            if (existingIngredient != null) {
                System.out.println("Ingredient '" + ingredient.getName() + "' already exists.");
                saveProductIngredient(ingredient.getId(), productId, conn);
                return;
            }

            stmtIngredient.setString(1, ingredient.getName());

            stmtIngredient.executeUpdate();

            try (ResultSet generatedKeys = stmtIngredient.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    ingredient.setId(generatedKeys.getInt(1));
                }
            }

            saveProductIngredient(ingredient.getId(), productId, conn);
            addAlergen(ingredient.getAlergens(), ingredient.getId(), conn);

            System.out.println("The ingredient: " + ingredient.getId() + " has been created");
        }
    }

    // * "INSERT INTO PRODUCT_INGREDIENT (product, ingredient) VALUES (?,?)"
    @Override
    public void saveProductIngredient(int ingredientId, int productId, Connection conn) throws SQLException {
        try (PreparedStatement stmtIngredient = conn.prepareStatement(INSERT_PRODUCT_INGREDIENT, Statement.RETURN_GENERATED_KEYS)) {
            stmtIngredient.setInt(1, productId);
            stmtIngredient.setInt(2, ingredientId);

            stmtIngredient.executeUpdate();

            System.out.println("Product-Ingredient added");
        }
    }

    private void addAlergen(List<String> alergens, int ingredientId, Connection conn) throws SQLException {
        for (String alergen : alergens) {
            saveAlergen(alergen, ingredientId, conn);
        }
    }

    // * "INSERT INTO alergen (alergen_name) VALUES (?)"
    @Override
    public void saveAlergen(String alergen, int ingredientId, Connection conn) throws SQLException {
        try (PreparedStatement stmtAlergen = conn.prepareStatement(INSERT_ALERGEN, Statement.RETURN_GENERATED_KEYS)) {

            String alergenAux = findAlergensByName(alergen, conn);

            if (alergenAux != null) {
                System.out.println("Alergen '" + alergenAux + "' already exists.");
                return;
            }

            stmtAlergen.setString(1, alergen);

            stmtAlergen.executeUpdate();

            try (ResultSet generatedKeys = stmtAlergen.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    saveIngredientAlergen(ingredientId, generatedKeys.getInt(1), conn);
                }
            }

            System.out.println("The alergen: " + alergen + " has been created");
        }
    }

    // * "INSERT INTO INGREDIENT_ALERGEN (ingredient, alergen) VALUES (?,?)"
    @Override
    public void saveIngredientAlergen(int ingredientId, int alergen, Connection conn) throws SQLException {
        try (PreparedStatement stmtIngredient = conn.prepareStatement(INSERT_INGREDIENT_ALERGEN, Statement.RETURN_GENERATED_KEYS)) {
            stmtIngredient.setInt(1, ingredientId);
            stmtIngredient.setInt(2, alergen);

            stmtIngredient.executeUpdate();

            System.out.println("Ingredient-Alergen added");
        }
    }

    // * "UPDATE product SET product.product_name=?, product.price=?, product.size=?, product.type=? WHERE product.id = ?"
    @Override
    public void updateProduct(Product product) throws SQLException {
        try (Connection conn = getConnection(DatabaseConf.URL, DatabaseConf.USER, DatabaseConf.PASSWORD);
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
            System.out.println("The product: " + product.getId() + " has been modified");
        }
    }

    // * "UPDATE ingredient SET ingredient.ingredient_name=? WHERE product.id = ?"
    @Override
    public void updateIngredient(Ingredient ingredient) throws SQLException {
        try (Connection conn = getConnection(DatabaseConf.URL, DatabaseConf.USER, DatabaseConf.PASSWORD);
             PreparedStatement stmtClient = conn.prepareStatement(UPDATE_INGREDIENT)) {
            stmtClient.setString(1, ingredient.getName());
            stmtClient.setInt(2, ingredient.getId());

            stmtClient.execute();
            System.out.println("The ingredient: " + ingredient.getId() + " has been modified");
        }
    }

    // * "UPDATE alergen SET alergen.alergen_name=? WHERE product.id = ?"
    @Override
    public void updateAlergen(String alergen, int id) throws SQLException {
        try (Connection conn = getConnection(DatabaseConf.URL, DatabaseConf.USER, DatabaseConf.PASSWORD);
             PreparedStatement stmtClient = conn.prepareStatement(UPDATE_ALERGEN)) {
            stmtClient.setString(1, alergen);
            stmtClient.setInt(2, id);

            stmtClient.execute();
            System.out.println("The alergen: " + alergen + " has been modified");
        }
    }

    // * "DELETE FROM product WHERE product.ID = ?"
    @Override
    public void deleteProduct(Product product) throws SQLException {
        if (findProductById(product.getId())== null){
            throw new IllegalArgumentException("Invalid or non-existent product");
        }
        try (Connection conn = getConnection(DatabaseConf.URL, DatabaseConf.USER, DatabaseConf.PASSWORD);
             PreparedStatement stmtClient = conn.prepareStatement(DELETE_PRODUCT)) {
            stmtClient.setInt(1, product.getId());
            stmtClient.execute();
            System.out.println("The product: " + product.getId() + " has been deleted");
        }
    }

    // * "DELETE FROM ingredient WHERE ingredient.ID = ?"
    @Override
    public void deleteIngredient(Ingredient ingredient) throws SQLException {
        try (Connection conn = getConnection(DatabaseConf.URL, DatabaseConf.USER, DatabaseConf.PASSWORD);
             PreparedStatement stmtClient = conn.prepareStatement(DELETE_INGREDIENT)) {
            stmtClient.setInt(1, ingredient.getId());
            stmtClient.execute();
            System.out.println("The ingredient: " + ingredient.getId() + " has been deleted");
        }
    }

    // * "DELETE FROM alergen WHERE alergen.ID = ?"
    @Override
    public void deleteAlergen(int id) throws SQLException {
        try (Connection conn = getConnection(DatabaseConf.URL, DatabaseConf.USER, DatabaseConf.PASSWORD);
             PreparedStatement stmtClient = conn.prepareStatement(DELETE_ALERGEN)) {
            stmtClient.setInt(1, id);
            stmtClient.execute();
            System.out.println("The alergen: " + id + " has been deleted");
        }
    }

    // * "SELECT product.id, product.product_name, product.price, product.size, product.type FROM product WHERE product.ID = ? ORDER BY product.id"
    @Override
    public Product findProductById(int id) throws SQLException, IllegalArgumentException {
        Product product;
        try (Connection conn = getConnection(DatabaseConf.URL, DatabaseConf.USER, DatabaseConf.PASSWORD);
             PreparedStatement stmtIngredient = conn.prepareStatement(SELECT_PRODUCT_BY_ID)) {
            stmtIngredient.setInt(1, id);
            try (ResultSet rsProduct = stmtIngredient.executeQuery()) {
                if (rsProduct.next()) {
                    switch (rsProduct.getString("type")) {
                        case "PIZZA":
                            product = new Pizza(
                                    rsProduct.getInt("id"),
                                    rsProduct.getString("product_name"),
                                    rsProduct.getDouble("price"),
                                    Size.valueOf(rsProduct.getString("size"))
                            );
                            Pizza pizza = (Pizza) product;
                            pizza.setIngredients(findIngredientsByProduct(rsProduct.getInt("id")));
                            break;
                        case "PASTA":
                            product = new Pasta(
                                    rsProduct.getInt("id"),
                                    rsProduct.getString("product_name"),
                                    rsProduct.getDouble("price")
                            );
                            Pasta pasta = (Pasta) product;
                            pasta.setIngredients(findIngredientsByProduct(rsProduct.getInt("id")));
                            break;
                        case "DRINK":
                            product = new Drink(
                                    rsProduct.getInt("id"),
                                    rsProduct.getString("product_name"),
                                    rsProduct.getDouble("price"),
                                    Size.valueOf(rsProduct.getString("size"))
                            );
                            break;
                        default:
                            throw new IllegalArgumentException("Unexpected value: " + id);
                    }
                    return product;
                }
            }
            return null;
        }
    }

    // * "SELECT ingredient.id, ingredient.ingredient_name FROM ingredient WHERE ingredient.id = ?"
    @Override
    public Ingredient findIngredientsById(int id) throws SQLException {
        Ingredient ingredient;
        try (Connection conn = getConnection(DatabaseConf.URL, DatabaseConf.USER, DatabaseConf.PASSWORD);
             PreparedStatement stmtingredient = conn.prepareStatement(SELECT_INGREDIENT_BY_ID)) {
            stmtingredient.setInt(1, id);
            try (ResultSet rsIngredient = stmtingredient.executeQuery()) {
                if (rsIngredient.next()) {
                    ingredient = new Ingredient(
                            rsIngredient.getInt("id"),
                            rsIngredient.getString("ingredient_name")
                    );
                    ingredient.setAlergens(findAlergensByIngredient(ingredient));
                    return ingredient;
                }
            }
            return null;
        }
    }

    // * "SELECT ingredient.id, ingredient.ingredient_name FROM ingredient WHERE ingredient.ingredient_name = ?"
    @Override
    public Ingredient findIngredientsByName(String name, Connection conn) throws SQLException {
        Ingredient ingredient;
        try (PreparedStatement stmtIngredient = conn.prepareStatement(SELECT_INGREDIENT_BY_NAME)) {
            stmtIngredient.setString(1, name);
            try (ResultSet rsClient = stmtIngredient.executeQuery()) {
                if (rsClient.next()) {
                    ingredient = new Ingredient(
                            rsClient.getInt("id"),
                            rsClient.getString("ingredient_name")
                    );

                    List<String> alergens = findAlergensByIngredient(ingredient);

                    ingredient.setAlergens(alergens);

                    return ingredient;
                }
            }
            return null;
        }
    }

    // * "SELECT alergen.alergen_name FROM alergen WHERE alergen.alergen_name = ?"
    @Override
    public String findAlergensByName(String name, Connection conn) throws SQLException {
        try (PreparedStatement stmtIngredient = conn.prepareStatement(SELECT_ALERGEN_BY_NAME)) {
            stmtIngredient.setString(1, name);
            try (ResultSet rsAlergen = stmtIngredient.executeQuery()) {
                if (rsAlergen.next()) {
                    return rsAlergen.getString("alergen_name");
                }
            }
            return null;
        }
    }

    // * "SELECT ingredient.id, ingredient.ingredient_name FROM ingredient WHERE product_ingredient.product=? AND product_ingredient.ingredient=ingredient.id"
    @Override
    public List<Ingredient> findIngredientsByProduct(int id) throws SQLException {
        List<Ingredient> ingredients = new ArrayList<>();
        try (Connection conn = getConnection(DatabaseConf.URL, DatabaseConf.USER, DatabaseConf.PASSWORD);
             PreparedStatement stmtIngredient = conn.prepareStatement(SELECT_INGREDIENTS_BY_PRODUCT)) {
            stmtIngredient.setInt(1, id);
            try (ResultSet rsIngredient = stmtIngredient.executeQuery()) {
                while (rsIngredient.next()) {
                    ingredients.add(
                            new Ingredient(
                                    rsIngredient.getInt("id"),
                                    rsIngredient.getString("ingredient_name")
                            )
                    );
                    ingredients.getLast().setAlergens(findAlergensByIngredient(ingredients.getLast()));
                }
            }
            return ingredients;
        }
    }

    // * "SELECT alergen.id, alergen.alergen_name FROM ingredient_alergen INNER JOIN alergen ON ingredient_alergen.alergen = alergen.id WHERE ingredient_alergen.ingredient = ?"
    @Override
    public List<String> findAlergensByIngredient(Ingredient ingredient) throws SQLException {
        List<String> alergens = new ArrayList<>();
        try (Connection conn = getConnection(DatabaseConf.URL, DatabaseConf.USER, DatabaseConf.PASSWORD);
             PreparedStatement stmtAlergen = conn.prepareStatement(SELECT_ALERGENS_BY_INGREDIENT)) {
            stmtAlergen.setInt(1, ingredient.getId());
            try (ResultSet rsAlergen = stmtAlergen.executeQuery()) {
                while (rsAlergen.next()) {
                    alergens.add(rsAlergen.getString("alergen_name"));
                }
            }
            return alergens;
        }
    }

    // * "SELECT product.id, product.product_name, product.price, product.size, product.type FROM product"
    @Override
    public List<Product> findAll() throws SQLException {
        List<Product> products = new ArrayList<>();
        try (Connection conn = getConnection(DatabaseConf.URL, DatabaseConf.USER, DatabaseConf.PASSWORD);
             PreparedStatement stmtProduct = conn.prepareStatement(SELECT_ALL)) {
            try (ResultSet rsProduct = stmtProduct.executeQuery()) {
                while (rsProduct.next()) {
                    switch (rsProduct.getString("type")) {
                        case "PIZZA":
                            products.add(new Pizza(
                                    rsProduct.getInt("id"),
                                    rsProduct.getString("product_name"),
                                    rsProduct.getDouble("price"),
                                    Size.valueOf(rsProduct.getString("size"))
                            ));
                            Pizza pizza = (Pizza) products.getLast();
                            pizza.setIngredients(findIngredientsByProduct(rsProduct.getInt("id")));
                            break;
                        case "PASTA":
                            products.add(new Pasta(
                                    rsProduct.getInt("id"),
                                    rsProduct.getString("product_name"),
                                    rsProduct.getDouble("price")
                            ));
                            Pasta pasta = (Pasta) products.getLast();
                            pasta.setIngredients(findIngredientsByProduct(rsProduct.getInt("id")));
                            break;
                        case "DRINK":
                            products.add(new Drink(
                                    rsProduct.getInt("id"),
                                    rsProduct.getString("product_name"),
                                    rsProduct.getDouble("price"),
                                    Size.valueOf(rsProduct.getString("size"))
                            ));
                            break;
                    }
                }
                return products;
            }
        }
    }
}
