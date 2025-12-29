package org.example;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.example.D.DESSERT;
import static org.example.I.VEGETABLE;

public class Main {
    public static void main(String[] args) {
        DBConnection db = new DBConnection();
        try(   Connection c = db.getDBConnection();){
            System.out.println("connxion reussie");

            DataRetriever dt = new DataRetriever(c);

            Dish d = dt.findDishById(4);
            System.out.println(d);

            List<Ingredient> i = dt.findIngredients(2,3);
            System.out.println(i);

            List<Dish> D = dt.findDishsByIngredientName("Laitue");
            System.out.println(D);










        }catch (SQLException e){
            System.out.println("connexion echoué");
            throw new RuntimeException(e);
        }

    }
}
