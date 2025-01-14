package controller.db.impl;

import controller.db.ClientDao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import model.Client;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JpaClientDao implements ClientDao {
    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");

    @Override
    public void save(Client client) throws SQLException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(client);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Override
    public void delete(Client client) throws SQLException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.remove(client);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Override
    public void update(Client client) throws SQLException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.merge(client);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Override
    public Client findById(int id) throws SQLException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Client cliente = entityManager.find(Client.class, id);
        entityManager.close();
        return cliente;
    }

    @Override
    public Client findByMail(String mail) throws SQLException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<Client> client = new ArrayList<>();
        try {
            client = entityManager.createQuery(
                            "SELECT c FROM Client c WHERE c.name = :name", Client.class)
                    .setParameter("mail", mail)
                    .getResultList();
        } catch (Exception e) {
            if (client.size() == 1) return client.getFirst();
            else if (client.size() > 1) throw new IllegalArgumentException("There is more than one user with this mail");
            else return new Client();
        } finally {
            entityManager.close();
        }
        if (client.size() == 1) return client.getFirst();
        else return new Client();
    }

    @Override
    public List<Client> findAll() throws SQLException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<Client> clients = new ArrayList<>();
        try {
            clients = entityManager.createQuery(
                            "SELECT * FROM Client", Client.class)
                    .getResultList();
        } catch (Exception e) {
            return clients;
        } finally {
            entityManager.close();
        }
        return clients;

    }
}
