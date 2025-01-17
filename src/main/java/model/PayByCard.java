package model;

/**
 * Represents a payment method where the payment is made using a card.
 * <p>
 * This class implements the {@link Payable} interface, providing the
 * functionality to handle card payments.
 * </p>
 *
 * @author DaniAndries
 * @version 0.1
 */
public class PayByCard extends Payable {

    /**
     * Constructs a new PayByCard instance.
     * <p>
     * This constructor is used to create a new payment method
     * that allows payments to be made by card.
     * </p>
     */
    public PayByCard() {
        // Default constructor
    }

    /**
     * Processes a card payment for the specified amount.
     *
     * @param amount The amount of money to be paid.
     */
    @Override
    public void pay(double amount) {
        System.out.println("You have paid: " + amount + "€ by card");
    }

    @Override
    public int paymentMethod() {
        return 0;
    }
}
