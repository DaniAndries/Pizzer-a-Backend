package model;

public class Drink extends Product {
    private String size;

    public Drink(int id, String name, double price, String size) {
        super(id, name, price);

    }

    private enum size {BIG, MEDIUM, SMALL;}
}
