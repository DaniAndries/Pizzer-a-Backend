package model;

import java.util.List;

public class Pasta extends Producto{

    private List<Ingrediente> ingredientes;

    public Pasta(int id, String nombre, double precio, List<Ingrediente> ingredientes) {
        super(id, nombre, precio);
        this.ingredientes = ingredientes;
    }
}
