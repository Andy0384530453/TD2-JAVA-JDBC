package org.example;

import java.util.List;
import java.util.Objects;

public class Dish {




    private int id;
    private String name;
    private D dishType;
    private List<DishIngredient> dishIngredient;
    private Double price;



    public Dish(int id, String name, D dishType, List<DishIngredient> dishIngredient, Double price) {
        this.id = id;
        this.name = name;
        this.dishType = dishType;
        this.dishIngredient = dishIngredient;
        this.price = price;
    }



    public int getId() {
        return id;
    }

    public Dish setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Dish setName(String name) {
        this.name = name;
        return this;
    }

    public D getDishType() {
        return dishType;
    }

    public Dish setDishType(D dishType) {
        this.dishType = dishType;
        return this;
    }

    public List<DishIngredient> getDishIngredient() {
        return dishIngredient;
    }

    public Dish setDishIngredient(List<DishIngredient> dishIngredient) {
        this.dishIngredient = dishIngredient;
        return this;
    }

    public Double getPrice() {
        return price;
    }

    public Dish setPrice(Double price) {
        this.price = price;
        return this;
    }
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Dish dish = (Dish) o;
        return id == dish.id && Objects.equals(name, dish.name) && dishType == dish.dishType && Objects.equals(dishIngredient, dish.dishIngredient) && Objects.equals(price, dish.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, dishType, dishIngredient, price);
    }
    @Override
    public String toString() {
        return "Dish{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", dishType=" + dishType +
                ", dishIngredient=" + (dishIngredient != null ? dishIngredient.size() + " ingredient(s)" : "aucun") +
                ", price=" + price +
                '}';
    }





    public double getDishCost() {
        double total = 0.0;

        if (dishIngredient != null) {
            for (DishIngredient di : dishIngredient) {
                Ingredient ingr = di.getIngredient();
                if (ingr != null) {
                    total += ingr.getPrice() * di.getRequiredQuantity();
                }
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
