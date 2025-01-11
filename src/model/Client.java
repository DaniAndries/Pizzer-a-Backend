package model;

import jakarta.persistence.*;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import java.util.List;
import java.util.Objects;

/**
 * Represents a client in the system. The Client class contains personal
 * information such as the client's identification, contact details,
 * and order history. This class can be serialized to XML.
 *
 * @author DaniAndries
 * @version 0.1
 */
@Entity
@XmlAccessorType(XmlAccessType.FIELD) // Direct access to fields
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "next_val")
    @SequenceGenerator(name = "next_val", sequenceName = "next_val", allocationSize = 1)
    private int id; // Unique identifier for the client
    @Column(unique = true, nullable = false)
    private String dni; // National identification number of the client
    private String clientName; // Name of the client
    private String direction; // Address of the client
    private String phone; // Phone number of the client
    @Column(unique = true, nullable = false)
    private String mail; // Email address of the client
    @XmlTransient // Exclude password from XML serialization
    private String password; // Password for the client's account
    @OneToMany(mappedBy = "Order", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @XmlTransient // Exclude order list from XML serialization
    private List<Order> orderList; // List of orders made by the client
    @XmlAttribute // Indicates that this field is an XML attribute
    private boolean admin = false; // Flag indicating if the client has admin privileges

    /**
     * Constructs a Client object with the specified parameters.
     *
     * @param dni         the national identification number of the client
     * @param clientName  the name of the client
     * @param direction   the address of the client
     * @param phone       the phone number of the client
     * @param mail        the email address of the client
     * @param password    the password for the client's account
     */
    public Client(String dni, String clientName, String direction, String phone, String mail, String password) {
        this.dni = dni;
        this.clientName = clientName;
        this.direction = direction;
        this.phone = phone;
        this.mail = mail;
        this.password = password;
    }

    /**
     * Constructs a Client object with admin privileges.
     *
     * @param dni         the national identification number of the client
     * @param clientName  the name of the client
     * @param direction   the address of the client
     * @param phone       the phone number of the client
     * @param mail        the email address of the client
     * @param password    the password for the client's account
     * @param admin       indicates if the client has admin privileges
     */
    public Client(String dni, String clientName, String direction, String phone, String mail, String password, boolean admin) {
        this(dni, clientName, direction, phone, mail, password);
        this.admin = admin; // Set admin flag
    }

    /**
     * Constructs a Client object with an ID and admin privileges.
     *
     * @param id          the unique identifier for the client
     * @param dni         the national identification number of the client
     * @param clientName  the name of the client
     * @param direction   the address of the client
     * @param phone       the phone number of the client
     * @param mail        the email address of the client
     * @param password    the password for the client's account
     * @param admin       indicates if the client has admin privileges
     */
    public Client(int id, String dni, String clientName, String direction, String phone, String mail, String password, boolean admin) {
        this(dni, clientName, direction, phone, mail, password, admin);
        this.id = id; // Set client ID
    }

    /**
     * Default constructor for creating an empty Client object.
     */
    public Client() {
    }

    /**
     * Placeholder method to realize an order. This method can be
     * implemented in the future to manage order realization logic.
     */
    public void realizeOrder() {
        // Implementation will be added later
    }

    // Getter and setter methods
    /**
     * Gets the unique identifier of the client.
     *
     * @return the unique identifier of the client.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the client.
     *
     * @param id the unique identifier to set for the client.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the DNI (Documento Nacional de Identidad) of the client.
     *
     * @return the DNI of the client.
     */
    public String getDni() {
        return dni;
    }

    /**
     * Sets the DNI (Documento Nacional de Identidad) of the client.
     *
     * @param dni the DNI to set for the client.
     */
    public void setDni(String dni) {
        this.dni = dni;
    }

    /**
     * Gets the name of the client.
     *
     * @return the name of the client.
     */
    public String getClientName() {
        return clientName;
    }

    /**
     * Sets the name of the client.
     *
     * @param clientName the name to set for the client.
     */
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    /**
     * Gets the direction (address) of the client.
     *
     * @return the direction of the client.
     */
    public String getDirection() {
        return direction;
    }

    /**
     * Sets the direction (address) of the client.
     *
     * @param direction the direction to set for the client.
     */
    public void setDirection(String direction) {
        this.direction = direction;
    }

    /**
     * Gets the phone number of the client.
     *
     * @return the phone number of the client.
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the phone number of the client.
     *
     * @param phone the phone number to set for the client.
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Gets the email address of the client.
     *
     * @return the email address of the client.
     */
    public String getMail() {
        return mail;
    }

    /**
     * Sets the email address of the client.
     *
     * @param mail the email address to set for the client.
     */
    public void setMail(String mail) {
        this.mail = mail;
    }

    /**
     * Gets the password of the client.
     *
     * @return the password of the client.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password for the client.
     *
     * @param password the password to set for the client.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the list of orders associated with the client.
     *
     * @return the list of orders for the client.
     */
    public List<Order> getOrderList() {
        return orderList;
    }

    /**
     * Sets the list of orders associated with the client.
     *
     * @param orderList the list of orders to set for the client.
     */
    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }

    /**
     * Checks if the client has admin privileges.
     *
     * @return true if the client is an admin; false otherwise.
     */
    public boolean isAdmin() {
        return admin;
    }

    /**
     * Sets the admin status of the client.
     *
     * @param admin the admin status to set for the client.
     */
    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    /**
     * Compares this client to another object for equality. Two Client objects
     * are considered equal if they have the same id, dni, name, direction,
     * phone, mail, password, and admin status.
     *
     * @param o the object to compare to
     * @return true if this client is equal to the specified object; false otherwise
     */
    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Client client)) return false;

        return getId() == client.getId() &&
                Objects.equals(getDni(), client.getDni()) &&
                Objects.equals(getClientName(), client.getClientName()) &&
                Objects.equals(getDirection(), client.getDirection()) &&
                Objects.equals(getPhone(), client.getPhone()) &&
                Objects.equals(getMail(), client.getMail()) &&
                Objects.equals(getPassword(), client.getPassword()) &&
                isAdmin() == client.isAdmin();
    }

    /**
     * Returns a hash code value for this client. The hash code is calculated
     * based on the client's dni, name, direction, phone, mail, password, and admin status.
     *
     * @return a hash code value for this client
     */
    @Override
    public int hashCode() {
        return Objects.hash(getDni(), getClientName(), getDirection(), getPhone(), getMail(), getPassword(), isAdmin());
    }

    /**
     * Returns a string representation of this client, including its ID, dni,
     * name, direction, phone, mail, and admin status.
     *
     * @return a string representation of this client
     */
    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", dni='" + dni + '\'' +
                ", clientName='" + clientName + '\'' +
                ", direction='" + direction + '\'' +
                ", phone='" + phone + '\'' +
                ", mail='" + mail + '\'' +
                ", password='" + password + '\'' +
                ", admin=" + admin +
                '}';
    }
}
