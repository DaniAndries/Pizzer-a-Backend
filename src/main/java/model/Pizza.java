package model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Pizza, which is a type of Product.
 * <p>
 * This class includes details about the pizza's size and its ingredients,
 * along with the inherited properties from the Product class.
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
@SequenceGenerator(name="customer_seq", sequenceName="hibernate_sequence", allocationSize=1)
public class Pizza extends Product {

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "Product_Ingredient",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_id")
    )
    private List<Ingredient> ingredients = new ArrayList<>();
}
