package controller;

import controller.db.impl.JdbcClientDao;
import model.Client;
import model.Product;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientController {
    JdbcClientDao clientDao = new JdbcClientDao();
    private Client actualClient;

    public ClientController(String email, String password) throws SQLException {
        loginClient(email, password);
    }

    public ClientController() {
    }

    public void loginClient(String mail, String password) throws SQLException, IllegalArgumentException {
        Client client = findByMail(mail);
        if (client.getPassword().equals(password)) {
            actualClient = client;
        } else throw new IllegalArgumentException("Wrong User or Password");
    }

    public void registerClient(Client client) throws SQLException {
        clientDao.save(client);
    }

    public void delete(Client client) throws SQLException {
        clientDao.delete(client);
    };

    public void update(Client client) throws SQLException{
        clientDao.update(client);
    };

    public Client findById(int id) throws SQLException{
        return clientDao.findById(id);
    };

    public Client findByMail(String mail) throws SQLException{
        return clientDao.findByMail(mail);
    };

    public List<Client> findAll() throws SQLException{
        return clientDao.findAll();
    };



    // Import / export files
    public OrderController addOrderLine(Product product) throws IllegalStateException {
        if (this.actualClient != null) {
            OrderController orderController = new OrderController();
            orderController.addLineaOrder(product, this.actualClient, 1);
            return orderController;
        } else throw (new IllegalStateException());
    }

    public void importAdminClient() throws IOException {
        List<Client> clientList = FileManagement.importClient();
        //clientsList.addAll(clientList);
    }

    public void jaxbExport() throws JAXBException {
        //FileManagement.clientToXml(this.clientsList);
    }

    public void jaxbImport() throws IOException, JAXBException {
        //this.clientsList = FileManagement.xmlToClient();
    }
}
