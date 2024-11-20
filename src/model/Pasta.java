package model;

import java.util.List;

public class Pasta extends Product {

    private List<Ingredient> ingredients;

    public Pasta(int id, String name, double price, List<Ingredient> ingredients) {
        super(id, name, price);
        this.ingredients = ingredients;
    }
}
