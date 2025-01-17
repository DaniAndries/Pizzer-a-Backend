package model;

/**
 * Represents a payment method where the payment is made using cash.
 * <p>
 * This class implements the {@link Payable} interface, providing the
 * functionality to handle cash payments.
 * </p>
 *
 * @author DaniAndries
 * @version 0.1
 */
public class PayByCash extends Payable {

    /**
     * Constructs a new PayByCash instance.
     * <p>
     * This constructor is used to create a new payment method
     * that allows payments to be made in cash.
     * </p>
     */
    public PayByCash() {
        // Default constructor
    }

    /**
     * Processes a cash payment for the specified amount.
     *
     * @param amount The amount of money to be paid.
     */
    @Override
    public void pay(double amount) {
        System.out.println("You have paid: " + amount + "€ in cash");
    }

    @Override
    public int paymentMethod() {
        return 1;
    }
}
