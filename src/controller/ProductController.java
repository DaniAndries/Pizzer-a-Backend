package controller;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.opencsv.exceptions.CsvValidationException;
import model.Ingredient;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProductController {
    List<Ingredient> ingredientList = new ArrayList<>();

    public ProductController(List<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
    }

    public void openCsvExport() throws CsvRequiredFieldEmptyException, FileNotFoundException, CsvDataTypeMismatchException {
        FileManagement.ingredientToCsv(this.ingredientList);
    }

    public List<Ingredient> openCsvImport() throws IOException, CsvValidationException {
        return FileManagement.csvToIngredients();
    }
}
