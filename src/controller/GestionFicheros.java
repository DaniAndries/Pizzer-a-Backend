package controller;


import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import model.Cliente;
import model.Ingrediente;

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
     * Method to export with JAXB to XML
     */
    public static void clientToXml(List<Cliente> list) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(ClientWrapper.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(new ClientWrapper(list), new File(FILEXML));
    }

    /**
     * Method to import with JAXB from XML
     */
    public static List<Cliente> xmlToClient() throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(ClientWrapper.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        ClientWrapper clientList = (ClientWrapper) unmarshaller.unmarshal(new File(FILEXML));
        return clientList.getClientList();
    }

    /**
     * Method to export with OpenCSV to csv
     */
    public static void ingredientToCsv(List<Ingrediente> ingredientList) throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, FileNotFoundException {
        try (PrintWriter pw = new PrintWriter(FILECSV)) {
            StatefulBeanToCsv<Ingrediente> beanToCsv = new StatefulBeanToCsvBuilder<Ingrediente>(pw).withSeparator(';').build();
            beanToCsv.write(ingredientList);
        }
    }

    /**
     * Method to import with OpenCSV from CSV
     */
    public static List<Ingrediente> csvToIngredients() throws IOException {
        List<Ingrediente> listaIngredientes;

        try (FileReader fileReader = new FileReader(FILECSV)) {
            CsvToBean<Ingrediente> csvToBean = new
                    CsvToBeanBuilder<Ingrediente>(fileReader).withType(Ingrediente.class).withSeparator(';').build();
            listaIngredientes = csvToBean.parse();
        }
        return listaIngredientes;
    }
}

