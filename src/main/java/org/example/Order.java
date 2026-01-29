package org.example;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;


public class Order {

    private int id;
    private String reference;
    private LocalDateTime creationDateTime;
    private List<DishOrder> dishOrders;

    public Order(int id, String reference, LocalDateTime creationDateTime, List<DishOrder> dishOrders) {
        this.id = id;
        this.reference = reference;
        this.creationDateTime = creationDateTime;
        this.dishOrders = dishOrders;
    }


    public int getId() {
        return id;
    }

    public Order setId(int id) {
        this.id = id;
        return this;
    }

    public String getReference() {
        return reference;
    }

    public Order setReference(String reference) {
        this.reference = reference;
        return this;
    }

    public LocalDateTime getCreationDateTime() {
        return creationDateTime;
    }

    public Order setCreationDateTime(LocalDateTime creationDateTime) {
        this.creationDateTime = creationDateTime;
        return this;
    }

    public List<DishOrder> getDishOrders() {
        return dishOrders;
    }

    public Order setDishOrders(List<DishOrder> dishOrders) {
        this.dishOrders = dishOrders;
        return this;
    }
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id && Objects.equals(reference, order.reference) && Objects.equals(creationDateTime, order.creationDateTime) && Objects.equals(dishOrders, order.dishOrders);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, reference, creationDateTime, dishOrders);
    }
    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", reference='" + reference + '\'' +
                ", creationDateTime=" + creationDateTime +
                ", dishOrders=" + dishOrders +
                '}';
    }

   public  double getTotalAmountwithoutVAT(List<DishOrder> dishOrders){
        double HT = 0.0;
        for (DishOrder dish : dishOrders){
            HT  = HT +  dish.getDish().getPrice() * dish.getQuantity();


       }

        return  HT;


   }
    public double getTotalAmountWithVAT(double tvaRate,List<DishOrder> dishOrders) {
        return getTotalAmountwithoutVAT(dishOrders) * (1 + tvaRate);
    }

}
