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

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Pizza pizza)) return false;
        if (!super.equals(o)) return false;

        return getSize() == pizza.getSize() && getIngredients().equals(pizza.getIngredients());
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + getSize().hashCode();
        result = 31 * result + getIngredients().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Pizza{" +
                super.toString() +
                "size=" + size +
                ", ingredients=" + ingredients +
                "} " ;
    }
}
