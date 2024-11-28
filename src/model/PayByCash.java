package model;

public class PayByCash implements Payable{
    @Override
    public void pay(double amount) {
        System.out.println("You have paid:" + amount+"€ by cash");
    }
}
