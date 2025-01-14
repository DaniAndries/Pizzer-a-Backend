import controller.ClientController;
import model.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import utils.DatabaseConf;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ClientControllerTest is a test class for testing the functionality
 * of the ClientController class.
 * <p>
 * This class contains test methods to validate the behavior of
 * ClientController, ensuring that it handles client operations correctly.
 * </p>
 *
 * @author DaniAndries
 * @version 0.1
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JdbcClientControllerTest {

    private ClientController clientController;

    /**
     * Default constructor for ClientControllerTest.
     * <p>
     * This constructor initializes a new instance of the ClientControllerTest class.
     * It can be used to create an instance of this class without
     * providing any specific parameters.
     * </p>
     */
    public JdbcClientControllerTest() {
        // Empty constructor
    }

    /**
     * Sets up the test environment by resetting the database and initializing the controller.
     *
     * @throws SQLException if a database error occurs.
     */
    @BeforeEach
    void setupDatabase() throws SQLException {
        DatabaseConf.dropAndCreateTables();
        clientController = new ClientController();
    }

    /**
     * Tests registering a new client and retrieving it by email.
     *
     * @throws SQLException if a database error occurs.
     */
    @Test
    void testRegisterClient() throws SQLException {
        Client client = new Client("12345678A", "Juan Pérez", "Calle Falsa 123, Madrid", "600123456", "juan.perez@example.com", "password123", false);
        clientController.registerClient(client);

        Client foundClient = clientController.findByMail("juan.perez@example.com");
        assertNotNull(foundClient, "Client should be found after registration.");
        assertEquals("Juan Pérez", foundClient.getClientName(), "Client name should match the registered value.");
    }

    /**
     * Tests updating an existing client's details.
     *
     * @throws SQLException if a database error occurs.
     */
    @Test
    void testUpdateClient() throws SQLException {
        Client client = new Client("12345678B", "Ana Gómez", "Av. Siempre Viva 742, Barcelona", "650654321", "ana.gomez@example.com", "securepass456", false);
        clientController.registerClient(client);

        client.setClientName("Ana González");
        clientController.update(client);

        Client updatedClient = clientController.findByMail("ana.gomez@example.com");
        assertNotNull(updatedClient, "Client should still exist after the update.");
        assertEquals("Ana González", updatedClient.getClientName(), "Client name should match the updated value.");
    }

    /**
     * Tests deleting a registered client.
     *
     * @throws SQLException if a database error occurs.
     */
    @Test
    void testDeleteClient() throws SQLException {
        Client client = new Client("45612378C", "Luis Martínez", "Plaza Mayor 5, Valencia", "610987654", "luis.martinez@example.com", "mypassword789", true);
        clientController.registerClient(client);

        clientController.delete(client);

        Client deletedClient = clientController.findByMail("luis.martinez@example.com");
        assertNull(deletedClient, "Client should not exist after deletion.");
    }

    /**
     * Tests finding a client by their unique ID.
     *
     * @throws SQLException if a database error occurs.
     */
    @Test
    void testFindById() throws SQLException {
        Client client = new Client("78965412D", "Marta López", "Calle Luna 8, Sevilla", "620112233", "marta.lopez@example.com", "martal123", false);
        clientController.registerClient(client);

        Client foundClient = clientController.findById(1);
        assertNotNull(foundClient, "Client should be found by ID.");
        assertEquals("Marta López", foundClient.getClientName(), "Client name should match the registered value.");
    }

    /**
     * Tests retrieving all registered clients.
     *
     * @throws SQLException if a database error occurs.
     */
    void testFindAllClients() throws SQLException {
        Client client1 = new Client("32198765E", "Carlos García", "Paseo del Prado 15, Bilbao", "630445566", "carlos.garcia@example.com", "garcia456", true);
        Client client2 = new Client("65432198F", "Laura Fernández", "Gran Vía 18, Zaragoza", "640987123", "laura.fernandez@example.com", "laura2023", false);

        clientController.registerClient(client1);
        clientController.registerClient(client2);

        List<Client> clients = clientController.findAll();
        assertEquals(2, clients.size());
    }

    @Test
    void shouldRegisterClientSuccessfully() throws SQLException {
        Client client = new Client("12345678A", "Juan Pérez", "Calle Falsa 123, Madrid", "600123456", "juan.perez@example.com", "password123", false);

        clientController.registerClient(client);

        Client foundClient = clientController.findByMail("juan.perez@example.com");
        assertNotNull(foundClient, "Expected client to be found after registration");
        assertEquals("Juan Pérez", foundClient.getClientName(), "Client name should match after registration");
    }
}
