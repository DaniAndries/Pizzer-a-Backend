package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order implements Payable {
    private OrderState state;
    private int id;
    private Date orderDate;
    private float totalPrice;
    private List<OrderLine> orderLines = new ArrayList<>();
    private String paymentMethod;
    private Client client;

    public Order(int id, Date orderDate, OrderLine orderLine, Client client) {
        this.id = id;
        this.orderDate = orderDate;
        this.orderLines.add(orderLine);
        this.client = client;
        this.state = OrderState.PENDING;
    }

    public void finalizar() {
        calcularPrecioTotal();
        pay(totalPrice);
    }

    @Override
    public void pay(double amount) {
        setState(OrderState.PAID);
        payByCash(amount);
        payByCard(amount);
    }

    @Override
    public void payByCard(double amount) {
        this.paymentMethod = "Card";
    }

    @Override
    public void payByCash(double amount) {
        this.paymentMethod = "Cash";
    }

    public void calcularPrecioTotal() {
        float auxiliaryPrice = 0;
        for (OrderLine orderLine : orderLines) {
            auxiliaryPrice += orderLine.calculateLinePrice();
        }
        this.totalPrice = auxiliaryPrice;
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

    public float getTotalPrice() {
        return totalPrice;
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

    public void addOrderLine(OrderLine orderLine) {
        this.orderLines.add(orderLine);
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Client getClient() {
        return client;
    }
}
