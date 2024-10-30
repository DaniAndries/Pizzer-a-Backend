package model;

public class Bebida extends Producto{
    private enum size {GRANDE, MEDIANA, PEQUEÑA;}
    private String size;
    public Bebida(int id, String nombre, double precio, String tamaño) {
        super(id, nombre, precio);

    }
}
