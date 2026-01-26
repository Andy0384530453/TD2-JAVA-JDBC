package org.example;

import java.util.List;
import java.util.Objects;

public class Ingredient {


    private int id;
    private String name;
    private double price;
    private I ingredientType;
    private List<StockMovement> stoque;


    public Ingredient(int id, String name, double price, I ingredientType, List<StockMovement> stoque) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.ingredientType = ingredientType;
        this.stoque = stoque;
    }

    public int getId() {
        return id;
    }

    public Ingredient setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Ingredient setName(String name) {
        this.name = name;
        return this;
    }

    public double getPrice() {
        return price;
    }

    public Ingredient setPrice(double price) {
        this.price = price;
        return this;
    }

    public I getIngredientType() {
        return ingredientType;
    }

    public Ingredient setIngredientType(I ingredientType) {
        this.ingredientType = ingredientType;
        return this;
    }

    public List<StockMovement> getStoque() {
        return stoque;
    }

    public Ingredient setStoque(List<StockMovement> stoque) {
        this.stoque = stoque;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return id == that.id && Double.compare(price, that.price) == 0 && Objects.equals(name, that.name) && ingredientType == that.ingredientType && Objects.equals(stoque, that.stoque);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, ingredientType, stoque);
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", ingredientType=" + ingredientType +
                ", stoque=" + stoque +
                '}';
    }
}
