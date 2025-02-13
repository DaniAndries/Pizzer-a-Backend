package model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
@Entity
@Data
@NoArgsConstructor
//Genera constructor sin parámetros
@EqualsAndHashCode
public class PayByCash extends Payable {
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
