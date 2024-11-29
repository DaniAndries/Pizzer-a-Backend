import controller.ClientController;
import controller.ProductController;
import controller.db.impl.JdbcClientDao;
import model.*;
import utils.DatabaseConf;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            DatabaseConf.dropAndCreateTables();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
