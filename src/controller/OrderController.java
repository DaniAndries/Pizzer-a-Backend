package controller;

import model.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class OrderController {
    private Order actualOrder;
    private List<Order> ordersList = new ArrayList<>();

    public OrderController(Order actualOrder) {
        this.actualOrder = actualOrder;
    }

    public OrderController() {
    }

    public void addLineaOrder(Product product, Client clienteActual, int cantidad) throws IllegalStateException {
        if (this.actualOrder == null) {
            if (clienteActual == null) {
                throw (new IllegalStateException());
            }
            this.actualOrder = new Order(ThreadLocalRandom.current().nextInt(999999), new Date(), new OrderLine(1, cantidad, product), clienteActual);
        } else {
            this.actualOrder.addOrderLine(new OrderLine(4, cantidad, product));
        }
    }

    public int finalizeOrder() {
        int idactualOrder = this.actualOrder.getId();
        if (this.actualOrder.getState() == OrderState.PENDING) {
            this.actualOrder.finalizar();
            this.actualOrder.pay(this.actualOrder.getTotalPrice());
            this.ordersList.add(this.actualOrder);
            this.actualOrder = null;
            deliverOrder(idactualOrder);
        } else System.err.println("No se puede finalizar un Order ya finalizado, entregado o cancelado");
        return idactualOrder;
    }

    public int cancelOrder() {
        int idactualOrder = this.actualOrder.getId();
        if (this.actualOrder.getState() == OrderState.PENDING) {
            this.actualOrder.setState(OrderState.CANCELED);
            this.ordersList.add(this.actualOrder);
            this.actualOrder = null;
        } else System.err.println("No se puede cancelar un Order ya finalizado, entregado o cancelado");
        return idactualOrder;
    }

    public void deliverOrder(int id) {
        for (Order listaOrder : ordersList) {
            if (listaOrder.getId() == id) {
                if (listaOrder.getState() == OrderState.COMPLETED) {
                    listaOrder.setState(OrderState.DELIVERED);
                } else {
                    System.err.println("No se puede entregar un Order cancelado o pendiente de pago");
                }
                break;
            }
        }
    }

    public Order getactualOrder() {
        return actualOrder;
    }

    public List<Order> getordersList() {
        return ordersList;
    }
}


