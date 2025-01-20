package controller.db.impl;

import controller.db.OrderDao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import model.*;
import model.Customer;
import org.hibernate.Hibernate;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JpaOrderDao implements OrderDao {
    private final EntityManagerFactory entityManagerFactory;

    public JpaOrderDao() {
        this.entityManagerFactory = Persistence.createEntityManagerFactory("default");
    }

    @Override
    public void saveOrder(Order order) throws SQLException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();

            // Asegurarnos de que el cliente no cause conflicto de duplicados
            Customer client = order.getCustomer();
            if (client != null) {
                Customer existingClient = entityManager.find(Customer.class, client.getId());
                if (existingClient != null) {
                    // Usar el cliente ya existente
                    order.setCustomer(existingClient);
                } else {
                    // Persistir cliente si no existe
                    entityManager.persist(client);
                }
            }

            // Persistir o actualizar productos en las líneas de pedido
            for (OrderLine lineOrder : order.getOrderLines()) {
                Product product = lineOrder.getProduct();
                if (product != null) {
                    if (product.getId() == 0) {
                        entityManager.persist(product);
                    } else {
                        entityManager.merge(product);
                    }
                }
            }

            // Usar merge para garantizar que la orden esté gestionada correctamente
            entityManager.merge(order);

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            e.printStackTrace();
            throw new SQLException("Error inserting the order.", e);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void deleteOrder(Order order) throws SQLException{
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();

            Order newOrder = findOrder(order.getId());
            if (newOrder == null) {
                throw new IllegalArgumentException("No order found with the given ID.");
            } else {
                newOrder = entityManager.merge(newOrder);
                entityManager.remove(newOrder);
            }

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            e.printStackTrace();
            throw new SQLException("Error deleting the order.", e);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void deleteOrderLine(OrderLine orderLine) throws SQLException {

    }

    @Override
    public void updateOrder(Order order) throws SQLException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(order);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            e.printStackTrace();
            throw new SQLException("Error updating the order.", e);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void updateOrderLine(OrderLine orderLine) throws SQLException {

    }

    @Override
    public Order findOrder(int id) throws SQLException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            Order order = entityManager.find(Order.class, id);
            if (order != null) {
                Hibernate.initialize(order.getOrderLines());
            }
            return order;
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List<Order> findOrdersByCustomer(Customer customer) throws SQLException {
        return List.of();
    }

    @Override
    public List<Order> findOrdersByState(OrderState state, model.Customer customer) throws SQLException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<Order> orders = new ArrayList<>();
        try {
            entityManager.getTransaction().begin();
            orders = entityManager
                    .createQuery("SELECT o FROM Order o WHERE o.state = :state", Order.class)
                    .setParameter("state", state.name())
                    .getResultList();

            for (Order order : orders) {
                Hibernate.initialize(order.getOrderLines());
            }
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            e.printStackTrace();
            throw new SQLException("Error finding orders by status.", e);
        } finally {
            entityManager.close();
        }
        return orders;
    }

    @Override
    public OrderLine findOrderLine(int id) throws SQLException {
        return null;
    }

    @Override
    public List<OrderLine> findOrderLinesByOrder(Order order) throws SQLException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<OrderLine> lineOrders = new ArrayList<>();
        try {
            entityManager.getTransaction().begin();
            Order newOrder = entityManager.find(Order.class, order.getId());
            if (newOrder == null) {
                throw new SQLException("Order not found.");
            } else {
                lineOrders = newOrder.getOrderLines();
                lineOrders.size(); // Force initialization
            }
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            e.printStackTrace();
            throw new SQLException("Error finding line orders by order ID.", e);
        } finally {
            entityManager.close();
        }
        return lineOrders;
    }

    @Override
    public void saveOrderLine(List<OrderLine> orderLine, Order order) throws SQLException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();

            Order newOrder = entityManager.find(Order.class, order.getId());
            if (newOrder == null) {
                throw new SQLException("Order not found.");
            } else {
                for (OrderLine lineOrder : newOrder.getOrderLines()) {
                    lineOrder.setOrder(newOrder);
                    entityManager.persist(lineOrder);
                }
                newOrder.setOrderLines(orderLine);
            }

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            e.printStackTrace();
            throw new SQLException("Error adding line orders to the order.", e);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void saveOrderLine(OrderLine orderLine, Order order, Connection conn) throws SQLException {

    }

}
