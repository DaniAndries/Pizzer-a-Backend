package controller.db.impl;

import controller.db.OrderDao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import model.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class JpaOrderDao implements OrderDao {
    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");

    @Override
    public void saveOrder(Order order) throws SQLException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();

            // Use merge instead of persist for detached entities
            Order managedOrder = entityManager.merge(order);

            transaction.commit(); // Commit the transaction
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback(); // Rollback if the transaction is active
            }
            throw e; // Rethrow the exception
        } finally {
            if (entityManager.isOpen()) {
                entityManager.close(); // Close the EntityManager only if it's open
            }
        }
    }

    @Override
    public void saveOrderLine(OrderLine orderLine, Order order) throws SQLException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(orderLine);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Override
    public void saveOrderLine(OrderLine orderLine, Order order, Connection conn) throws SQLException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(orderLine);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Override
    public void deleteOrder(Order order) throws SQLException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();

            // Make sure the customer is managed before removing it
            Order managedCustomer = entityManager.contains(order)
                    ? order
                    : entityManager.merge(order);

            entityManager.remove(managedCustomer);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new SQLException("Error deleting customer: " + order, e);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void deleteOrderLine(OrderLine orderLine) throws SQLException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();

            // Make sure the customer is managed before removing it
            OrderLine managedCustomer = entityManager.contains(orderLine)
                    ? orderLine
                    : entityManager.merge(orderLine);

            entityManager.remove(managedCustomer);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new SQLException("Error deleting customer: " + orderLine, e);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void updateOrder(Order order) throws SQLException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.merge(order);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Override
    public void updateOrderLine(OrderLine orderLine) throws SQLException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.merge(orderLine);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Override
    public Order findOrder(int id) throws SQLException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Order order = entityManager.find(Order.class, id);
        entityManager.close();
        return order;
    }

    @Override
    public List<Order> findOrdersByCustomer(Customer customer) throws SQLException {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return entityManager.createQuery(
                            "SELECT o FROM Order o WHERE o.customer = :customer", Order.class)
                    .setParameter("customer", customer)
                    .getResultList();
        }
    }

    @Override
    public List<Order> findOrdersByState(OrderState state, Customer customer) throws SQLException {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return entityManager.createQuery(
                            "SELECT o FROM Order o WHERE o.state = :state AND o.customer = :customer", Order.class)
                    .setParameter("state", state)
                    .setParameter("customer", customer)
                    .getResultList();
        }
    }

    @Override
    public OrderLine findOrderLine(int id) throws SQLException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        OrderLine orderLine = entityManager.find(OrderLine.class, id);
        entityManager.close();
        return orderLine;
    }

    @Override
    public List<OrderLine> findOrderLinesByOrder(Order order) throws SQLException {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return entityManager.createQuery(
                            "SELECT ol FROM OrderLine ol WHERE ol.order = :order", OrderLine.class)
                    .setParameter("order", order)
                    .getResultList();
        }
    }
}
