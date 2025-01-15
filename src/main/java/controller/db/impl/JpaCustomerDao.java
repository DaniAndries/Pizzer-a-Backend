package controller.db.impl;

import controller.db.CustomerDao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import model.Customer;

import java.sql.SQLException;
import java.util.List;

public class JpaCustomerDao implements CustomerDao {
    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");

    @Override
    public void save(Customer customer) throws SQLException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(customer);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Override
    public void delete(Customer customer) throws SQLException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();

            // Make sure the customer is managed before removing it
            Customer managedCustomer = entityManager.contains(customer)
                    ? customer
                    : entityManager.merge(customer);

            entityManager.remove(managedCustomer);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new SQLException("Error deleting customer: " + customer, e);
        } finally {
            entityManager.close();
        }
    }


    @Override
    public void update(Customer customer) throws SQLException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.merge(customer);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Override
    public Customer findById(int id) throws SQLException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Customer customer = entityManager.find(Customer.class, id);
        entityManager.close();
        return customer;
    }

    @Override
    public Customer findByMail(String mail) throws SQLException {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            List<Customer> customers = entityManager.createQuery(
                            "SELECT c FROM Customer c WHERE c.mail = :mail", Customer.class)
                    .setParameter("mail", mail)
                    .getResultList();

            if (customers.isEmpty()) {
                return null; // Devuelve null si no se encuentra ningún customere
            } else if (customers.size() > 1) {
                throw new IllegalArgumentException("There is more than one user with this mail");
            } else {
                return customers.getFirst(); // Devuelve el customere encontrado
            }
        }
    }

    @Override
    public List<Customer> findAll() throws SQLException {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return entityManager.createQuery("SELECT c FROM Customer c", Customer.class)
                    .getResultList();
        }
    }
}
