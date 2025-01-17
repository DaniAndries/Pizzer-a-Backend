package model;

import jakarta.persistence.*;

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
public abstract class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    @Column(unique = true, nullable = false)
    private String name;
    private double price;

    @Enumerated(EnumType.STRING)
    private Size size;

    public Product() {
    }

    /**
     * Constructs a Product with the specified id, name, and price.
     *
     * @param id    the unique identifier for the product
     * @param name  the name of the product
     * @param price the price of the product
     * @param size  the size of the pizza as a string
     */
    public Product(int id, String name, double price, Size size) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.size= size;
    }

    /**
     * Constructs a Product with the specified id, name, and price.
     *
     * @param id    the unique identifier for the product
     * @param name  the name of the product
     * @param price the price of the product
     */
    public Product(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    /**
     * Gets the unique identifier of the product.
     *
     * @return the id of the product
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the product.
     *
     * @param id the new id to set for the product
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the name of the product.
     *
     * @return the name of the product
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the product.
     *
     * @param name the new name to set for the product
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the price of the product.
     *
     * @return the price of the product
     */
    public double getPrice() {
        return price;
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
     * Sets the price of the product.
     *
     * @param price the new price to set for the product
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Returns a string representation of the product.
     *
     * @return a string in the format "{id=..., name='...', price=...}"
     */
    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", size='" + size + '\'' +
                ", price=" + price +
                '}';
    }

    /**
     * Compares this product to the specified object for equality.
     * Two products are considered equal if their ids, names, and prices are the same.
     *
     * @param o the object to compare this product against
     * @return true if the given object is equal to this product, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Product product)) return false;

        return getId() == product.getId() &&
                Double.compare(getPrice(), product.getPrice()) == 0 &&
                getName().equals(product.getName());
    }

    /**
     * Returns a hash code value for the product.
     * The hash code is calculated based on the product's id, name, and price.
     *
     * @return a hash code value for this product
     */
    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + getName().hashCode();
        result = 31 * result + Double.hashCode(getPrice());
        return result;
    }
}
