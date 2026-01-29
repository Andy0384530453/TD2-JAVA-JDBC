package org.example;

import java.util.Objects;

public class DishOrder {




    private int id;
    private Dish dish;
    private Order order;
    private Integer quantity;



    public DishOrder(int id, Dish dish, Order order, Integer quantity) {
        this.id = id;
        this.dish = dish;
        this.order = order;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public DishOrder setId(int id) {
        this.id = id;
        return this;
    }

    public Dish getDish() {
        return dish;
    }

    public DishOrder setDish(Dish dish) {
        this.dish = dish;
        return this;
    }

    public Order getOrder() {
        return order;
    }

    public DishOrder setOrder(Order order) {
        this.order = order;
        return this;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public DishOrder setQuantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DishOrder dishOrder = (DishOrder) o;
        return id == dishOrder.id && Objects.equals(dish, dishOrder.dish) && Objects.equals(order, dishOrder.order) && Objects.equals(quantity, dishOrder.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dish, order, quantity);
    }


    @Override
    public String toString() {
        return "DishOrder{" +
                "id=" + id +
                ", dish=" + dish +
                ", order=" + order +
                ", quantity=" + quantity +
                '}';
    }

}
