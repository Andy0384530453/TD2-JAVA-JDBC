package org.example;

import java.time.LocalDateTime;
import java.util.Objects;

public class StockMovement {




    private int id;
    private stockValue value;
    private MovementTypeEnum type;
    private LocalDateTime creationDateTime;
    private Ingredient ingredient;


    public StockMovement(int id, stockValue value, MovementTypeEnum type, LocalDateTime creationDateTime, Ingredient ingredient) {
        this.id = id;
        this.value = value;
        this.type = type;
        this.creationDateTime = creationDateTime;
        this.ingredient = ingredient;
    }

    public stockValue getValue() {
        return value;
    }

    public StockMovement setValue(stockValue value) {
        this.value = value;
        return this;
    }

    public int getId() {
        return id;
    }

    public StockMovement setId(int id) {
        this.id = id;
        return this;
    }

    public MovementTypeEnum getType() {
        return type;
    }

    public StockMovement setType(MovementTypeEnum type) {
        this.type = type;
        return this;
    }

    public LocalDateTime getCreationDateTime() {
        return creationDateTime;
    }

    public StockMovement setCreationDateTime(LocalDateTime creationDateTime) {
        this.creationDateTime = creationDateTime;
        return this;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public StockMovement setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
        return this;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        StockMovement that = (StockMovement) o;
        return id == that.id && Objects.equals(value, that.value) && type == that.type && Objects.equals(creationDateTime, that.creationDateTime) && Objects.equals(ingredient, that.ingredient);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, value, type, creationDateTime, ingredient);
    }


    @Override
    public String toString() {
        return "StockMovement{" +
                "id=" + id +
                ", value=" + value +
                ", type=" + type +
                ", creationDateTime=" + creationDateTime +
                ", ingredient=" + ingredient +
                '}';
    }



}
