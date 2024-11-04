package controller;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.opencsv.exceptions.CsvValidationException;
import model.Ingrediente;
import model.Pizza;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ControladorProducto {
    List<Ingrediente> ingredientList = new ArrayList<>();

    public ControladorProducto(List<Ingrediente> ingredientList) {
        this.ingredientList = ingredientList;
    }

    public void openCsvExport() throws CsvRequiredFieldEmptyException, FileNotFoundException, CsvDataTypeMismatchException {
        GestionFicheros.csvExport(this.ingredientList);
    }

    public List<Ingrediente> openCsvImport() throws IOException, CsvValidationException {
        return GestionFicheros.importCsv();
    }

    public List<Pizza> importPizzas() throws IOException {
        return GestionFicheros.importPizzasTxt();
    }
}
