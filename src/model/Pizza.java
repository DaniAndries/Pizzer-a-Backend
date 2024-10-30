package model;

import java.util.List;

public class Pizza extends Producto {

    private Size size;
    private List<Ingrediente> ingredientes;

    public Pizza(int id, String nombre, double precio, List<Ingrediente> ingredientes, String size) {
        super(id, nombre, precio);
        this.ingredientes = ingredientes;
        this.size=Size.valueOf(size);
    }
}
