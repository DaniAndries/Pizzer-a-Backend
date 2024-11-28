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
    @CsvIgnore
    private float linePrice;

    public OrderLine(int id, int amount, Product product, float linePrice) {
        this.id = id;
        this.amount = amount;
        this.product = product;
    }

    public OrderLine(int id, int amount, float linePrice) {
        this.id = id;
        this.amount = amount;
        this.linePrice = linePrice;
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
}
