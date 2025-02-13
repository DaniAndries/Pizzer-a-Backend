package model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Represents a drink product with a specific size. This class extends the
 * {@link Product} class and includes additional information relevant to drinks.
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
public class Drink extends Product {
    @Enumerated(EnumType.STRING)
    private Size size; // The size of the drink (e.g., BIG, MEDIUM, SMALL)
}
