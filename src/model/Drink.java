package model;

public class Drink extends Product {
    private Size size;

    public Drink(int id, String name, double price, Size size) {
        super(id, name, price);
        this.size = size;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "Drink{" +
                "size=" + size +
                "} " + super.toString();
    }
}
