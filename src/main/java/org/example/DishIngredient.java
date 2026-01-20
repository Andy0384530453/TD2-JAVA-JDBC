package org.example;

import java.util.Objects;

public class DishIngredient {




    private int id;
    private double requiredQuantity;
    private Unit unitType;
    private Dish dish;
    private Ingredient ingredient;

    public DishIngredient(int id, double requiredQuantity, Unit unitType, Dish dish, Ingredient ingredient) {
        this.id = id;
        this.requiredQuantity = requiredQuantity;
        this.unitType = unitType;
        this.dish = dish;
        this.ingredient = ingredient;
    }


    public int getId() {
        return id;
    }

    public DishIngredient setId(int id) {
        this.id = id;
        return this;
    }

    public double getRequiredQuantity() {
        return requiredQuantity;
    }

    public DishIngredient setRequiredQuantity(double requiredQuantity) {
        this.requiredQuantity = requiredQuantity;
        return this;
    }

    public Unit getUnitType() {
        return unitType;
    }

    public DishIngredient setUnitType(Unit unitType) {
        this.unitType = unitType;
        return this;
    }

    public Dish getDish() {
        return dish;
    }

    public DishIngredient setDish(Dish dish) {
        this.dish = dish;
        return this;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public DishIngredient setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
        return this;
    }
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DishIngredient that = (DishIngredient) o;
        return id == that.id && Double.compare(requiredQuantity, that.requiredQuantity) == 0 && unitType == that.unitType && Objects.equals(dish, that.dish) && Objects.equals(ingredient, that.ingredient);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, requiredQuantity, unitType, dish, ingredient);
    }

    @Override
    public String toString() {
        return "DishIngredient{" +
                "id=" + id +
                ", requiredQuantity=" + requiredQuantity +
                ", unitType=" + unitType +
                ", ingredient=" + ingredient.getName() +
                '}';
    }




}
