package model;

import jakarta.persistence.*;

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
public class Pizza extends Product {
    @Enumerated(EnumType.STRING)
    private Size size;
    @OneToMany(mappedBy = "ingredient", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Ingredient> ingredients;

    /**
     * Constructs a Pizza with the specified id, name, price, ingredients, and size.
     *
     * @param id          the unique identifier for the pizza
     * @param name        the name of the pizza
     * @param price       the price of the pizza
     * @param ingredients the list of ingredients in the pizza
     * @param size        the size of the pizza as a string
     */
    public Pizza(int id, String name, double price, List<Ingredient> ingredients, String size) {
        super(id, name, price);
        this.ingredients = ingredients;
        this.size = Size.valueOf(size);
    }

    /**
     * Constructs a Pizza with the specified id, name, price, and size.
     *
     * @param id    the unique identifier for the pizza
     * @param name  the name of the pizza
     * @param price the price of the pizza
     * @param size  the size of the pizza
     */
    public Pizza(int id, String name, double price, Size size) {
        super(id, name, price);
        this.size = size;
    }

    /**
     * Gets the size of the pizza.
     *
     * @return the size of the pizza
     */
    public Size getSize() {
        return size;
    }

    /**
     * Sets the size of the pizza.
     *
     * @param size the new size to set for the pizza
     */
    public void setSize(Size size) {
        this.size = size;
    }

    /**
     * Gets the list of ingredients in the pizza.
     *
     * @return the list of ingredients
     */
    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    /**
     * Sets the list of ingredients for the pizza.
     *
     * @param ingredients the new list of ingredients to set for the pizza
     */
    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    /**
     * Compares this pizza to the specified object for equality.
     * Two pizzas are considered equal if their ids, names, sizes, and ingredients are the same.
     *
     * @param o the object to compare this pizza against
     * @return true if the given object is equal to this pizza, false otherwise
     */
    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Pizza pizza)) return false;
        if (!super.equals(o)) return false;

        return getSize() == pizza.getSize() && getIngredients().equals(pizza.getIngredients());
    }

    /**
     * Returns a hash code value for the pizza.
     * The hash code is calculated based on the pizza's properties, including id, size, and ingredients.
     *
     * @return a hash code value for this pizza
     */
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + getSize().hashCode();
        result = 31 * result + getIngredients().hashCode();
        return result;
    }

    /**
     * Returns a string representation of the pizza.
     *
     * @return a string in the format "Pizza{[Product properties] size=..., ingredients=...}"
     */
    @Override
    public String toString() {
        return "Pizza{" +
                super.toString() +
                "size=" + size +
                ", ingredients=" + ingredients +
                "} ";
    }
}
