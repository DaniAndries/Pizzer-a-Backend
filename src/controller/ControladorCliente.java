package controller;

import model.Cliente;
import model.Producto;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ControladorCliente {
    private Cliente clienteActual;
    private List<Cliente> listaClientes = new ArrayList<>();

    public ControladorCliente(String email, String password) {
        loginCliente(email, password);
    }

    public ControladorCliente() {
    }

    public void registrarCliente(String direccion, String nombre, String dni, String email, String telefono, String password) {
        listaClientes.add(new Cliente(ThreadLocalRandom.current().nextInt(999999), direccion, nombre, dni, email, telefono, password));
    }

    public void loginCliente(String email, String password) {
        for (Cliente listaCliente : listaClientes) {
            if (listaCliente.getEmail().equals(email) && listaCliente.getPassword().equals(password)) {
                this.clienteActual = listaCliente;
            }
        }
        if (clienteActual == null) System.err.println("Usuario o contraseña incorrecta");
    }

    public ControladorPedido agregarLineaPedido(Producto producto) throws IllegalStateException {
        if (this.clienteActual != null) {
            ControladorPedido controladorDePedido = new ControladorPedido();
            controladorDePedido.agregarLineaPedido(producto, this.clienteActual, 1);
            return controladorDePedido;
        } else throw (new IllegalStateException());
    }

    public void importAdminClient() throws IOException {
        List<Cliente> clientList = GestionFicheros.importClient();
        listaClientes.addAll(clientList);
    }

    public void jaxbExport() throws JAXBException {
        GestionFicheros.clientToXml(this.listaClientes);
    }

    public void jaxbImport() throws IOException, JAXBException {
        this.listaClientes = GestionFicheros.xmlToClient();
    }


    public List<Cliente> getListaClientes() {
        return listaClientes;
    }

    public void setListaClientes(List<Cliente> listaClientes) {
        this.listaClientes = listaClientes;
    }
}
