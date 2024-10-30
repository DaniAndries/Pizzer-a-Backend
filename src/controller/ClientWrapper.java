package controller;

import model.Cliente;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement // Raíz del documento XML
@XmlAccessorType(XmlAccessType.FIELD) // Acceso directo a campos
public class ClientWrapper {
    private List<Cliente> clientList = new ArrayList<>();

    public ClientWrapper(List<Cliente> clientList) {
        this.clientList = clientList;
    }

    public ClientWrapper() {
    }

    public List<Cliente> getClientList() {
        return clientList;
    }

    public void setClientList(List<Cliente> clientList) {
        this.clientList = clientList;
    }
}
