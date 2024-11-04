package controller;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import model.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ControladorPedido {
    private Pedido pedidoActual;
    private List<Pedido> listaPedidos = new ArrayList<>();

    public ControladorPedido(Pedido pedidoActual) {
        this.pedidoActual = pedidoActual;
    }

    public ControladorPedido() {
    }

    public void agregarLineaPedido(Producto producto, Cliente clienteActual, int cantidad) throws IllegalStateException {
        if (this.pedidoActual == null) {
            if (clienteActual == null) {
                throw (new IllegalStateException());
            }
            this.pedidoActual = new Pedido(ThreadLocalRandom.current().nextInt(999999), new Date(), new LineaPedido(1, cantidad, producto), clienteActual);
        } else {
            this.pedidoActual.addLineaDePedido(new LineaPedido(4, cantidad, producto));
        }
    }

    public int finalizarPedido() {
        int idPedidoActual = this.pedidoActual.getId();
        if (this.pedidoActual.getEstado() == EstadoPedido.PENDIENTE) {
            this.pedidoActual.finalizar();
            this.pedidoActual.pagar(this.pedidoActual.getPrecioTotal());
            this.listaPedidos.add(this.pedidoActual);
            this.pedidoActual = null;
            entregarPedido(idPedidoActual);
        } else System.err.println("No se puede finalizar un pedido ya finalizado, entregado o cancelado");
        return idPedidoActual;
    }

    public int cancelarPedido() {
        int idPedidoActual = this.pedidoActual.getId();
        if (this.pedidoActual.getEstado() == EstadoPedido.PENDIENTE) {
            this.pedidoActual.setEstado(EstadoPedido.CANCELADO);
            this.listaPedidos.add(this.pedidoActual);
            this.pedidoActual = null;
        } else System.err.println("No se puede cancelar un pedido ya finalizado, entregado o cancelado");
        return idPedidoActual;
    }

    public void entregarPedido(int id) {
        for (Pedido listaPedido : listaPedidos) {
            if (listaPedido.getId() == id) {
                if (listaPedido.getEstado() == EstadoPedido.FINALIZADO) {
                    listaPedido.setEstado(EstadoPedido.ENTREGADO);
                } else {
                    System.err.println("No se puede entregar un pedido cancelado o pendiente de pago");
                }
                break;
            }
        }
    }

    public void lineaPedidoToCsv(List<LineaPedido> lineaPedido) throws CsvRequiredFieldEmptyException, FileNotFoundException, CsvDataTypeMismatchException {
        GestionFicheros.lineaPedidoToCsv(lineaPedido);
    }

    public List<LineaPedido> csvToLineaPedido() throws IOException {
        return GestionFicheros.csvToLineaPedido();
    }

    public Pedido getPedidoActual() {
        return pedidoActual;
    }

    public List<Pedido> getListaPedidos() {
        return listaPedidos;
    }
}
