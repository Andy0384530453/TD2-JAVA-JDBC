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

            Dish d = dt.findDishById(0);
            List<Ingredient> i = dt.findIngredients(1,3);

            List<Ingredient> insert = new ArrayList<>();
            Dish di = new Dish(4,"Glace pistache",null,DESSERT);
            Ingredient ingredient = new Ingredient(6,"Pistache",12.0,VEGETABLE,di);
            insert.add(ingredient);

            dt.createIngredients(insert);

            System.out.println(d);
            System.out.println(i);



        }catch (SQLException e){
            System.out.println("connexion echoué");
            throw new RuntimeException(e);
        }

    }
}
