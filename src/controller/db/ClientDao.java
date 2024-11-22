package controller.db;

import model.Client;

import java.sql.SQLException;
import java.util.List;

public interface ClientDao {
    void save(Client client) throws SQLException;

    void delete(Client client) throws SQLException;

    void update(Client client) throws SQLException;

    Client findById(int id) throws SQLException;

    Client findByMail(String dni) throws SQLException;

    List<Client> findAll() throws SQLException;
}
