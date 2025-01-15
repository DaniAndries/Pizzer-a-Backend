package controller;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import model.Customer;
import model.Ingredient;

import javax.xml.bind.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The FileManagement class provides methods for importing and exporting
 * customer and ingredient data to and from various file formats, including
 * XML and CSV.
 *
 * @author DaniAndries
 * @version 0.1
 */
public class FileManagement {
    private static final String FILEXML = "customers.xml";  // File path for XML storage
    private static final String FILECSV = "ingredients.csv"; // File path for CSV storage

    // Private constructor to prevent instantiation
    private FileManagement() {
    }

    /**
     * Imports customer data from a specified text file. Each line in the file
     * is split using various delimiters (semicolon, comma, or pipe)
     * and parsed into Customer objects.
     *
     * @return a list of imported Customer objects
     * @throws IOException if an error occurs while reading the file
     */
    public static List<Customer> importCustomer() throws IOException {
        // Read all lines from the file into a List and convert it to a String[]
        String[] lines = Files.readAllLines(Path.of("admin.txt")).toArray(new String[0]);

        // Convert each line into a String array using split, and then add customers
        String[][] dataList = Arrays.stream(lines)
                .map(line -> line.split("[;,|]")) // Split by multiple delimiters
                .toArray(String[][]::new); // Convert to 2D array

        return addAdminCustomers(dataList);
    }

    /**
     * Adds customers from a 2D String array to a list of Customer objects.
     *
     * @param list a 2D String array containing customer data
     * @return a list of Customer objects created from the provided data
     * @throws NumberFormatException if an error occurs while parsing integers
     * @throws ArrayIndexOutOfBoundsException if the data array does not contain enough elements
     */
    private static List<Customer> addAdminCustomers(String[][] list) throws NumberFormatException, ArrayIndexOutOfBoundsException {
        List<Customer> customerList = new ArrayList<>();
        for (String[] data : list) {
            Customer customer = getCustomer(data);
            customerList.add(customer);
        }
        return customerList;
    }

    /**
     * Creates a Customer object from an array of customer data.
     *
     * @param data a String array containing the customer's data
     * @return a Customer object populated with the provided data
     */
    private static Customer getCustomer(String[] data) {
        Customer customer = new Customer(
                Integer.parseInt(data[0].trim()),  // ID
                data[3].trim(),                    // Address
                data[2].trim(),                    // Name
                data[1].trim(),                    // DNI
                data[5].trim(),                    // Email
                data[4].trim(),                    // Phone
                data[6].trim(),                    // Password
                true                               // Admin flag
        );
        customer.setAdmin(true);
        return customer;
    }

    /**
     * Exports a list of Customer objects to an XML file using JAXB.
     *
     * @param list the list of Customer objects to be exported
     * @throws JAXBException if an error occurs during the marshalling process
     */
    public static void customerToXml(List<Customer> list) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(CustomerWrapper.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(new CustomerWrapper(list), new File(FILEXML));
    }

    /**
     * Imports customer data from an XML file using JAXB.
     *
     * @return a list of Customer objects imported from the XML file
     * @throws JAXBException if an error occurs during the unmarshalling process
     */
    public static List<Customer> xmlToCustomer() throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(CustomerWrapper.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        CustomerWrapper customerList = (CustomerWrapper) unmarshaller.unmarshal(new File(FILEXML));
        return customerList.getCustomerList();
    }

    /**
     * Exports a list of Ingredient objects to a CSV file using OpenCSV.
     *
     * @param ingredientList the list of Ingredient objects to be exported
     * @throws CsvRequiredFieldEmptyException if a required field is empty
     * @throws CsvDataTypeMismatchException if a data type mismatch occurs during export
     * @throws FileNotFoundException if the output file cannot be created
     */
    public static void ingredientToCsv(List<Ingredient> ingredientList) throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, FileNotFoundException {
        try (PrintWriter pw = new PrintWriter(FILECSV)) {
            StatefulBeanToCsv<Ingredient> beanToCsv = new StatefulBeanToCsvBuilder<Ingredient>(pw).withSeparator(';').build();
            beanToCsv.write(ingredientList);
        }
    }

    /**
     * Imports a list of Ingredient objects from a CSV file using OpenCSV.
     *
     * @return a list of Ingredient objects imported from the CSV file
     * @throws IOException if an error occurs while reading the file
     */
    public static List<Ingredient> csvToIngredients() throws IOException {
        List<Ingredient> ingredientsList;

        try (FileReader fileReader = new FileReader(FILECSV)) {
            CsvToBean<Ingredient> csvToBean = new CsvToBeanBuilder<Ingredient>(fileReader)
                    .withType(Ingredient.class)
                    .withSeparator(';')
                    .build();
            ingredientsList = csvToBean.parse();
        }
        return ingredientsList;
    }
}
