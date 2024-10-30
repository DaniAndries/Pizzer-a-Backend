import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.opencsv.exceptions.CsvValidationException;
import controller.ControladorCliente;
import controller.ControladorPedido;
import controller.ControladorProducto;
import model.*;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        /**
         * Creación de usuario e instancia del ControladorCliente
         */
        ControladorCliente controlCiente = new ControladorCliente();
        controlCiente.registrarCliente("Calle 13", "Pepe", "654654654C", "ejemplo@ejemplo.ej", "+34 624653846", "contraseña");

        /**
         * Login para instanciar el cliente actual
         */
        controlCiente.loginCliente("ejemplo@ejemplo.ej", "contraseña");

        /**
         * Instanciado de ingredientes y sus alérgenos y lista de ellos
         */
        Ingrediente tomate = new Ingrediente(1, "Tomate", List.of(""));
        Ingrediente queso = new Ingrediente(2, "Queso Mozzarella", List.of("Lactosa"));
        Ingrediente harina = new Ingrediente(3, "Harina de Trigo", Arrays.asList("Gluten", "Wiwi"));
        Ingrediente pepperoni = new Ingrediente(4, "Pepperoni", List.of("Soja"));

        List<Ingrediente> ingredientsList = new ArrayList<>();
        ingredientsList.add(tomate);
        ingredientsList.add(queso);
        ingredientsList.add(harina);
        ingredientsList.add(pepperoni);

        /**
         * Instanciado de pizzas y sus listas de ingredientes
         */
        // Pizza 1: Margarita (mediana)
        List<Ingrediente> ingredientesMargarita = Arrays.asList(tomate, queso, harina);
        Producto pizza1 = new Pizza(1, "Pizza Margarita", 8.99, ingredientesMargarita, "MEDIANA");

        // Pizza 2: Pepperoni (grande)
        List<Ingrediente> ingredientesPepperoni = Arrays.asList(tomate, queso, harina, pepperoni);
        Producto pizza2 = new Pizza(2, "Pizza Pepperoni", 12.99, ingredientesPepperoni, "GRANDE");

        // Pizza 3: Vegana (pequeña)
        List<Ingrediente> ingredientesVegana = Arrays.asList(tomate, harina);  // Sin queso
        Producto pizza3 = new Pizza(3, "Pizza Vegana", 9.49, ingredientesVegana, "PEQUEÑA");

        /**
         * Añadido de linea pedido e instanciado de Controlador Pedido
         */
        ControladorPedido controlPedido1 = controlCiente.agregarLineaPedido(pizza1);
        ControladorPedido controlPedido2 = controlCiente.agregarLineaPedido(pizza2);

        /**
         * Comprobante de que se ha instanciado
         */
        System.out.println("Pedido 1: " + controlPedido1.getPedidoActual().getEstado());
        System.out.println("Pedido 2: " + controlPedido2.getPedidoActual().getEstado());

        String espaciado = "-----------------------";

        System.out.println(" ");
        System.out.println(espaciado);
        System.out.println(" ");

        /**
         * Comprobante de que se pueden añadir varias lineas de pedido
         */
        controlPedido1.agregarLineaPedido(pizza3, controlPedido1.getPedidoActual().getCliente(), 3);

        System.out.println(" ");
        System.out.println(espaciado);
        System.out.println(" ");

        /**
         * Finalizamos un pedido y el otro lo cancelamos
         */
        int idPedido1 = controlPedido1.finalizarPedido();
        for (Pedido pedido : controlPedido1.getListaPedidos()) {
            if (pedido.getId() == idPedido1) {
                System.out.println("Pedido 1: " + pedido.getEstado());
                System.out.println("Precio: " + pedido.getPrecioTotal());
            }
        }
        int idPedido2 = controlPedido2.cancelarPedido();
        for (Pedido pedido : controlPedido2.getListaPedidos()) {
            if (pedido.getId() == idPedido2) {
                System.out.println("Pedido 2: " + pedido.getEstado());
                System.out.println("Precio: " + pedido.getPrecioTotal());
            }
        }

        System.out.println(" ");
        System.out.println(espaciado);
        System.out.println(" ");

        /**
         * Importamos los clientes
         */
        try {
            controlCiente.importAdminClient();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        controlCiente.getListaClientes().forEach(System.out::println);

        System.out.println(" ");
        System.out.println(espaciado);
        System.out.println(" ");

        try {
            controlCiente.jaxbExport();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
        controlCiente.setListaClientes(new ArrayList<>());

        try {
            controlCiente.jaxbImport();
            controlCiente.getListaClientes().forEach(System.out::println);
        } catch (IOException | JAXBException e) {
            throw new RuntimeException(e);
        }

        ControladorProducto controlProducto = new ControladorProducto(ingredientsList);

        try {
            controlProducto.openCsvExport();
        } catch (CsvRequiredFieldEmptyException | FileNotFoundException | CsvDataTypeMismatchException e) {
            throw new RuntimeException(e);
        }

        System.out.println(" ");
        System.out.println(espaciado);
        System.out.println(" ");

        List<Ingrediente> ingredientListAux;
        try {
            ingredientListAux = controlProducto.openCsvImport();
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException(e);
        }

        ingredientListAux.forEach(System.out::println);
    }
}
