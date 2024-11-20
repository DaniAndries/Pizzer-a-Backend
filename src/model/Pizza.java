package model;

import java.util.List;

public class Pizza extends Product {

    private Size size;
    private List<Ingredient> ingredients;

    public Pizza(int id, String name, double price, List<Ingredient> ingredients, String size) {
        super(id, name, price);
        this.ingredients = ingredients;
        this.size = Size.valueOf(size);
    }

    public Pizza(int id, String name, double precio, Size size) {
        super(id, name, precio);
        this.size = size;
    }

    @Override
    public String toString() {
        return "Pizza{" +
                "size=" + size +
                "} " + super.toString();
    }
}
