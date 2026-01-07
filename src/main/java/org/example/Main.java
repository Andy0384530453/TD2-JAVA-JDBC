package org.example;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.example.D.DESSERT;
import static org.example.D.START;
import static org.example.I.*;

public class Main {
    public static void main(String[] args) {
        DBConnection db = new DBConnection();
        try(   Connection c = db.getDBConnection();){
            System.out.println("connxion reussie");

            DataRetriever dt = new DataRetriever(c);
            Dish d = dt.findDishById(4);
            System.out.println(d);


            Ingredient laitue = new Ingredient(1, "Laitue", 800, VEGETABLE, null);
            Ingredient tomate = new Ingredient(2, "Tomate", 600, VEGETABLE, null);
            Ingredient chocolat = new Ingredient(4, "Chocolat", 3000, OTHER, null);
            Ingredient beurre = new Ingredient(5, "Beurre", 2500, DAIRY, null);
            Ingredient pistache = new Ingredient(6, "Pistache", 12, VEGETABLE, null);


            List<Ingredient> saladeIngredients = new ArrayList<>();
            saladeIngredients.add(laitue);
            saladeIngredients.add(tomate);

            List<Ingredient> gateauIngredients = new ArrayList<>();
            gateauIngredients.add(chocolat);
            gateauIngredients.add(beurre);
            gateauIngredients.add(pistache);


            Dish salade = new Dish(1, "Salade fraîche", saladeIngredients, START, 2000.0);
            Dish gateau = new Dish(4, "Gâteau au chocolat", gateauIngredients, DESSERT, 6000.0);

            System.out.println("Marge brute salade : " + salade.getGrossMargin());
            System.out.println("Marge brute gâteau : " + gateau.getGrossMargin());

            Dish plat = new Dish(7, "Pizza", null, D.DESSERT, 12.0);
            Dish saveddISH = dt.saveDish(plat);
            System.out.println("plat sauvegardé ou ajouté " + saveddISH);
















        }catch (SQLException e){
            System.out.println("connexion echoué");
            throw new RuntimeException(e);
        }

    }
}
