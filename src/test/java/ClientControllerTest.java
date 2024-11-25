package test.java;

import controller.ClientController;
import model.Client;
import org.junit.jupiter.api.*;
import utils.DatabaseConf;

import java.sql.SQLException;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ClientControllerTest {

    private ClientController clientController;

    @BeforeEach
    void setupDatabase() throws SQLException {
        DatabaseConf.dropAndCreateTables(); // Resetea la base de datos antes de empezar
        clientController = new ClientController();
    }

    @Test
    void testRegisterClient() throws SQLException {
        Client client = new Client("12345678A", "Juan Pérez", "Calle Falsa 123, Madrid", "600123456", "juan.perez@example.com", "password123", false);
        clientController.registerClient(client);

        Client foundClient = clientController.findByMail("juan.perez@example.com");
        Assertions.assertNotNull(foundClient);
        Assertions.assertEquals("Juan Pérez", foundClient.getClientName());
    }

    @Test
    void testUpdateClient() throws SQLException {
        Client client = new Client("12345678B", "Ana Gómez", "Av. Siempre Viva 742, Barcelona", "650654321", "ana.gomez@example.com", "securepass456", false);
        clientController.registerClient(client);

        client.setClientName("Ana González");
        clientController.update(client);

        Client updatedClient = clientController.findByMail("ana.gomez@example.com");
        Assertions.assertEquals("Ana González", updatedClient.getClientName());
    }

    @Test
    void testDeleteClient() throws SQLException {
        Client client = new Client("45612378C", "Luis Martínez", "Plaza Mayor 5, Valencia", "610987654", "luis.martinez@example.com", "mypassword789", true);
        clientController.registerClient(client);

        clientController.delete(client);

        Client deletedClient = clientController.findByMail("luis.martinez@example.com");
        Assertions.assertNull(deletedClient);
    }

    @Test
    void testFindById() throws SQLException {
        Client client = new Client("78965412D", "Marta López", "Calle Luna 8, Sevilla", "620112233", "marta.lopez@example.com", "martal123", false);
        clientController.registerClient(client);

        Client foundClient = clientController.findById(1);
        Assertions.assertNotNull(foundClient);
        Assertions.assertEquals("Marta López", foundClient.getClientName());
    }

    @Test
    void testFindAllClients() throws SQLException {
        Client client1 = new Client("32198765E", "Carlos García", "Paseo del Prado 15, Bilbao", "630445566", "carlos.garcia@example.com", "garcia456", true);
        Client client2 = new Client("65432198F", "Laura Fernández", "Gran Vía 18, Zaragoza", "640987123", "laura.fernandez@example.com", "laura2023", false);

        clientController.registerClient(client1);
        clientController.registerClient(client2);

        List<Client> clients = clientController.findAll();
        Assertions.assertEquals(2, clients.size());
    }
}
