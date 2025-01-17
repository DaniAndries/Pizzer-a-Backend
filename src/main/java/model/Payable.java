package model;

import jakarta.persistence.*;

/**
 * Represents a general payment method.
 * <p>
 * This interface defines a contract for any class that implements it to provide
 * a mechanism for processing payments. Classes implementing this interface
 * should define the logic for how payments are handled, whether by cash, card,
 * or any other payment method.
 * </p>
 *
 * @author DaniAndries
 * @version 0.1
 */
@Entity
public abstract class Payable {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private int id;

    /**
     * Processes a payment for the specified amount.
     *
     * @param amount The amount of money to be paid.
     */
    public abstract  void pay(double amount);

    public abstract int paymentMethod();
}
