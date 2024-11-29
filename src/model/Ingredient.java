package model;

import com.opencsv.bean.CsvBindAndSplitByName;
import com.opencsv.bean.CsvBindByName;

import java.util.List;

public class Ingredient {
    @CsvBindAndSplitByName(writeDelimiter = ",", elementType = String.class)
    private List<String> alergens;

    @CsvBindByName
    private int id;

    @CsvBindByName
    private String name;

    public Ingredient(int id, String name, List<String> alergens) {
        this.id = id;
        this.name = name;
        this.alergens = alergens;
    }

    public Ingredient(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Ingredient() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getAlergens() {
        return alergens;
    }

    public void setAlergens(List<String> alergens) {
        this.alergens = alergens;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Ingredient that)) return false;

        return getId() == that.getId() && getAlergens().equals(that.getAlergens()) && getName().equals(that.getName());
    }

    @Override
    public int hashCode() {
        int result = getAlergens().hashCode();
        result = 31 * result + getId();
        result = 31 * result + getName().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "id=" + id +
                ", name='" + name +
                ", alergens=" + alergens + '\'' +
                '}';
    }
}
