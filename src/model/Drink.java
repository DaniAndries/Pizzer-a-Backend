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
    public final boolean equals(Object o) {
        if (!(o instanceof Drink drink)) return false;
        if (!super.equals(o)) return false;

        return getSize() == drink.getSize();
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + getSize().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Drink{" +
                super.toString() +
                "size=" + size +
                "} ";
    }
}
