package controller;

import model.Client;
import model.Product;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClientController {
    private Client actualClient;
    private List<Client> clientsList = new ArrayList<>();

    public ClientController(String email, String password) {
        loginClient(email, password);
    }

    public ClientController() {
    }

    public void registerClient(String dni, String name, String direction, String phone, String mail, String password) {
        clientsList.add(new Client(mail, name, dni, mail, phone, password));
    }

    public void loginClient(String email, String password) {
        for (Client listaClient : clientsList) {
            if (listaClient.getMail().equals(email) && listaClient.getPassword().equals(password)) {
                this.actualClient = listaClient;
            }
        }
        if (actualClient == null) System.err.println("Usuario o contraseña incorrecta");
    }

    public OrderController addOrderLine(Product product) throws IllegalStateException {
        if (this.actualClient != null) {
            OrderController orderController = new OrderController();
            orderController.addLineaOrder(product, this.actualClient, 1);
            return orderController;
        } else throw (new IllegalStateException());
    }

    public void importAdminClient() throws IOException {
        List<Client> clientList = FileManagement.importClient();
        clientsList.addAll(clientList);
    }

    public void jaxbExport() throws JAXBException {
        FileManagement.clientToXml(this.clientsList);
    }

    public void jaxbImport() throws IOException, JAXBException {
        this.clientsList = FileManagement.xmlToClient();
    }


    public List<Client> getClientsList() {
        return clientsList;
    }

    public void setClientsList(List<Client> clientsList) {
        this.clientsList = clientsList;
    }
}
