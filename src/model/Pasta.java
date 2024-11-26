package model;

import java.util.List;

public class Pasta extends Product {

    private List<Ingredient> ingredients;

    public Pasta(int id, String name, double price) {
        super(id, name, price);
    }

    public Pasta(int id, String name, double price, List<Ingredient> ingredients) {
        super(id, name, price);
        this.ingredients = ingredients;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public String toString() {
        return "Pasta{" +super.toString()+
                "ingredients=" + ingredients +
                "} ";
    }
}
