package model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvIgnore;

public class OrderLine {
    @CsvBindByName(column = "IDENTIFICATION")
    private int id;
    @CsvBindByName(column = "QUANTITY")
    private int amount;
    @CsvIgnore
    private Product product;

    public OrderLine(int id, int amount, Product product) {
        this.id = id;
        this.amount = amount;
        this.product = product;
    }

    public OrderLine(int id, int amount) {
        this.id = id;
        this.amount = amount;
    }

    public OrderLine( int amount, Product product) {
        this.amount = amount;
        this.product = product;
    }


    public float calculateLinePrice(){
        return (float) (this.product.getPrice() * amount);
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Product getProducto() {
        return product;
    }

    public void setProducto(Product product) {
        this.product = product;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof OrderLine orderLine)) return false;

        return getAmount() == orderLine.getAmount() && Float.compare(calculateLinePrice(), orderLine.calculateLinePrice()) == 0 && product.equals(orderLine.product);
    }

    @Override
    public int hashCode() {
        int result = getAmount();
        result = 31 * result + product.hashCode();
        result = 31 * result + Float.hashCode(calculateLinePrice());
        return result;
    }

    @Override
    public String toString() {
        calculateLinePrice();
        return "OrderLine{" +
                "id=" + id +
                ", amount=" + amount +
                ", product=" + product +
                ", linePrice=" + calculateLinePrice() +
                '}';
    }
}
