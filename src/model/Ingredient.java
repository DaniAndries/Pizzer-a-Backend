package model;

import com.opencsv.bean.CsvBindAndSplitByName;
import com.opencsv.bean.CsvBindByName;
import jakarta.persistence.*;

import java.util.List;

/**
 * Represents an ingredient used in products, including its name, unique identifier,
 * and any allergens associated with it. This class is designed to be compatible
 * with CSV serialization and deserialization using OpenCSV.
 *
 * @author DaniAndries
 * @version 0.1
 */
@Entity
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "next_val")
    @SequenceGenerator(name = "next_val", sequenceName = "next_val", allocationSize = 1)
    @CsvBindByName
    private int id; // Unique identifier for the ingredient
    @CsvBindByName
    private String name; // Name of the ingredient
    @CsvBindAndSplitByName(writeDelimiter = ",", elementType = String.class)
    private List<String> alergens; // List of allergens associated with the ingredient

    /**
     * Constructs an Ingredient with the specified id, name, and allergens.
     *
     * @param id       the unique identifier for this ingredient
     * @param name     the name of the ingredient
     * @param alergens the list of allergens associated with this ingredient
     */
    public Ingredient(int id, String name, List<String> alergens) {
        this.id = id;
        this.name = name;
        this.alergens = alergens;
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
    public List<String> getAlergens() {
        return alergens;
    }

    /**
     * Sets the list of allergens for this ingredient.
     *
     * @param alergens the new list of allergens
     */
    public void setAlergens(List<String> alergens) {
        this.alergens = alergens;
    }

    /**
     * Compares this ingredient to another object for equality.
     * Two Ingredient objects are considered equal if they have the same
     * unique identifier, name, and allergens.
     *
     * @param o the object to compare to
     * @return true if this ingredient is equal to the specified object; false otherwise
     */
    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Ingredient that)) return false;

        return getId() == that.getId() &&
                getAlergens().equals(that.getAlergens()) &&
                getName().equals(that.getName());
    }

    /**
     * Returns a hash code value for this ingredient.
     * The hash code is calculated based on the unique identifier, name, and allergens.
     *
     * @return a hash code value for this ingredient
     */
    @Override
    public int hashCode() {
        int result = getAlergens().hashCode();
        result = 31 * result + getId();
        result = 31 * result + getName().hashCode();
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
        return "Ingredient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", alergens=" + alergens +
                '}';
    }
}
