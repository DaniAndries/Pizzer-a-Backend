package model;

import com.mysql.cj.xdevapi.Client;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represents an order in the system, containing details about the customer,
 * order lines, payment method, and order state. This class manages the
 * details of an order and allows for the calculation of the total order price.
 *
 * @author DaniAndries
 * @version 0.1
 */
@Entity
@Table(name = "customer_order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    @Column(name = "order_date")
    @Temporal(TemporalType.DATE)
    private Date orderDate;
    @Enumerated(EnumType.STRING)
    private OrderState state;
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<OrderLine> orderLines = new ArrayList<>();
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    /**
     * Constructs an Order with the specified parameters.
     *
     * @param id            the unique identifier for this order
     * @param orderDate     the date when the order was placed
     * @param state         the current state of the order
     * @param paymentMethod the method used for payment
     * @param orderLines    the list of order lines associated with this order
     * @param customer      the customer who placed the order
     */
    public Order(int id, Date orderDate, OrderState state, PaymentMethod paymentMethod, List<OrderLine> orderLines, Customer customer) {
        this.id = id;
        this.orderDate = orderDate;
        this.state = state;
        this.paymentMethod = paymentMethod;
        this.orderLines = orderLines;
        this.customer = customer;
    }

    /**
     * Constructs an Order with the specified parameters, without initial order lines.
     *
     * @param id            the unique identifier for this order
     * @param orderDate     the date when the order was placed
     * @param state         the current state of the order
     * @param paymentMethod the method used for payment
     * @param customer      the customer who placed the order
     */
    public Order(int id, Date orderDate, OrderState state, PaymentMethod paymentMethod, Customer customer) {
        this.id = id;
        this.orderDate = orderDate;
        this.state = state;
        this.paymentMethod = paymentMethod;
        this.customer = customer;
    }

    /**
     * Constructs an Order with a single order line, defaulting to pending state and unpaid method.
     *
     * @param id        the unique identifier for this order
     * @param orderDate the date when the order was placed
     * @param orderLine the initial order line for this order
     * @param customer  the customer who placed the order
     */
    public Order(int id, Date orderDate, OrderLine orderLine, Customer customer) {
        this.id = id;
        this.orderDate = orderDate;
        this.state = OrderState.PENDING;
        this.paymentMethod = PaymentMethod.UNPAID;
        this.orderLines.add(orderLine);
        this.customer = customer;
    }

    /**
     * Constructs an Order with a single order line and a specific state, defaulting to unpaid method.
     *
     * @param orderDate the date when the order was placed
     * @param state     the current state of the order
     * @param orderLine the initial order line for this order
     * @param customer  the customer who placed the order
     */
    public Order(Date orderDate, OrderState state, OrderLine orderLine, Customer customer) {
        this.orderDate = orderDate;
        this.state = state;
        this.paymentMethod = PaymentMethod.UNPAID;
        this.orderLines.add(orderLine);
        this.customer = customer;
    }

    public Order() {
    }

    /**
     * Completes the order by processing the payment and recording the payment method.
     * <p>
     * This method uses the provided {@code Payable} strategy to handle the payment
     * for the total price of the order. Once the payment is successfully processed,
     * the order's state is updated to {@code FINISHED}, and the specified payment
     * method is recorded.
     *
     * @param payable       the payment strategy used to process the order's payment
     */
    public void finalizeOrder(Payable payable, Customer customer) {
        payable.pay(getTotalPrice());
        setState(OrderState.FINISHED);
        setPaymentMethod(paymentMethod);
    }

    /**
     * Calculates the total price of the order by summing the prices of all order lines.
     *
     * @return the total price of the order
     */
    public double getTotalPrice() {
        double auxiliaryPrice = 0;
        for (OrderLine orderLine : orderLines) {
            auxiliaryPrice += orderLine.calculateLinePrice();
        }
        return auxiliaryPrice;
    }

    /**
     * Gets the unique identifier for this order.
     *
     * @return the unique identifier
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique identifier for this order.
     *
     * @param id the new unique identifier
     */
    public void setId(int id) {
        this.id = id;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /**
     * Gets the date when this order was placed.
     *
     * @return the order date
     */
    public Date getOrderDate() {
        return orderDate;
    }

    /**
     * Sets the date for this order.
     *
     * @param orderDate the new order date
     */
    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    /**
     * Gets the current state of the order.
     *
     * @return the order state
     */
    public OrderState getState() {
        return state;
    }

    /**
     * Sets the current state of the order.
     *
     * @param state the new order state
     */
    public void setState(OrderState state) {
        this.state = state;
    }

    /**
     * Gets the list of order lines associated with this order.
     *
     * @return the list of order lines
     */
    public List<OrderLine> getOrderLines() {
        return orderLines;
    }

    /**
     * Sets the list of order lines for this order.
     *
     * @param orderLines the new list of order lines
     */
    public void setOrderLines(List<OrderLine> orderLines) {
        this.orderLines = orderLines;
    }

    /**
     * Adds a new order line to this order.
     *
     * @param orderLine the order line to be added
     */
    public void addOrderLine(OrderLine orderLine) {
        this.orderLines.add(orderLine);
    }

    /**
     * Gets the payment method used for this order.
     *
     * @return the payment method
     */
    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    /**
     * Sets the payment method used for this order.
     *
     * @param paymentMethod the new payment method
     */
    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    /**
     * Gets the customer associated with this order.
     *
     * @return the customer who placed the order
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * Compares this order to another object for equality.
     * Two Order objects are considered equal if they have the same order date,
     * state, payment method, order lines, and associated customer.
     *
     * @param o the object to compare to
     * @return true if this order is equal to the specified object; false otherwise
     */
    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Order order)) return false;

        return getOrderDate().equals(order.getOrderDate()) && getState() == order.getState() && getPaymentMethod() == order.getPaymentMethod() && getOrderLines().equals(order.getOrderLines()) && getCustomer().equals(order.getCustomer());
    }

    /**
     * Returns a hash code value for this order.
     * The hash code is calculated based on the order date, state, payment method,
     * order lines, and customer.
     *
     * @return a hash code value for this order
     */
    @Override
    public int hashCode() {
        int result = getOrderDate().hashCode();
        result = 31 * result + getState().hashCode();
        result = 31 * result + getPaymentMethod().hashCode();
        result = 31 * result + getOrderLines().hashCode();
        result = 31 * result + getCustomer().hashCode();
        return result;
    }

    /**
     * Returns a string representation of this order, including the id, order date,
     * state, payment method, order lines, and customer details.
     *
     * @return a string representation of this order
     */
    @Override
    public String toString() {
        return "Order{" + "id=" + id + ", orderDate=" + orderDate + ", state=" + state + ", paymentMethod=" + paymentMethod + ", orderLines=" + orderLines + ", customer=" + customer + '}';
    }
}
