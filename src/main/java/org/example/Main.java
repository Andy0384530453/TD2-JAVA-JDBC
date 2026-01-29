package org.example;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.example.D.*;
import static org.example.I.*;

public class Main {
    public static void main(String[] args) {
        DBConnection db = new DBConnection();

        try (Connection c = db.getDBConnection()) {
            System.out.println("Connexion réussie");

            DataRetriever dt = new DataRetriever(c);



               Dish d = dt.findDishById(1);
             System.out.println(d);

             Ingredient laitue = new Ingredient(1, "Laitue", 800, VEGETABLE,null);
            Ingredient tomate = new Ingredient(2, "Tomate", 600, VEGETABLE,null);
            Ingredient chocolat = new Ingredient(4, "Chocolat", 3000, OTHER,null);
            Ingredient beurre = new Ingredient(5, "Beurre", 2500, DAIRY,null);
            Ingredient pistache = new Ingredient(6, "Pistache", 12, VEGETABLE,null);

            Dish salade = new Dish(1, "Salade fraîche", START, new ArrayList<>(), 3500.0);
            Dish gateau = new Dish(4, "Gâteau au chocolat", DESSERT, new ArrayList<>(), 8000.0);

            List<DishIngredient> saladeDishIngredients = new ArrayList<>();
            saladeDishIngredients.add(new DishIngredient(1, 0.2, Unit.KG, salade, laitue));
            saladeDishIngredients.add(new DishIngredient(2, 0.15, Unit.KG, salade, tomate));
            salade.setDishIngredient(saladeDishIngredients);

            List<DishIngredient> gateauDishIngredients = new ArrayList<>();
            gateauDishIngredients.add(new DishIngredient(4, 0.3, Unit.KG, gateau, chocolat));
            gateauDishIngredients.add(new DishIngredient(5, 0.2, Unit.KG, gateau, beurre));
            gateauDishIngredients.add(new DishIngredient(6, 0.012, Unit.KG, gateau, pistache));
            gateau.setDishIngredient(gateauDishIngredients);


            System.out.println("Marge brute salade : " + salade.getGrossMargin());
            System.out.println("Marge brute gâteau : " + gateau.getGrossMargin());


        } catch (SQLException e) {
            System.out.println("Connexion a échoue");
            throw new RuntimeException(e);
        }
    }
}
