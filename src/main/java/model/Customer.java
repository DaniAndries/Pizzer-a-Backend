package model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a customer in the system. The customer class contains personal
 * information such as the customer's identification, contact details,
 * and order history. This class can be serialized to XML.
 *
 * @author DaniAndries
 * @version 0.1
 */
@Entity
@Data
@AllArgsConstructor
//Genera constructor con parámetros
@NoArgsConstructor
//Genera constructor sin parámetros
@EqualsAndHashCode
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; // Unique identifier for the customer
    @Column(unique = true, nullable = false)
    private String dni; // National identification number of the customer
    private String customerName; // Name of the customer
    private String direction; // Address of the customer
    private String phone; // Phone number of the customer
    @Column(unique = true, nullable = false)
    private String mail; // Email address of the customer
    private String password; // Password for the customer's account
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Order> orderList = new ArrayList<>(); // List of orders made by the customer
    private boolean admin = false; // Flag indicating if the customer has admin privileges
}
