package model;

public interface Pagable {
    void pagar(double cantidad);

    void pagarTarjeta(double cantidad);

    void pagarEfectivo(double cantidad);
}
