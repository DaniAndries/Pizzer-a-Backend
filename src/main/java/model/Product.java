package model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

/**
 * Abstract class representing a product in the system.
 * <p>
 * This class serves as a base class for different types of products such as
 * food items, drinks, etc. It encapsulates common properties and behaviors
 * shared among all products.
 * </p>
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
@JsonTypeInfo (use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Pizza.class, name = "pizza"),
        @JsonSubTypes.Type(value = Pasta.class, name = "pasta"),
        @JsonSubTypes.Type(value = Drink.class, name = "drink")
})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    @Column(unique = true, nullable = false)
    private String name;
    private double price;

    @Enumerated(EnumType.STRING)
    private Size size;
}
