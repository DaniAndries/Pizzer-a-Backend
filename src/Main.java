import controller.ClientController;
import controller.db.impl.JdbcClientDao;
import model.Client;
import utils.DatabaseConf;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        JdbcClientDao jdbcClientDao = new JdbcClientDao();

        ClientController clientController = new ClientController();

        Client client1 = new Client("12345678A", "Juan Pérez", "Calle Falsa 123, Madrid", "600123456", "juan.perez@example.com", "password123");
        Client client2 = new Client("87654321B", "Ana Gómez", "Av. Siempre Viva 742, Barcelona", "650654321", "ana.gomez@example.com", "securepass456");
        Client client3 = new Client("45612378C", "Luis Martínez", "Plaza Mayor 5, Valencia", "610987654", "luis.martinez@example.com", "mypassword789");
        Client client4 = new Client("78965412D", "Marta López", "Calle Luna 8, Sevilla", "620112233", "marta.lopez@example.com", "martal123");
        Client client5 = new Client("32198765E", "Carlos García", "Paseo del Prado 15, Bilbao", "630445566", "carlos.garcia@example.com", "garcia456");




        try {
            DatabaseConf.dropAndCreateTables();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
