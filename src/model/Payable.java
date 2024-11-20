package model;

public interface Payable {
    void pay(double amount);

    void payByCard(double amount);

    void payByCash(double amount);
}
