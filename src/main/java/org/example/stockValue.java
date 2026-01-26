package org.example;

import java.util.Objects;

public class stockValue {


    private double quantity;
    private Unit unit;

    public stockValue(double quantity, Unit unit) {
        this.quantity = quantity;
        this.unit = unit;
    }

    public double getQuantity() {
        return quantity;
    }

    public stockValue setQuantity(double quantity) {
        this.quantity = quantity;
        return this;
    }

    public Unit getUnit() {
        return unit;
    }

    public stockValue setUnit(Unit unit) {
        this.unit = unit;
        return this;
    }
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        stockValue that = (stockValue) o;
        return Double.compare(quantity, that.quantity) == 0 && unit == that.unit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantity, unit);
    }
    @Override
    public String toString() {
        return "stockValue{" +
                "quantity=" + quantity +
                ", unit=" + unit +
                '}';
    }




}
