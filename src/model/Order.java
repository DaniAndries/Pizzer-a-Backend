package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order {
    private int id;
    private Date orderDate;
    private OrderState state;
    private PaymentMethod paymentMethod;
    private List<OrderLine> orderLines = new ArrayList<>();
    private Client client;

    public Order(int id, Date orderDate, OrderState state, PaymentMethod paymentMethod, List<OrderLine> orderLines, Client client) {
        this.id = id;
        this.orderDate = orderDate;
        this.state = state;
        this.paymentMethod = paymentMethod;
        this.orderLines = orderLines;
        this.client = client;
    }

    public Order(int id, Date orderDate, OrderState state, PaymentMethod paymentMethod, Client client) {
        this.id = id;
        this.orderDate = orderDate;
        this.state = state;
        this.paymentMethod = paymentMethod;
        this.client = client;
    }

    public Order(int id, Date orderDate, OrderLine orderLine, Client client) {
        this.id = id;
        this.orderDate = orderDate;
        this.state = OrderState.PENDING;
        this.paymentMethod = PaymentMethod.UNPAID;
        this.orderLines.add(orderLine);
        this.client = client;
    }

    public Order(Date orderDate, OrderState state, OrderLine orderLine, Client client) {
        this.orderDate = orderDate;
        this.state = state;
        this.paymentMethod = PaymentMethod.UNPAID;
        this.orderLines.add(orderLine);
        this.client = client;
    }

    public void finalizar(Payable payable, PaymentMethod paymentMethod) {
        payable.pay(getTotalPrice());
        setPaymentMethod(paymentMethod);
    }

    public double getTotalPrice() {
        double auxiliaryPrice = 0;
        for (OrderLine orderLine : orderLines) {
            auxiliaryPrice += orderLine.calculateLinePrice();
        }
        return auxiliaryPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public OrderState getState() {
        return state;
    }

    public void setState(OrderState state) {
        this.state = state;
    }

    public List<OrderLine> getOrderLines() {
        return orderLines;
    }

    public void setOrderLines(List<OrderLine> orderLines) {
        this.orderLines = orderLines;
    }

    public void addOrderLine(OrderLine orderLine) {
        this.orderLines.add(orderLine);
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Client getClient() {
        return client;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Order order)) return false;

        return getOrderDate().equals(order.getOrderDate()) && getState() == order.getState() && getPaymentMethod() == order.getPaymentMethod() && getOrderLines().equals(order.getOrderLines()) && getClient().equals(order.getClient());
    }

    @Override
    public int hashCode() {
        int result = getOrderDate().hashCode();
        result = 31 * result + getState().hashCode();
        result = 31 * result + getPaymentMethod().hashCode();
        result = 31 * result + getOrderLines().hashCode();
        result = 31 * result + getClient().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderDate=" + orderDate +
                ", state=" + state +
                ", paymentMethod=" + paymentMethod +
                ", orderLines=" + orderLines +
                ", client=" + client +
                '}';
    }
}
