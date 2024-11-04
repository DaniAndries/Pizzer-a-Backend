package controller;


import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import model.*;

import javax.xml.bind.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GestionFicheros {
    private static final String FILEXML = "clients.xml";
    private static final String FILECSV = "ingredientes.csv";
    private static final String FILEXMLEXAMEN = "clients.xml";
    private static final String FILECSVEXAMEN = "lineas_pedido.csv";

    private GestionFicheros() {
    }

    // Method to import clients from a file (using String array)
    public static List<Cliente> importClient() throws IOException {
        // Read all lines from the file into a List and convert it to a String[]
        String[] lineas = Files.readAllLines(Path.of("admin.txt")).toArray(new String[0]);

        // Convert each line into a String array using split, and then add clients
        String[][] dataList = Arrays.stream(lineas)
                .map(line -> line.split("[;,|]")) // Split by multiple delimiters
                .toArray(String[][]::new);        // Convert to 2D array

        return addAdminClients(dataList);
    }

    /**
     * Method to add clients from a 2D String array
     *
     * @param list
     * @return list<Cliente></Cliente>
     */
    private static List<Cliente> addAdminClients(String[][] list) throws NumberFormatException, ArrayIndexOutOfBoundsException {
        List<Cliente> clientList = new ArrayList<>();
        for (String[] data : list) {
            Cliente cliente = getCliente(data);
            clientList.add(cliente);
        }
        return clientList;
    }

    private static Cliente getCliente(String[] data) {
        Cliente cliente = new Cliente(
                Integer.parseInt(data[0].trim()),  // ID
                data[3].trim(),                    // Address
                data[2].trim(),                    // Name
                data[1].trim(),                    // DNI
                data[5].trim(),                    // Email
                data[4].trim(),                    // Phone
                data[6].trim(),                    // Password
                true
        );
        cliente.setAdmin(true);
        return cliente;
    }

    /**
     * Method to export clients with JAXB to XML
     */
    public static void exportXml(List<Cliente> list) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(ClientWrapper.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(new ClientWrapper(list), new File(FILEXML));
    }

    /**
     * Method to import clients with JAXB from XML
     */
    public static List<Cliente> xmlImport() throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(ClientWrapper.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        ClientWrapper clientList = (ClientWrapper) unmarshaller.unmarshal(new File(FILEXML));
        return clientList.getClientList();
    }

    /**
     * Method to export ingredients with OpenCSV to csv
     */
    public static void csvExport(List<Ingrediente> ingredientList) throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, FileNotFoundException {
        try (PrintWriter pw = new PrintWriter(FILECSV)) {
            StatefulBeanToCsv<Ingrediente> beanToCsv = new StatefulBeanToCsvBuilder<Ingrediente>(pw).withSeparator(';').build();
            beanToCsv.write(ingredientList);
        }
    }

    /**
     * Method to import ingredients with OpenCSV from CSV
     */
    public static List<Ingrediente> importCsv() throws IOException {
        List<Ingrediente> listaIngredientes;

        try (FileReader fileReader = new FileReader(FILECSV)) {
            CsvToBean<Ingrediente> csvToBean = new
                    CsvToBeanBuilder<Ingrediente>(fileReader).withType(Ingrediente.class).withSeparator(';').build();
            listaIngredientes = csvToBean.parse();
        }
        return listaIngredientes;
    }

    /**
     * Implementa un metodo llamado importarPizzas, que dado un fichero llamado pizzas.txt, lo deserialice,
     * devolviendo una lista de objetos Pizza
     */
    public static List<Pizza> importPizzasTxt() throws IOException {
        String[] lineas = Files.readAllLines(Path.of("pizzas.txt")).toArray(new String[0]);
        // Convert each line into a String array using split, and then add clients
        String[][] dataList = Arrays.stream(lineas)
                .map(line -> line.split("[*]")) // Split by multiple delimiters
                .toArray(String[][]::new);        // Convert to 2D array

        return addPizzas(dataList);
    }

    /**
     * Method to add clients from a 2D String array
     */
    private static List<Pizza> addPizzas(String[][] list) throws NumberFormatException, ArrayIndexOutOfBoundsException {
        List<Pizza> pizzaList = new ArrayList<>();
        for (String[] data : list) {
            Pizza pizza = getProducto(data);
            pizzaList.add(pizza);
        }
        return pizzaList;
    }

    // int id, String nombre, double precio, Size size
    private static Pizza getProducto(String[] data) {
        return new Pizza(
                Integer.parseInt(data[0].trim()),  // ID
                data[1].trim(),                    // nombre
                Double.parseDouble(data[2].trim()),// precio
                Size.valueOf(data[3].trim())       // size

        );
    }

    /**
     * Method to export with JAXB to XML
     * El campo admin sea atributo XML, en lugar de elemento.
     * El campo password no se serialice por seguridad.
     * El campo id sea elemento XML.
     */
    public static void clientToXml(List<Cliente> list) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(ClientWrapper.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(new ClientWrapper(list), new File(FILEXMLEXAMEN));
    }

    /**
     * Method to import with JAXB from XML
     * Actualiza el metodo de importación de clientes, para que tenga en cuenta los cambios realizados en la exportación de clientes.
     */
    public static List<Cliente> xmlToClient() throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(ClientWrapper.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        ClientWrapper clientList = (ClientWrapper) unmarshaller.unmarshal(new File(FILEXMLEXAMEN));
        return clientList.getClientList();
    }

    /**
     * Method to export with OpenCSV to csv
     */
    public static void lineaPedidoToCsv(List<LineaPedido> lineaPedido) throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, FileNotFoundException {
        try (PrintWriter pw = new PrintWriter(FILECSVEXAMEN)) {
            StatefulBeanToCsv<LineaPedido> beanToCsv = new StatefulBeanToCsvBuilder<LineaPedido>(pw).withSeparator(',').build();
            beanToCsv.write(lineaPedido);
        }
    }

    /**
     * Method to import with OpenCSV from CSV
     */
    public static List<LineaPedido> csvToLineaPedido() throws IOException {
        List<LineaPedido> listaIngredientes;

        try (FileReader fileReader = new FileReader(FILECSVEXAMEN)) {
            CsvToBean<LineaPedido> csvToBean = new
                    CsvToBeanBuilder<LineaPedido>(fileReader).withType(LineaPedido.class).withSeparator(',').build();
            listaIngredientes = csvToBean.parse();
        }
        return listaIngredientes;
    }
}

