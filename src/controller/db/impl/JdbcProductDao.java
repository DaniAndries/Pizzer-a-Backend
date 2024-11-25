package controller.db.impl;

public class JdbcProductDao {
    static final JdbcOrderDao jdbcCarDao = new JdbcOrderDao();
    /**
     * id
     * product_name;
     * price;
     * size;
     * List<Ingredient> ingredients;
     * type;
     */
    private static final String INSERT_CLIENT = "INSERT INTO Client (dni, client_name, direction, phone, mail, password, admin) VALUES (?,?,?,?,?,?,?)";
    private static final String UPDATE_CLIENT = "UPDATE Client SET client.client_name=?, client.direction=?, client.phone=?, client.password=?, client.admin=? WHERE client.id = ?";
    private static final String DELETE_CLIENT = "DELETE FROM client WHERE client.ID = ?";
    private static final String SELECT_CLIENT = "SELECT client.id, client.dni, client.client_name, client.direction, client.phone, client.mail, client.password, client.admin FROM client WHERE client.ID = ?";
    private static final String SELECT_CLIENT_MAIL = "SELECT client.id, client.dni, client.client_name, client.direction, client.phone, client.mail, client.password, client.admin FROM client WHERE client.MAIl = ?";
    private static final String SELECT_ALL = "SELECT client.id, client.dni, client.client_name, client.direction, client.phone, client.mail, client.password, client.admin FROM client";

}
