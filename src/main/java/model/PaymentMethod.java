package model;

/**
 * Represents the payment methods available in the application.
 * <p>
 * This enum provides a set of constants that can be used to specify
 * the method by which a payment is made.
 * </p>
 * <p>
 * The available payment methods are:
 * <ul>
 *     <li>{@link #CARD} - Payment made using a credit or debit card.</li>
 *     <li>{@link #CASH} - Payment made using cash.</li>
 *     <li>{@link #UNPAID} - Indicates that the payment has not been made.</li>
 * </ul>
 *
 * @author DaniAndries
 * @version 0.1
 */
public enum PaymentMethod {
    /** Payment made via credit or debit card */
    CARD,

    /** Payment made using cash */
    CASH,

    /** Payment that has not yet been made */
    UNPAID
}
