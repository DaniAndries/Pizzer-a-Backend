import utils.DatabaseConf;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try {
            DatabaseConf.dropAndCreateTables();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
