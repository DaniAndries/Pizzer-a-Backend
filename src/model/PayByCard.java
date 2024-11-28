package model;

public class PayByCard implements Payable{
    @Override
    public void pay(double amount) {
        System.out.println("You have paid:" + amount+"€ by card");
    }
}
