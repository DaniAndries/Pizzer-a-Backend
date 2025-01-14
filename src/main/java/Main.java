import utils.DatabaseConf;
import java.sql.SQLException;

/**
 * The Main class serves as the entry point for the Pizzeria application.
 * <p>
 * This class contains the main method which initializes the application by
 * dropping and recreating the necessary database tables.
 * </p>
 *
 * @author DaniAndries
 * @version 0.1
 */
public class Main {

    /**
     * Default constructor for the Main class.
     * <p>
     * This constructor initializes a new instance of the Main class.
     * It can be used to create an instance of this class without
     * any specific parameters.
     * </p>
     */
    public Main() {
        // Empty constructor
    }

    /**
     * The main method that executes the application.
     * <p>
     * This method establishes a connection to the database and
     * ensures that all required tables are present by dropping
     * and recreating them.
     * </p>
     *
     * @param args Command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        try {
            DatabaseConf.dropAndCreateTables();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
