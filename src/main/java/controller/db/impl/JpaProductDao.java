package controller.db.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Query;
import org.hibernate.Hibernate;

import controller.db.ProductDao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import model.Ingredient;
import model.Pasta;
import model.Pizza;
import model.Product;

public class JpaProductDao implements ProductDao {
    private static final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");

    @Override
    public void saveProduct(Product product) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();

            // Verificar si el producto ya existe
            Product existingProduct = entityManager.createQuery(
                            "SELECT p FROM Product p WHERE p.name = :name", Product.class)
                    .setParameter("name", product.getName())
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

            if (existingProduct == null) {
                List<Ingredient> ingredientes = new ArrayList<>();
                if (product instanceof Pizza pizza) {
                    ingredientes = pizza.getIngredients();
                } else if (product instanceof Pasta pasta) {
                    ingredientes = pasta.getIngredients();
                }

                List<Ingredient> ingredientsWithId = new ArrayList<>();
                for (Ingredient ingredient : ingredientes) {
                    // Buscar si el ingrediente ya existe en la base de datos
                    Ingredient ingredientWithId = entityManager.createQuery(
                                    "SELECT i FROM Ingredient i WHERE i.name = :name", Ingredient.class)
                            .setParameter("name", ingredient.getName())
                            .getResultStream()
                            .findFirst()
                            .orElse(null);

                    if (ingredientWithId == null) {
                        // El ingrediente no existe, persistirlo
                        Ingredient managedIngredient = entityManager.merge(ingredient);
                        ingredientsWithId.add(managedIngredient);
                    } else {
                        // El ingrediente ya existe, actualizar sus alérgenos si es necesario
                        ingredientWithId.setAllergens(ingredient.getAllergens());
                        ingredientsWithId.add(ingredientWithId); // Agregar el ingrediente existente
                    }
                }

                // Actualizar los ingredientes en el producto
                if (product instanceof Pizza pizza) {
                    pizza.setIngredients(ingredientsWithId);
                } else if (product instanceof Pasta pasta) {
                    pasta.setIngredients(ingredientsWithId);
                }

                // Persistir el nuevo producto
                entityManager.merge(product);
            }

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback(); // Hacer rollback en caso de error
            }
            e.printStackTrace(); // Imprimir la traza de la excepción para más detalles
        } finally {
            entityManager.close(); // Asegúrate de cerrar el EntityManager
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
        entityManager.getTransaction().begin();

        try {
            // Get managed instance of the product
            Product managedProduct = entityManager.find(Product.class, product.getId());
            if (managedProduct == null) {
                throw new IllegalArgumentException("El producto con ID " + product.getId() + " no existe y no puede ser eliminado.");
            }

            // First, delete all entries from the join table for this product
            Query deleteJoinTableQuery = entityManager.createNativeQuery(
                    "DELETE FROM product_ingredient WHERE product_id = ?1"
            );
            deleteJoinTableQuery.setParameter(1, managedProduct.getId());
            deleteJoinTableQuery.executeUpdate();
            entityManager.detach(product);
            entityManager.merge(product);
            // Refresh the managed product to ensure its state is up-to-date
            entityManager.refresh(managedProduct);

            // Clear the ingredients collection in memory
            if (product instanceof Pizza pizza) {
                pizza.getIngredients().clear();
            } else if (product instanceof Pasta pasta) {
                pasta.getIngredients().clear();
            }

            entityManager.flush();

            // Finally remove the product
            entityManager.remove(managedProduct);
            entityManager.flush();

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new SQLException("Error al eliminar el producto: " + product, e);
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
            Ingredient managedCustomer = entityManager.contains(ingredient) ? ingredient
                    : entityManager.merge(ingredient);

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
            String allergen = entityManager.find(String.class, id); // Supongamos que el alérgeno es una entidad
                                                                    // gestionada
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
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            // Consultar el ingrediente por su nombre para obtener la versión persistida
            Ingredient persistedIngredient = entityManager.createQuery(
                            "SELECT i FROM Ingredient i WHERE i.name = :name", Ingredient.class)
                    .setParameter("name", ingredient.getName())
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

            // Si el ingrediente no existe, devolver una lista vacía
            if (persistedIngredient == null) {
                return List.of();
            }

            // Retornar los alérgenos del ingrediente persistido
            return persistedIngredient.getAllergens();
        } finally {
            entityManager.close();
        }
    }


    @Override
    public List<Product> findAll() throws SQLException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<Product> listaProductos = new ArrayList<>();

        try {
            listaProductos = entityManager.createQuery("SELECT c FROM Product c", Product.class).getResultList();
            for (Product producto : listaProductos) {
                if (producto instanceof Pizza pizza) {
                    Hibernate.initialize(pizza.getIngredients());
                } else if (producto instanceof Pasta pasta) {
                    Hibernate.initialize(pasta.getIngredients());
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
        return listaProductos;
    }
}
