package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever {
    Connection c;


    public DataRetriever(Connection c) {
        this.c = c;

    }


    public Dish findDishById(Integer id)  {
       try {
           String requete1 = "SELECT Dish.id_dish AS Dish_id, Dish.name AS Dish_name, Dish.dish_type AS Dish_type, " +
                   "Ingredient.id_ingredient AS Ingredient_id, Ingredient.name AS Ingredient_name, Ingredient.price AS price_Ingredient " +
                   "FROM Dish INNER JOIN Ingredient " +
                   "ON Dish.id_dish = Ingredient.dish_id " +
                   "WHERE 1=1 ";

           if (id != null) {
               requete1 += " AND Dish.id_dish = ? ";
           }
           PreparedStatement pstmt = c.prepareStatement(requete1);
           List<Ingredient> i = new ArrayList<>();
           Dish d = null;

           if (id != null) {
               pstmt.setInt(1, id);
           }

           ResultSet rs = pstmt.executeQuery();

           while (rs.next()) {
               int id_Dish = rs.getInt("Dish_id");
               String name = rs.getString("Dish_name");
               D dishType = D.valueOf(rs.getString("Dish_type"));

               d = new Dish(id_Dish, name, i, dishType);

               int id_Ingredient = rs.getInt("Ingredient_id");

               if (id_Ingredient != 0) {
                   String name_Ingredient = rs.getString("Ingredient_name");
                   double price = rs.getDouble("price_Ingredient");

                   Ingredient i1 =
                           new Ingredient(id_Ingredient, name_Ingredient, price, null, null);

                   i.add(i1);
               }
           }

           return d;
       } catch (SQLException e) {
           throw new RuntimeException(e);
       }


    }
    public List<Ingredient> findIngredients(int page, int size) throws SQLException {
        List<Ingredient> i = new ArrayList<>();
        int offset = (page - 1 ) * size ;

        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT Ingredient.id_ingredient AS Ingredient_id,Ingredient.name AS Ingredient_name,Ingredient.price AS Ingredient_price,Ingredient.category AS Cat " +
                "FROM Ingredient " +
                "LIMIT " + size + " OFFSET " + offset);

        while(rs.next()){
            int id = rs.getInt("Ingredient_id");
            String name = rs.getString("Ingredient_name");
            double price = rs.getDouble("Ingredient_price");
            I type = I.valueOf(rs.getString("Cat"));

            Ingredient ingr = new Ingredient(id,name,price,type,null);
            i.add(ingr);
        }

        return i;

    }
    public List<Ingredient> createIngredients(List<Ingredient> newIngredients) throws SQLException {
        String requete = "SELECT Ingredient.name  AS name , Ingredient.category FROM Ingredient " +
                "WHERE Ingredient.name = ? AND Ingredient.category::text = ?";

        PreparedStatement pstmt = c.prepareStatement(requete);
        for (Ingredient in : newIngredients){
            String name = in.getName();
            I cat = in.getIngredientType();
            String catN = cat.name();

            pstmt.setString(1,name);
            pstmt.setString(2, catN);


            ResultSet sr = pstmt.executeQuery();

            if (sr.next()){
                 throw new RuntimeException("deja existente dans la base " + cat);
             }

        }


        String insertSQL = "INSERT INTO Ingredient(id_ingredient, name, price, category, dish_id) " +
                "VALUES (?, ?, ?, ?::C, ?)";
        PreparedStatement insertPstmt = c.prepareStatement(insertSQL);

        for (Ingredient in : newIngredients){
            insertPstmt.setInt(1, in.getId());
            insertPstmt.setString(2, in.getName());
            insertPstmt.setDouble(3, in.getPrice());
            insertPstmt.setString(4, in.getIngredientType().name());
            insertPstmt.setInt(5, in.getDish().getId());

            insertPstmt.executeUpdate();
        }

        return newIngredients;





    }



}
