package model;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

/**
 * Represents a drink product with a specific size. This class extends the
 * {@link Product} class and includes additional information relevant to drinks.
 *
 * @author DaniAndries
 * @version 0.1
 */
public class Drink extends Product {
    @Enumerated(EnumType.STRING)
    private Size size; // The size of the drink (e.g., BIG, MEDIUM, SMALL)

    /**
     * Constructs a Drink object with the specified parameters.
     *
     * @param id    the unique identifier for this drink
     * @param name  the name of the drink
     * @param price the price of the drink
     * @param size  the size of the drink
     */
    public Drink(int id, String name, double price, Size size) {
        super(id, name, price);
        this.size = size;
    }

    public Drink(int id, String name, double price) {
        super(id, name, price);
    }

    /**
     * Gets the size of this drink.
     *
     * @return the size of the drink
     */
    public Size getSize() {
        return size;
    }

    /**
     * Sets the size of this drink.
     *
     * @param size the new size for the drink
     */
    public void setSize(Size size) {
        this.size = size;
    }

    /**
     * Compares this drink to another object for equality. Two Drink objects
     * are considered equal if they have the same id, name, price, and size.
     *
     * @param o the object to compare to
     * @return true if this drink is equal to the specified object; false otherwise
     */
    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Drink drink)) return false;
        if (!super.equals(o)) return false;

        return getSize() == drink.getSize();
    }

    /**
     * Returns a hash code value for this drink. The hash code is calculated
     * based on the superclass's hash code and the drink's size.
     *
     * @return a hash code value for this drink
     */
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + getSize().hashCode();
        return result;
    }

    /**
     * Returns a string representation of this drink, including its id, name,
     * price, and size.
     *
     * @return a string representation of this drink
     */
    @Override
    public String toString() {
        return "Drink{" +
                super.toString() +
                "size=" + size +
                "} ";
    }
}
