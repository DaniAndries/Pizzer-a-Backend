package model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Represents an ingredient used in products, including its name, unique
 * identifier,
 * and any allergens associated with it. This class is designed to be compatible
 * with CSV serialization and deserialization using OpenCSV.
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
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id; // Unique identifier for the ingredient
    @Column(unique = true, nullable = false)
    private String name; // Name of the ingredient

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "ingredient_allergen")
    private List<String> allergens = new ArrayList<>(); // List of allergens associated with the ingredient
}
