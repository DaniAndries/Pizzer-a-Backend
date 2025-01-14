package controller;

import model.Client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * The ClientWrapper class serves as a container for a list of Client objects.
 * It is used for JAXB XML serialization and deserialization, allowing a collection
 * of Client objects to be easily converted to and from XML format.
 *
 * @author DaniAndries
 * @version 0.1
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ClientWrapper {

    private List<Client> clientList = new ArrayList<>(); // List of Client objects

    /**
     * Constructs a ClientWrapper with a specified list of clients.
     *
     * @param clientList a List of Client objects to initialize the wrapper
     */
    public ClientWrapper(List<Client> clientList) {
        this.clientList = clientList;
    }

    /**
     * Default constructor for ClientWrapper.
     * This is necessary for JAXB to instantiate the object during deserialization.
     */
    public ClientWrapper() {
    }

    /**
     * Gets the list of Client objects.
     *
     * @return a List of Client objects contained in the wrapper
     */
    public List<Client> getClientList() {
        return clientList;
    }

    /**
     * Sets the list of Client objects.
     *
     * @param clientList a List of Client objects to set in the wrapper
     */
    public void setClientList(List<Client> clientList) {
        this.clientList = clientList;
    }
}
