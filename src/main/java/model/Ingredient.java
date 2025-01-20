package model;

import java.util.ArrayList;
import java.util.List;

import com.opencsv.bean.CsvBindAndSplitByName;
import com.opencsv.bean.CsvBindByName;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

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
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @CsvBindByName
    private int id; // Unique identifier for the ingredient

    @CsvBindByName
    @Column(unique = true, nullable = false)
    private String name; // Name of the ingredient

    @CsvBindAndSplitByName(writeDelimiter = ",", elementType = String.class)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "ingredient_allergen")
    private List<String> allergens = new ArrayList<>(); // List of allergens associated with the ingredient

    /**
     * Constructs an Ingredient with the specified id, name, and allergens.
     *
     * @param id        the unique identifier for this ingredient
     * @param name      the name of the ingredient
     * @param allergens the list of allergens associated with this ingredient
     */
    public Ingredient(int id, String name, List<String> allergens) {
        this.id = id;
        this.name = name;
        this.allergens = allergens;
    }

    /**
     * Constructs an Ingredient with the specified id and name.
     * The allergens list will be initialized to null.
     *
     * @param id   the unique identifier for this ingredient
     * @param name the name of the ingredient
     */
    public Ingredient(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Default constructor for Ingredient.
     */
    public Ingredient() {
    }

    /**
     * Gets the unique identifier for this ingredient.
     *
     * @return the unique identifier
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique identifier for this ingredient.
     *
     * @param id the new unique identifier
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the name of this ingredient.
     *
     * @return the ingredient name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this ingredient.
     *
     * @param name the new ingredient name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the list of allergens associated with this ingredient.
     *
     * @return the list of allergens
     */
    public List<String> getAllergens() {
        return allergens;
    }

    /**
     * Sets the list of allergens for this ingredient.
     *
     * @param alergens the new list of allergens
     */
    public void setAllergens(List<String> alergens) {
        this.allergens = allergens;
    }

    /**
     * Compares this ingredient to another object for equality.
     * Two Ingredient objects are considered equal if they have the same
     * unique identifier, name, and allergens.
     *
     * @param o the object to compare to
     * @return true if this ingredient is equal to the specified object; false
     *         otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Ingredient that = (Ingredient) o;

        if (id != that.id)
            return false;
        if (!name.equals(that.name))
            return false;

        // Comparamos las listas de alérgenos asegurándonos que tengan los mismos
        // elementos
        if (allergens == null && that.allergens == null)
            return true;
        if (allergens == null || that.allergens == null)
            return false;

        // Comparamos basándonos en los contenidos ignorando el orden
        return allergens.size() == that.allergens.size() &&
                allergens.containsAll(that.allergens);
    }

    /**
     * Returns a hash code value for this ingredient.
     * The hash code is calculated based on the unique identifier, name, and
     * allergens.
     *
     * @return a hash code value for this ingredient
     */
    @Override
    public int hashCode() {
        // Calculamos hashCode considerando id, nombre y contenido de la lista de
        // alérgenos
        int result = id;
        result = 31 * result + name.hashCode();

        if (allergens != null) {
            // Generamos un hash independiente del orden de los elementos en la lista
            result = 31 * result + allergens.stream().sorted().hashCode();
        }

        return result;
    }

    /**
     * Returns a string representation of this ingredient, including its id,
     * name, and allergens.
     *
     * @return a string representation of this ingredient
     */
    @Override
    public String toString() {
        return "Ingredient{" + "id=" + id + ", name='" + name + '\'' + ", allergens=" + allergens + '}';
    }
}
