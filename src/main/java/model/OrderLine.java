package model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvIgnore;
import jakarta.persistence.*;

import javax.lang.model.element.Name;

/**
 * Represents a line item in an order, including the product and its quantity.
 * This class calculates the total price for the product line based on the quantity ordered.
 *
 * @author DaniAndries
 * @version 0.1
 */
@Entity
@Table(name = "order_line")
public class OrderLine {
    @Id
    @SequenceGenerator(name = "orderline_seq", sequenceName = "orderline_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orderline_seq")
    @CsvBindByName(column = "IDENTIFICATION")
    private int id;
    @CsvBindByName(column = "QUANTITY")
    private int amount;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @CsvIgnore
    private Product product;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id", nullable = false) // Explicit join column
    private Order order;

    /**
     * Constructs an OrderLine with the specified identifier, quantity, and product.
     *
     * @param id      the unique identifier for this order line, typically auto-generated
     * @param amount  the quantity of the product included in this order line
     * @param product the product associated with this order line
     */
    public OrderLine(int id, int amount, Product product) {
        this.id = id;
        this.amount = amount;
        this.product = product;
    }

    /**
     * Constructs an OrderLine with the specified identifier and quantity.
     * This constructor is typically used when the product is set later.
     *
     * @param id      the unique identifier for this order line
     * @param amount  the quantity of the product in this order line
     */
    public OrderLine(int id, int amount) {
        this.id = id;
        this.amount = amount;
    }

    /**
     * Constructs an OrderLine with the specified quantity and product.
     * This constructor is useful when the product is available but not its identifier.
     *
     * @param amount  the quantity of the product in this order line
     * @param product the product associated with this order line
     */
    public OrderLine(int amount, Product product) {
        this.amount = amount;
        this.product = product;
    }

    public OrderLine() {
    }

    /**
     * Calculates the total price for this order line.
     * The total price is computed by multiplying the product's price by the quantity.
     *
     * @return the total price for this order line, calculated as product price * quantity
     */
    public float calculateLinePrice() {
        return (float) (this.product.getPrice() * amount);
    }

    /**
     * Gets the quantity of the product in this order line.
     *
     * @return the quantity of the product
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Sets the quantity of the product in this order line.
     *
     * @param amount the new quantity of the product
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }

    /**
     * Gets the unique identifier for this order line.
     *
     * @return the unique identifier
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique identifier for this order line.
     *
     * @param id the new unique identifier
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the product associated with this order line.
     *
     * @return the product for this order line
     */
    public Product getProducto() {
        return product;
    }

    /**
     * Sets the product for this order line.
     *
     * @param product the new product to associate with this order line
     */
    public void setProducto(Product product) {
        this.product = product;
    }

    /**
     * Compares this order line to another object for equality.
     * Two OrderLine objects are considered equal if they have the same quantity
     * and the same product, as well as the same line price.
     *
     * @param o the object to compare to
     * @return true if this order line is equal to the specified object; false otherwise
     */
    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof OrderLine orderLine)) return false;

        return getAmount() == orderLine.getAmount() &&
                Float.compare(calculateLinePrice(), orderLine.calculateLinePrice()) == 0 &&
                product.equals(orderLine.product);
    }

    /**
     * Returns a hash code value for this order line.
     * The hash code is calculated based on the amount and the product.
     *
     * @return a hash code value for this order line
     */
    @Override
    public int hashCode() {
        int result = getAmount();
        result = 31 * result + product.hashCode();
        result = 31 * result + Float.hashCode(calculateLinePrice());
        return result;
    }

    /**
     * Returns a string representation of this order line, including the
     * unique identifier, quantity, product details, and calculated line price.
     *
     * @return a string representation of this order line
     */
    @Override
    public String toString() {
        calculateLinePrice(); // Ensures that the line price is calculated before converting to string
        return "OrderLine{" +
                "id=" + id +
                ", amount=" + amount +
                ", product=" + product +
                ", linePrice=" + calculateLinePrice() +
                '}';
    }
}
