package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Pedido implements Pagable {
    private EstadoPedido estado;
    private int id;
    private Date fecha;
    private float precioTotal;
    private List<LineaPedido> lineasDePedidos=new ArrayList<>();
    private String metodoDePago;
    private Cliente cliente;

    public Pedido(int id, Date fecha, LineaPedido lineaDePedido, Cliente cliente) {
        this.id = id;
        this.fecha = fecha;
        this.lineasDePedidos.add(lineaDePedido);
        this.cliente = cliente;
        this.estado= EstadoPedido.PENDIENTE;
    }

    public void finalizar() {
        calcularPrecioTotal();
        pagar(precioTotal);
    }

    @Override
    public void pagar(double cantidad) {
        setEstado(EstadoPedido.FINALIZADO);
        pagarEfectivo(cantidad);
        pagarTarjeta(cantidad);
    }

    @Override
    public void pagarTarjeta(double cantidad) {
        this.metodoDePago = "Tarjeta";
    }

    @Override
    public void pagarEfectivo(double cantidad) {
        this.metodoDePago = "Efectivo";
    }

    public void calcularPrecioTotal() {
        float precioAuxiliar = 0;
        for (LineaPedido lineaPedido : lineasDePedidos) {
            precioAuxiliar += lineaPedido.calcularPrecioLinea();
        }
        this.precioTotal = precioAuxiliar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public float getPrecioTotal() {
        return precioTotal;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    public List<LineaPedido> getLineaDePedido() {
        return lineasDePedidos;
    }

    public void addLineaDePedido(LineaPedido lineaPedido) {
        this.lineasDePedidos.add(lineaPedido);
    }

    public String getMetodoDePago() {
        return metodoDePago;
    }

    public void setMetodoDePago(String metodoDePago) {
        this.metodoDePago = metodoDePago;
    }

    public Cliente getCliente() {
        return cliente;
    }
}


