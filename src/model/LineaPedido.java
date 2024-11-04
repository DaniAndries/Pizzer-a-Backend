package model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvIgnore;

public class LineaPedido {
    @CsvBindByName(column = "IDENTIFICATION")
    private int id;
    @CsvBindByName(column = "QUANTITY")
    private int cantidad;
    @CsvIgnore
    private Producto producto;
    @CsvIgnore
    private float precioDeLinea;

    public LineaPedido(int id, int cantidad, Producto producto) {
        this.id = id;
        this.cantidad = cantidad;
        this.producto = producto;
    }

    public float calcularPrecioLinea(){
        return (float) (this.producto.getPrecio() * cantidad);
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}
