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
import java.util.Objects;

public class JpaProductDao implements ProductDao {
    private static final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");


    @Override
    public void saveProduct(Product product) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();

            Objects.requireNonNull(product, "El producto no puede ser nulo");

            // Procesar ingredientes si el producto es Pizza o Pasta
            if (product instanceof Pizza) {
                Pizza pizza = (Pizza) product;
                List<Ingredient> checkedIngredients = new ArrayList<>();
                for (Ingredient ingredient : pizza.getIngredients()) {
                    // Buscar ingrediente por nombre
                    Ingredient existingIngredient = entityManager.createQuery(
                                    "SELECT i FROM Ingredient i WHERE i.name = :name", Ingredient.class)
                            .setParameter("name", ingredient.getName())
                            .getResultStream()
                            .findFirst()
                            .orElse(null);

                    if (existingIngredient != null) {
                        // Usar ingrediente existente
                        checkedIngredients.add(existingIngredient);
                    } else {
                        // Persistir nuevo ingrediente
                        entityManager.persist(ingredient);
                        checkedIngredients.add(ingredient);
                    }
                }
                pizza.setIngredients(checkedIngredients);
            } else if (product instanceof Pasta) {
                Pasta pasta = (Pasta) product;
                List<Ingredient> checkedIngredients = new ArrayList<>();
                for (Ingredient ingredient : pasta.getIngredients()) {
                    // Buscar ingrediente por nombre
                    Ingredient existingIngredient = entityManager.createQuery(
                                    "SELECT i FROM Ingredient i WHERE i.name = :name", Ingredient.class)
                            .setParameter("name", ingredient.getName())
                            .getResultStream()
                            .findFirst()
                            .orElse(null);

                    if (existingIngredient != null) {
                        // Usar ingrediente existente
                        checkedIngredients.add(existingIngredient);
                    } else {
                        // Persistir nuevo ingrediente
                        entityManager.persist(ingredient);
                        checkedIngredients.add(ingredient);
                    }
                }
                pasta.setIngredients(checkedIngredients);
            }

            // Verificar si el producto ya existe
            Product existingProduct = entityManager.createQuery(
                            "SELECT p FROM Product p WHERE p.name = :name", Product.class)
                    .setParameter("name", product.getName())
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
            if (existingProduct != null) {
                // Actualizar producto existente
                existingProduct.setPrice(product.getPrice());
                existingProduct.setSize(product.getSize());
                if (product instanceof Pizza && existingProduct instanceof Pizza) {
                    ((Pizza) existingProduct).setIngredients(((Pizza) product).getIngredients());
                } else if (product instanceof Pasta && existingProduct instanceof Pasta) {
                    ((Pasta) existingProduct).setIngredients(((Pasta) product).getIngredients());
                }
                entityManager.merge(existingProduct);
            } else {
                // Persistir nuevo producto
                entityManager.persist(product);
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
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
    public void saveAlergen(String allergen, int ingredientId, Connection conn) throws SQLException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(allergen);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Override
    public void deleteProduct(Product product) throws SQLException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();

            Product existingProduct = entityManager.find(Product.class, product.getId());
            if (existingProduct == null) {
                throw new IllegalArgumentException("No se ha podido encontrar el producto.");
            }

            entityManager.remove(existingProduct);

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new IllegalStateException("Error al eliminar el producto: " + e.getMessage(), e);
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
            String allergen = entityManager.find(String.class, id); // Supongamos que el alérgeno es una entidad gestionada
            if (allergen != null) {
                entityManager.remove(allergen);
                entityManager.getTransaction().commit();
            } else {
                throw new SQLException("Allergen with ID " + id + " not found");
            }
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new SQLException("Error deleting allergen: " + id, e);
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
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            Product product = entityManager.find(Product.class, id);
            if (product != null) {
                if (product instanceof Pizza) {
                    Hibernate.initialize(((Pizza) product).getIngredients());
                } else if (product instanceof Pasta) {
                    Hibernate.initialize(((Pasta) product).getIngredients());
                }
                return product;
            }
            return null;
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
