package model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
@Entity
@Data
@NoArgsConstructor
//Genera constructor sin parámetros
@EqualsAndHashCode
public class PayByCard extends Payable {
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
