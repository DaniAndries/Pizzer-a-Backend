package model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Pasta product in the system.
 * <p>
 * This class extends the {@link Product} class and includes additional
 * information specific to pasta products, including their ingredients.
 * </p>
 *
 * @author DaniAndries
 * @version 0.1
 */
@Entity
public class Pasta extends Product {
    @OneToMany(mappedBy = "pasta", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Ingredient> ingredients = new ArrayList<>();

    /**
     * Constructs a Pasta object with the specified id, name, and price.
     *
     * @param id    The unique identifier for the pasta.
     * @param name  The name of the pasta.
     * @param price The price of the pasta.
     */
    public Pasta(int id, String name, double price) {
        super(id, name, price);
    }

    public Pasta() {}

    /**
     * Constructs a Pasta object with the specified id, name, price, and ingredients.
     *
     * @param id         The unique identifier for the pasta.
     * @param name       The name of the pasta.
     * @param price      The price of the pasta.
     * @param ingredients The list of ingredients included in the pasta.
     */
    public Pasta(int id, String name, double price, List<Ingredient> ingredients) {
        super(id, name, price);
        this.ingredients = ingredients;
    }

    /**
     * Returns the list of ingredients in the pasta.
     *
     * @return A list of ingredients.
     */
    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    /**
     * Sets the list of ingredients for the pasta.
     *
     * @param ingredients A list of ingredients to set.
     */
    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    /**
     * Compares this Pasta object to the specified object for equality.
     *
     * @param o the object to compare this Pasta against
     * @return true if the given object represents an equivalent Pasta;
     *         false otherwise
     */
    @Override
    public final boolean equals(Object o) {
        // Check if the object is an instance of Pasta
        if (!(o instanceof Pasta pasta)) return false;

        // Call the superclass equals method
        if (!super.equals(o)) return false;

        // Compare the ingredients lists for equality
        return getIngredients().equals(pasta.getIngredients());
    }

    /**
     * Returns a hash code value for the Pasta object.
     *
     * @return a hash code value for this Pasta
     */
    @Override
    public int hashCode() {
        // Calculate the hash code based on the superclass and ingredients
        int result = super.hashCode();
        result = 31 * result + getIngredients().hashCode();
        return result;
    }

    /**
     * Returns a string representation of the Pasta object.
     *
     * @return a string representation of the Pasta
     */
    @Override
    public String toString() {
        return "Pasta{" + super.toString() +
                "ingredients=" + ingredients +
                "} ";
    }
}
