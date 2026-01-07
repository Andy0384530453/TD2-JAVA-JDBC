package org.example;

import java.util.List;
import java.util.Objects;

public class Dish {


    private int id;
    private String name;
    private D dishType;
    private List<Ingredient> ingredients;
    private Double price;

    public Dish(int id, String name, List<Ingredient> ingredients, D dishType,Double price) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.dishType = dishType;
        this.price = price;
    }


    public Dish setPrice(Double price) {
        this.price = price;
        return this;
    }

    public double getPrice() {
        return price;
    }
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public D getDishType() {
        return dishType;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }


    public Dish setId(int id) {
        this.id = id;
        return this;
    }

    public Dish setName(String name) {
        this.name = name;
        return this;
    }

    public Dish setDishType(D dishType) {
        this.dishType = dishType;
        return this;
    }

    public Dish setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Dish dish = (Dish) o;
        return id == dish.id && Objects.equals(name, dish.name) && dishType == dish.dishType && Objects.equals(ingredients, dish.ingredients);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, dishType, ingredients);
    }



    @Override
    public String toString() {
        return "Dish{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", dishType=" + dishType +
                ", ingredients=" + ingredients +
                '}';
    }


    public double getDishCost() {
        double total = 0.0;
        if (ingredients != null) {
            for (Ingredient i : ingredients) {
                total += i.getPrice();
            }
        }
        return total;
    }


    public Double getGrossMargin() {
        if (price == null) {
            throw new IllegalStateException(
                    "Impossible de calculer la marge : le prix de vente n'a pas encore de valeur."
            );
        }
        return price - getDishCost();
    }
}
