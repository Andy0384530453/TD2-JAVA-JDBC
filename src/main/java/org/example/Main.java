package org.example;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        DBConnection db = new DBConnection();
        try(   Connection c = db.getDBConnection();){
            System.out.println("connxion reussie");

            DataRetriever dt = new DataRetriever(c);
            Dish d = dt.findDishById(0);
            List<Ingredient> i = dt.findIngredients(1,3);
            System.out.println(d);
            System.out.println(i);



        }catch (SQLException e){
            System.out.println("connexion echoué");
            throw new RuntimeException(e);
        }

    }
}
