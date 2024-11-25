import controller.ClientController;
import controller.db.impl.JdbcClientDao;
import model.Client;
import utils.DatabaseConf;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        JdbcClientDao jdbcClientDao = new JdbcClientDao();
        ClientController clientController = new ClientController();

        List<Client> clientList = new ArrayList<>();

        Client auxiliar = new Client();

        Client client1 = new Client("12345678A", "Juan Pérez", "Calle Falsa 123, Madrid", "600123456", "juan.perez@example.com", "password123", false);
        Client client2 = new Client("87654321B", "Ana Gómez", "Av. Siempre Viva 742, Barcelona", "650654321", "ana.gomez@example.com", "securepass456", false);
        Client client3 = new Client("45612378C", "Luis Martínez", "Plaza Mayor 5, Valencia", "610987654", "luis.martinez@example.com", "mypassword789", true);
        Client client4 = new Client("78965412D", "Marta López", "Calle Luna 8, Sevilla", "620112233", "marta.lopez@example.com", "martal123", false);
        Client client5 = new Client("32198765E", "Carlos García", "Paseo del Prado 15, Bilbao", "630445566", "carlos.garcia@example.com", "garcia456", true);

        auxiliar = client1;

        auxiliar.setClientName("Rafiki Kashimiri");

        try {
            DatabaseConf.dropAndCreateTables();
            clientController.registerClient(client1);
            clientController.registerClient(client2);
            clientController.registerClient(client3);
            clientController.registerClient(client4);
            clientController.registerClient(client5);

            clientController.update(auxiliar);

            clientController.delete(client3);

            auxiliar = clientController.findById(1);

            System.out.println(auxiliar);

            auxiliar = clientController.findByMail("carlos.garcia@example.com");

            System.out.println(auxiliar);

            clientList = clientController.findAll();

            clientList.forEach(System.out::println);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
