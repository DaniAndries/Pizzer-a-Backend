package model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
@Data
@AllArgsConstructor
//Genera constructor con parámetros
@NoArgsConstructor
//Genera constructor sin parámetros
@EqualsAndHashCode
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "paid")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Pizza.class, name = "card"),
        @JsonSubTypes.Type(value = Pasta.class, name = "cash"),
})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Payable {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    private int id;

    /**
     * Processes a payment for the specified amount.
     *
     * @param amount The amount of money to be paid.
     */
    public abstract  void pay(double amount);

    public abstract int paymentMethod();

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
}
