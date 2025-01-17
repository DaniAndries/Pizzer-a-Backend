package controller.db.impl;

import controller.db.ProductDao;
import jakarta.persistence.*;
import model.Ingredient;
import model.Pasta;
import model.Pizza;
import model.Product;
import org.hibernate.Hibernate;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class JpaProductDao implements ProductDao {
    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");


    @Override
    public void saveProduct(Product product) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();

            // Process ingredients first if it's a Pizza or Pasta
            if (product instanceof Pizza pizza || product instanceof Pasta pasta) {
                List<Ingredient> ingredients = (product instanceof Pizza) ?
                        ((Pizza) product).getIngredients() :
                        ((Pasta) product).getIngredients();

                if (ingredients != null && !ingredients.isEmpty()) {
                    List<Ingredient> managedIngredients = new ArrayList<>();

                    for (Ingredient ingredient : ingredients) {
                        try {
                            // Try to find existing ingredient by name
                            Ingredient existingIngredient = entityManager
                                    .createQuery("SELECT i FROM Ingredient i WHERE i.name = :name", Ingredient.class)
                                    .setParameter("name", ingredient.getName())
                                    .getSingleResult();

                            // Update allergens and merge to ensure changes are persisted
                            existingIngredient.setAllergens(ingredient.getAllergens());
                            entityManager.merge(existingIngredient);
                            managedIngredients.add(existingIngredient);

                        } catch (NoResultException e) {
                            // If ingredient doesn't exist, persist new one
                            Ingredient newIngredient = new Ingredient();
                            newIngredient.setName(ingredient.getName());
                            newIngredient.setAllergens(ingredient.getAllergens());
                            entityManager.persist(newIngredient);
                            managedIngredients.add(newIngredient);
                        }
                    }

                    // Update the product with managed ingredients
                    if (product instanceof Pizza pizza) {
                        pizza.setIngredients(managedIngredients);
                    } else if (product instanceof Pasta pasta) {
                        pasta.setIngredients(managedIngredients);
                    }
                }
            }

            // Now handle the product
            if (product.getId() != 0) {
                entityManager.merge(product);
            } else {
                entityManager.persist(product);
            }

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new RuntimeException("Error while saving the product", e);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void saveProductIngredient(int ingredientId, int productId, Connection conn) throws SQLException {

    }

    @Override
    public void saveIngredient(Ingredient ingredient, int productId, Connection conn) throws SQLException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(ingredient);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Override
    public void saveIngredientAlergen(int ingredientId, int alergen, Connection conn) throws SQLException {

    }

    @Override
    public void saveAlergen(String alergen, int ingredientId, Connection conn) throws SQLException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(alergen);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Override
    public void deleteProduct(Product product) throws SQLException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();

            // Make sure the customer is managed before removing it
            Product managedCustomer = entityManager.contains(product) ? product : entityManager.merge(product);

            entityManager.remove(managedCustomer);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new SQLException("Error deleting customer: " + product, e);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void deleteIngredient(Ingredient ingredient) throws SQLException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();

            // Make sure the customer is managed before removing it
            Ingredient managedCustomer = entityManager.contains(ingredient) ? ingredient : entityManager.merge(ingredient);

            entityManager.remove(managedCustomer);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new SQLException("Error deleting customer: " + ingredient, e);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void deleteAlergen(int id) throws SQLException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();

            // Make sure the customer is managed before removing it
            int managedCustomer = entityManager.contains(id) ? id : entityManager.merge(id);

            entityManager.remove(managedCustomer);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new SQLException("Error deleting customer: " + id, e);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void updateProduct(Product product) throws SQLException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.merge(product);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Override
    public void updateIngredient(Ingredient ingredient) throws SQLException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.merge(ingredient);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Override
    public void updateAlergen(String alergen, int id) throws SQLException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.merge(alergen);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Override
    public Product findProductById(int id) throws SQLException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try (entityManager) {
            Product product = entityManager.find(Product.class, id);
            if (product != null) {
                if (product instanceof Pizza) {
                    Hibernate.initialize(((Pizza) product).getIngredients());
                } else if (product instanceof Pasta) {
                    Hibernate.initialize(((Pasta) product).getIngredients());
                }
                return product;
            } else {
                return null;
            }
        }
    }

    @Override
    public Ingredient findIngredientsById(int id) throws SQLException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Ingredient ingredient = entityManager.find(Ingredient.class, id);
        entityManager.close();
        return ingredient;
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
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return entityManager.createQuery("SELECT p FROM Product p", Product.class).getResultList();
        }
    }
}
