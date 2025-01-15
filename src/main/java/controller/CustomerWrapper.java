package controller;

import model.Customer;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * The CustomerWrapper class serves as a container for a list of Customer objects.
 * It is used for JAXB XML serialization and deserialization, allowing a collection
 * of Customer objects to be easily converted to and from XML format.
 *
 * @author DaniAndries
 * @version 0.1
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CustomerWrapper {

    private List<Customer> customerList = new ArrayList<>(); // List of Customer objects

    /**
     * Constructs a CustomerWrapper with a specified list of customers.
     *
     * @param customerList a List of Customer objects to initialize the wrapper
     */
    public CustomerWrapper(List<Customer> customerList) {
        this.customerList = customerList;
    }

    /**
     * Default constructor for CustomerWrapper.
     * This is necessary for JAXB to instantiate the object during deserialization.
     */
    public CustomerWrapper() {
    }

    /**
     * Gets the list of Customer objects.
     *
     * @return a List of Customer objects contained in the wrapper
     */
    public List<Customer> getCustomerList() {
        return customerList;
    }

    /**
     * Sets the list of Customer objects.
     *
     * @param customerList a List of Customer objects to set in the wrapper
     */
    public void setCustomerList(List<Customer> customerList) {
        this.customerList = customerList;
    }
}
