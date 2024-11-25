package controller.db;

import model.Client;
import model.Product;

import java.sql.SQLException;

public interface ProductDao {
    void save(Product product) throws SQLException;

    void delete(Product product) throws SQLException;

    void update(Product product) throws SQLException;

    Client findById(int id) throws SQLException;
}
