package model;

/**
 * Represents the various states of an order in the system.
 * <p>
 * This enum defines the lifecycle of an order, capturing the different
 * states an order can be in as it progresses from creation to completion.
 * </p>
 *
 * <p>Order States:</p>
 * <ul>
 *   <li><strong>PENDING:</strong> The order has been created but not yet processed.</li>
 *   <li><strong>FINISHED:</strong> The order has been processed and is completed.</li>
 *   <li><strong>DELIVERED:</strong> The order has been delivered to the customer.</li>
 *   <li><strong>CANCELED:</strong> The order has been canceled and will not be processed.</li>
 * </ul>
 *
 * @author DaniAndries
 * @version 0.1
 */
public enum OrderState {
    /** The order has been created but not yet processed. */
    PENDING,

    /** The order has been processed and is completed. */
    FINISHED,

    /** The order has been delivered to the customer. */
    DELIVERED,

    /** The order has been canceled and will not be processed. */
    CANCELED;
}
