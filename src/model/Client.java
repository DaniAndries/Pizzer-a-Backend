package model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD) // Acceso directo a campos
public class Client {
    private int id;
    private String dni;
    private String clientName;
    private String direction;
    private String phone;
    private String mail;
    @XmlTransient
    private String password;
    @XmlTransient
    private List<Order> orderList;
    @XmlAttribute
    private boolean admin = false;

    public Client(String dni, String clientName, String direction, String phone, String mail, String password) {
        this.dni = dni;
        this.clientName = clientName;
        this.direction = direction;
        this.phone = phone;
        this.mail = mail;
        this.password = password;
    }

    public Client(String dni, String clientName, String direction, String phone, String mail, String password, boolean admin) {
        this(dni, clientName, direction, phone, mail, password);
        this.admin = true;
    }

    public Client(int id, String dni, String clientName, String direction, String phone, String mail, String password, boolean admin) {
        this(dni, clientName, direction, phone, mail, password, admin);
        this.id = id;
    }

    public Client() {}

    public void realizeOrder() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Client client)) return false;

        return getId() == client.getId() && isAdmin() == client.isAdmin() && getDni().equals(client.getDni()) && getClientName().equals(client.getClientName()) && getDirection().equals(client.getDirection()) && getPhone().equals(client.getPhone()) && getMail().equals(client.getMail()) && getPassword().equals(client.getPassword()) && getOrderList().equals(client.getOrderList());
    }

    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + getDni().hashCode();
        result = 31 * result + getClientName().hashCode();
        result = 31 * result + getDirection().hashCode();
        result = 31 * result + getPhone().hashCode();
        result = 31 * result + getMail().hashCode();
        result = 31 * result + getPassword().hashCode();
        result = 31 * result + getOrderList().hashCode();
        result = 31 * result + Boolean.hashCode(isAdmin());
        return result;
    }

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
