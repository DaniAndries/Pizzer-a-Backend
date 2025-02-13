package model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.lang.model.element.Name;

/**
 * Represents a line item in an order, including the product and its quantity.
 * This class calculates the total price for the product line based on the quantity ordered.
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
@Table(name = "order_line")
public class OrderLine {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    private int amount;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Product product;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id", nullable = false) // Explicit join column
    private Order order;
}
