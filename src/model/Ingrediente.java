package model;

import com.opencsv.bean.CsvBindAndSplitByName;
import com.opencsv.bean.CsvBindByName;

import java.util.List;

public class Ingrediente {
    @CsvBindAndSplitByName(writeDelimiter = ",", elementType = String.class)
    private List<String> alergenos;

    @CsvBindByName
    private int id;

    @CsvBindByName
    private String nombre;

    public Ingrediente(int id, String nombre, List<String> alergenos) {
        this.id = id;
        this.nombre = nombre;
        this.alergenos = alergenos;
    }

    public Ingrediente() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<String> getAlergenos() {
        return alergenos;
    }

    public void setAlergenos(List<String> alergenos) {
        this.alergenos = alergenos;
    }

    @Override
    public String toString() {
        return "Ingrediente{" +
                "id=" + id +
                ", nombre='" + nombre +
                ", alergenos=" + alergenos + '\'' +
                '}';
    }
}
