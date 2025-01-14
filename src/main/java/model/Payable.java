package model;

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
public interface Payable {

    /**
     * Processes a payment for the specified amount.
     *
     * @param amount The amount of money to be paid.
     */
    void pay(double amount);
}
