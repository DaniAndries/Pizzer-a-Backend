package controller.db.impl;

import controller.db.OrderDao;
import controller.db.ProductDao;
import model.*;
import utils.DatabaseConf;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.sql.DriverManager.getConnection;

/**
 * JdbcProductDao is an implementation of the {@link ProductDao} interface that provides
 * a comprehensive set of methods for performing CRUD (Create, Read, Update, Delete)
 * operations on products, ingredients, and allergens stored in a relational database using JDBC.
 *
 * <p>This class manages the database connection, ensuring proper handling of SQL queries
 * related to product data, ingredients, and allergens. It encapsulates the logic for interacting
 * with the underlying database and provides a clean API for other components of the application.</p>
 *
 * <p>Each method in this class is designed to throw a {@link SQLException} if an error occurs
 * during database operations, ensuring that the calling code can handle potential issues effectively.</p>
 *
 * @see ProductDao
 * @see Product
 * @see Ingredient
 *
 * @author DaniAndries
 * @version 0.1
 */

public class JdbcProductDao implements ProductDao {
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

    /**
     * Default constructor for JdbcProductDao.
     * <p>
     * This constructor initializes a new instance of JdbcProductDao.
     * It can be used to create an instance of this class without
     * providing any specific parameters.
     * </p>
     */
    public JdbcProductDao() {
        // Empty constructor
    }

    /**
     * Saves a product to the database. This method will handle different product types (Pizza, Pasta, Drink)
     * and their associated ingredients.
     *
     * @param product the product to be saved.
     * @throws SQLException if a database access error occurs.
     */
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

    /**
     * Adds a list of ingredients to the product by saving each ingredient and associating it with the product.
     *
     * @param ingredients the list of ingredients to add.
     * @param productId the ID of the product to which the ingredients are associated.
     * @param conn the database connection to use.
     * @throws SQLException if a database access error occurs.
     */
    private void addIngredient(List<Ingredient> ingredients, int productId, Connection conn) throws SQLException {
        for (Ingredient ingredient : ingredients) {
            saveIngredient(ingredient, productId, conn);
        }
    }

    /**
     * Saves an ingredient to the database and associates it with a product.
     *
     * @param ingredient the ingredient to be saved.
     * @param productId the ID of the product to which the ingredient is associated.
     * @param conn the database connection to use.
     * @throws SQLException if a database access error occurs.
     */
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

    /**
     * Associates a product with an ingredient in the PRODUCT_INGREDIENT table.
     *
     * @param ingredientId the ID of the ingredient to associate.
     * @param productId the ID of the product to associate with the ingredient.
     * @param conn the database connection to use.
     * @throws SQLException if a database access error occurs.
     */
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

    /**
     * Adds allergens associated with an ingredient by saving each allergen to the database.
     *
     * @param alergens the list of allergens to add.
     * @param ingredientId the ID of the ingredient to which the allergens are associated.
     * @param conn the database connection to use.
     * @throws SQLException if a database access error occurs.
     */
    private void addAlergen(List<String> alergens, int ingredientId, Connection conn) throws SQLException {
        for (String alergen : alergens) {
            saveAlergen(alergen, ingredientId, conn);
        }
    }

    /**
     * Saves an allergen to the database and associates it with an ingredient.
     *
     * @param alergen the name of the allergen to be saved.
     * @param ingredientId the ID of the ingredient to which the allergen is associated.
     * @param conn the database connection to use.
     * @throws SQLException if a database access error occurs.
     */
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

    /**
     * Saves a new ingredient-alergen association in the database.
     *
     * @param ingredientId the ID of the ingredient
     * @param alergen the ID of the alergen
     * @param conn the database connection
     * @throws SQLException if a database access error occurs
     */
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

    /**
     * Updates an existing product in the database.
     *
     * @param product the product to update
     * @throws SQLException if a database access error occurs
     */
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

    /**
     * Updates the name of an existing ingredient in the database.
     *
     * @param ingredient the ingredient to update
     * @throws SQLException if a database access error occurs
     */
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

    /**
     * Updates the name of an existing alergen in the database.
     *
     * @param alergen the new name of the alergen
     * @param id the ID of the alergen to update
     * @throws SQLException if a database access error occurs
     */
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

    /**
     * Deletes a product from the database.
     *
     * @param product the product to delete
     * @throws SQLException if a database access error occurs
     * @throws IllegalArgumentException if the product does not exist
     */
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

    /**
     * Deletes an ingredient from the database.
     *
     * @param ingredient the ingredient to delete
     * @throws SQLException if a database access error occurs
     */
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

    /**
     * Deletes an alergen from the database.
     *
     * @param id the ID of the alergen to delete
     * @throws SQLException if a database access error occurs
     */
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

    /**
     * Finds a product by its ID in the database.
     *
     * @param id the ID of the product to find
     * @return the product if found, or null if not found
     * @throws SQLException if a database access error occurs
     * @throws IllegalArgumentException if an unexpected value is encountered
     */
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

    /**
     * Finds an ingredient by its ID in the database.
     *
     * @param id the ID of the ingredient to find
     * @return the ingredient if found, or null if not found
     * @throws SQLException if a database access error occurs
     */
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

    /**
     * Finds an ingredient by its name in the database.
     *
     * @param name the name of the ingredient to find
     * @param conn the database connection
     * @return the ingredient if found, or null if not found
     * @throws SQLException if a database access error occurs
     */
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

    /**
     * Finds an alergen by its name in the database.
     *
     * @param name the name of the alergen to find
     * @param conn the database connection
     * @return the name of the alergen if found, or null if not found
     * @throws SQLException if a database access error occurs
     */
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

    /**
     * Finds all ingredients associated with a given product.
     *
     * @param id the ID of the product
     * @return a list of ingredients associated with the product
     * @throws SQLException if a database access error occurs
     */
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

    /**
     * Finds all alergens associated with a given ingredient.
     *
     * @param ingredient the ingredient to find alergens for
     * @return a list of alergen names associated with the ingredient
     * @throws SQLException if a database access error occurs
     */
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

    /**
     * Finds all products in the database.
     *
     * @return a list of all products
     * @throws SQLException if a database access error occurs
     */
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
