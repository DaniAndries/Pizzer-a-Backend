package model;

import jakarta.persistence.*;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a customer in the system. The customer class contains personal
 * information such as the customer's identification, contact details,
 * and order history. This class can be serialized to XML.
 *
 * @author DaniAndries
 * @version 0.1
 */
@Entity
@XmlAccessorType(XmlAccessType.FIELD) // Direct access to fields
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id; // Unique identifier for the customer
    @Column(unique = true, nullable = false)
    private String dni; // National identification number of the customer
    private String customerName; // Name of the customer
    private String direction; // Address of the customer
    private String phone; // Phone number of the customer
    @Column(unique = true, nullable = false)
    private String mail; // Email address of the customer
    @XmlTransient // Exclude password from XML serialization
    private String password; // Password for the customer's account
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @XmlTransient // Exclude order list from XML serialization
    private List<Order> orderList = new ArrayList<>(); // List of orders made by the customer
    @XmlAttribute // Indicates that this field is an XML attribute
    private boolean admin = false; // Flag indicating if the customer has admin privileges

    /**
     * Constructs a customer object with the specified parameters.
     *
     * @param dni         the national identification number of the customer
     * @param customerName  the name of the customer
     * @param direction   the address of the customer
     * @param phone       the phone number of the customer
     * @param mail        the email address of the customer
     * @param password    the password for the customer's account
     */
    public Customer(String dni, String customerName, String direction, String phone, String mail, String password) {
        this.dni = dni;
        this.customerName = customerName;
        this.direction = direction;
        this.phone = phone;
        this.mail = mail;
        this.password = password;
    }

    /**
     * Constructs a customer object with admin privileges.
     *
     * @param dni         the national identification number of the customer
     * @param customerName  the name of the customer
     * @param direction   the address of the customer
     * @param phone       the phone number of the customer
     * @param mail        the email address of the customer
     * @param password    the password for the customer's account
     * @param admin       indicates if the customer has admin privileges
     */
    public Customer(String dni, String customerName, String direction, String phone, String mail, String password, boolean admin) {
        this(dni, customerName, direction, phone, mail, password);
        this.admin = admin; // Set admin flag
    }

    /**
     * Constructs a customer object with an ID and admin privileges.
     *
     * @param id          the unique identifier for the customer
     * @param dni         the national identification number of the customer
     * @param customerName  the name of the customer
     * @param direction   the address of the customer
     * @param phone       the phone number of the customer
     * @param mail        the email address of the customer
     * @param password    the password for the customer's account
     * @param admin       indicates if the customer has admin privileges
     */
    public Customer(int id, String dni, String customerName, String direction, String phone, String mail, String password, boolean admin) {
        this(dni, customerName, direction, phone, mail, password, admin);
        this.id = id; // Set customer ID
    }

    /**
     * Default constructor for creating an empty customer object.
     */
    public Customer() {
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
     * Gets the unique identifier of the customer.
     *
     * @return the unique identifier of the customer.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the customer.
     *
     * @param id the unique identifier to set for the customer.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the DNI (Documento Nacional de Identidad) of the customer.
     *
     * @return the DNI of the customer.
     */
    public String getDni() {
        return dni;
    }

    /**
     * Sets the DNI (Documento Nacional de Identidad) of the customer.
     *
     * @param dni the DNI to set for the customer.
     */
    public void setDni(String dni) {
        this.dni = dni;
    }

    /**
     * Gets the name of the customer.
     *
     * @return the name of the customer.
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * Sets the name of the customer.
     *
     * @param customerName the name to set for the customer.
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * Gets the direction (address) of the customer.
     *
     * @return the direction of the customer.
     */
    public String getDirection() {
        return direction;
    }

    /**
     * Sets the direction (address) of the customer.
     *
     * @param direction the direction to set for the customer.
     */
    public void setDirection(String direction) {
        this.direction = direction;
    }

    /**
     * Gets the phone number of the customer.
     *
     * @return the phone number of the customer.
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the phone number of the customer.
     *
     * @param phone the phone number to set for the customer.
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Gets the email address of the customer.
     *
     * @return the email address of the customer.
     */
    public String getMail() {
        return mail;
    }

    /**
     * Sets the email address of the customer.
     *
     * @param mail the email address to set for the customer.
     */
    public void setMail(String mail) {
        this.mail = mail;
    }

    /**
     * Gets the password of the customer.
     *
     * @return the password of the customer.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password for the customer.
     *
     * @param password the password to set for the customer.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the list of orders associated with the customer.
     *
     * @return the list of orders for the customer.
     */
    public List<Order> getOrderList() {
        return orderList;
    }

    /**
     * Sets the list of orders associated with the customer.
     *
     * @param orderList the list of orders to set for the customer.
     */
    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }

    /**
     * Checks if the customer has admin privileges.
     *
     * @return true if the customer is an admin; false otherwise.
     */
    public boolean isAdmin() {
        return admin;
    }

    /**
     * Sets the admin status of the customer.
     *
     * @param admin the admin status to set for the customer.
     */
    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    /**
     * Compares this customer to another object for equality. Two customer objects
     * are considered equal if they have the same id, dni, name, direction,
     * phone, mail, password, and admin status.
     *
     * @param o the object to compare to
     * @return true if this customer is equal to the specified object; false otherwise
     */
    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer customer)) return false;

        return getId() == customer.getId() &&
                Objects.equals(getDni(), customer.getDni()) &&
                Objects.equals(getCustomerName(), customer.getCustomerName()) &&
                Objects.equals(getDirection(), customer.getDirection()) &&
                Objects.equals(getPhone(), customer.getPhone()) &&
                Objects.equals(getMail(), customer.getMail()) &&
                Objects.equals(getPassword(), customer.getPassword()) &&
                isAdmin() == customer.isAdmin();
    }

    /**
     * Returns a hash code value for this customer. The hash code is calculated
     * based on the customer's dni, name, direction, phone, mail, password, and admin status.
     *
     * @return a hash code value for this customer
     */
    @Override
    public int hashCode() {
        return Objects.hash(getDni(), getCustomerName(), getDirection(), getPhone(), getMail(), getPassword(), isAdmin());
    }

    /**
     * Returns a string representation of this customer, including its ID, dni,
     * name, direction, phone, mail, and admin status.
     *
     * @return a string representation of this customer
     */
    @Override
    public String toString() {
        return "customer{" +
                "id=" + id +
                ", dni='" + dni + '\'' +
                ", customerName='" + customerName + '\'' +
                ", direction='" + direction + '\'' +
                ", phone='" + phone + '\'' +
                ", mail='" + mail + '\'' +
                ", password='" + password + '\'' +
                ", admin=" + admin +
                '}';
    }
}
