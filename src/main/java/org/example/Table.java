package org.example;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Table {


    private int id;
    private int numeroTable;
    private List<Order> orders;


    public Table(int id, int numeroTable, List<Order> orders) {
        this.id = id;
        this.numeroTable = numeroTable;
        this.orders = orders;
    }


    public int getId() {
        return id;
    }

    public Table setId(int id) {
        this.id = id;
        return this;
    }

    public int getNumeroTable() {
        return numeroTable;
    }

    public Table setNumeroTable(int numeroTable) {
        this.numeroTable = numeroTable;
        return this;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public Table setOrders(List<Order> orders) {
        this.orders = orders;
        return this;
    }




    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Table table = (Table) o;
        return id == table.id && numeroTable == table.numeroTable && Objects.equals(orders, table.orders);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, numeroTable, orders);
    }



    @Override
    public String toString() {
        return "Table{" +
                "id=" + id +
                ", numeroTable=" + numeroTable +
                ", orders=" + orders +
                '}';
    }






}
