package org.example;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever {
    Connection c;


    public DataRetriever(Connection c) {
        this.c = c;

    }


    public Dish findDishById(Integer id) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
       try {
           String requete1 = "SELECT Dish.id_dish AS Dish_id, Dish.name AS Dish_name, Dish.dish_type AS Dish_type, Dish.selling_price AS price, " +
                   "Ingredient.id_ingredient AS Ingredient_id, Ingredient.name AS Ingredient_name, Ingredient.price AS price_Ingredient," +
                   "DishIngredient.id_dishIngredient AS id_dishIngredient," +
                   "DishIngredient.quantity_required AS Q," +
                   "DishIngredient.unit AS UNIT " +
                   "FROM Dish JOIN DishIngredient " +
                   "ON Dish.id_dish = DishIngredient.id_dish " +
                   "JOIN Ingredient " +
                   "ON Ingredient.id_ingredient = DishIngredient.id_ingredient " +
                   "WHERE 1=1 ";


           if (id != null) {
               requete1 += " AND Dish.id_dish = ? ";
           }
            pstmt = c.prepareStatement(requete1);
           List<DishIngredient> di = new ArrayList<>();
           Dish d = null;

           if (id != null) {
               pstmt.setInt(1, id);
           }

          rs = pstmt.executeQuery();

           while (rs.next()) {
               if (d == null) {
                   int id_Dish = rs.getInt("Dish_id");
                   String name = rs.getString("Dish_name");
                   D dishType = D.valueOf(rs.getString("Dish_type"));
                   double dishPrice = rs.getDouble("price");

                   d = new Dish(id_Dish, name, dishType, di, dishPrice);
               }

               int id_Ingredient = rs.getInt("Ingredient_id");

               if (id_Ingredient != 0) {
                   String name_Ingredient = rs.getString("Ingredient_name");
                   double price = rs.getDouble("price_Ingredient");


                   int id_dishIngredient = rs.getInt("id_dishIngredient");
                   double quantityRequired = rs.getDouble("Q");
                   Unit u = Unit.valueOf(rs.getString("unit"));

                   Ingredient i1 =
                           new Ingredient(id_Ingredient, name_Ingredient, price,null,null);
                   DishIngredient dii = new DishIngredient(id_dishIngredient, quantityRequired, u, d, i1);

                    di.add(dii);

               }
           }
           if (di.isEmpty()){
               throw new  RuntimeException("introuvé");
           }



           return d;
       } catch (SQLException e) {
           throw new RuntimeException(e);
       }finally {
           if (pstmt != null){
               pstmt.close();
           } else if (rs != null) {
               rs.close();

           }

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

            Ingredient ing = new Ingredient(id,name,price,type,null);
            i.add(ing);
        }

        return i;

    }
    public List<Ingredient> createIngredients(List<Ingredient> newIngredients) throws SQLException {
        try {
            String requete = "SELECT Ingredient.name  AS name , Ingredient.category FROM Ingredient " +
                    "WHERE Ingredient.name = ? AND Ingredient.category::text = ?";

            PreparedStatement pstmt = c.prepareStatement(requete);
            for (Ingredient in : newIngredients) {
                String name = in.getName();
                I cat = in.getIngredientType();
                String catN = cat.name();

                pstmt.setString(1, name);
                pstmt.setString(2, catN);


                ResultSet sr = pstmt.executeQuery();

                if (sr.next()) {
                    throw new RuntimeException("deja existente dans la base " + name);
                }

            }


            String insertSQL = "INSERT INTO Ingredient(id_ingredient, name, price, category, dish_id) " +
                    "VALUES (?, ?, ?, ?::C, ?)";
            PreparedStatement insertPstmt = c.prepareStatement(insertSQL);

            for (Ingredient in : newIngredients) {
                insertPstmt.setInt(1, in.getId());
                insertPstmt.setString(2, in.getName());
                insertPstmt.setDouble(3, in.getPrice());
                insertPstmt.setString(4, in.getIngredientType().name());


                insertPstmt.executeUpdate();
            }

            return newIngredients;

        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }


    public Dish saveDish(Dish dishToSave) throws SQLException{
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String requete = "SELECT Dish.name AS name , Dish.dish_type AS type FROM Dish " +
                    "WHERE Dish.name = ? AND Dish.id_dish = ?";

             pstmt = c.prepareStatement(requete);

            pstmt.setString(1,dishToSave.getName());
            pstmt.setInt(2,dishToSave.getId());

            rs = pstmt.executeQuery();

            if (rs.next()){
                String UpdateRq = "UPDATE Dish SET Dish.name = ? ,Dish.dish_type::D = ?, Dish.price = ? WHERE id_dish = ?";

                PreparedStatement UpdatePstmt = c.prepareStatement(UpdateRq);

                UpdatePstmt.setString(1,dishToSave.getName());
                UpdatePstmt.setString(2,dishToSave.getDishType().name());
                UpdatePstmt.setObject(3, dishToSave.getPrice());
                UpdatePstmt.setInt(4,dishToSave.getId());
                int sr = UpdatePstmt.executeUpdate();
                System.out.println(sr + "ligne mis à jour");

            }else {
                String InsertRq = "INSERT INTO Dish (id_dish,name,dish_type) VALUES (?,?,?::D) ";

                PreparedStatement InsertPstmt = c.prepareStatement(InsertRq);
                InsertPstmt.setInt(1,dishToSave.getId());
                InsertPstmt.setString(2,dishToSave.getName());
                InsertPstmt.setString(3,dishToSave.getDishType().name());

                int InsertSr = InsertPstmt.executeUpdate();

                System.out.println(InsertSr + "ligne inséré(s)");






            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }finally {
            try{
                if (pstmt != null ){
                    pstmt.close();
                }else if(rs != null){
                    rs.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return dishToSave;

    }

    public List<Dish> findDishesByIngredientName(String ingredientName) throws SQLException {
        List<Dish> di = new ArrayList<>();

        String rq = "SELECT d.id_dish AS Dish_id, d.name AS Dish_name, d.dish_type AS Dish_type, d.selling_price AS Dish_price, " +
                "di.id_dishingredient AS DishIngredient_id, di.quantity_required AS quantity, di.unit AS unit, " +
                "i.id_ingredient AS Ingredient_id, i.name AS Ingredient_name, i.price AS Ingredient_price " +
                "FROM Dish d " +
                "JOIN DishIngredient di ON d.id_dish = di.id_dish " +
                "JOIN Ingredient i ON di.id_ingredient = i.id_ingredient " +
                "WHERE i.name = ?";

        PreparedStatement pstmt = c.prepareStatement(rq);
        pstmt.setString(1, ingredientName);
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {

            int dishId = rs.getInt("Dish_id");
            String dishName = rs.getString("Dish_name");
            D dishType = D.valueOf(rs.getString("Dish_type"));
            double dishPrice = rs.getDouble("Dish_price");

            Dish dish = new Dish(dishId, dishName, dishType, new ArrayList<>(), dishPrice);


            int ingredientId = rs.getInt("Ingredient_id");
            String ingredientNameDb = rs.getString("Ingredient_name");
            double ingredientPrice = rs.getDouble("Ingredient_price");
            Ingredient ing = new Ingredient(ingredientId,ingredientNameDb,ingredientPrice,null,null);


            int diId = rs.getInt("DishIngredient_id");
            double quantity = rs.getDouble("quantity");
            Unit unit = Unit.valueOf(rs.getString("unit"));
            DishIngredient dishIngredient = new DishIngredient(diId, quantity, unit, dish, ing);


            dish.getDishIngredient().add(dishIngredient);


            di.add(dish);
        }


        if (di.isEmpty()) {
            throw new RuntimeException("plat introuvé  " + ingredientName);
        }

        rs.close();
        pstmt.close();

        return di;
    }



    public List<Ingredient> findIngredientsByCriteria(String ingredientName, I category, String dishName, int page, int size) throws SQLException {
        List<Ingredient> ingredients = new ArrayList<>();

        String rq = "SELECT i.id_ingredient AS ingredient_id, i.name AS ingredient_name, i.price AS ingredient_price, i.category AS category, " +
                "d.id_dish AS dish_id, d.name AS dish_name, d.selling_price AS dish_price, d.dish_type AS dish_type " +
                "FROM Ingredient i " +
                "JOIN DishIngredient di ON i.id_ingredient = di.id_ingredient " +
                "JOIN Dish d ON di.id_dish = d.id_dish " +
                "WHERE 1=1";

        if (ingredientName != null) {
            rq += " AND i.name = ?";
        }


        if (category != null) {

            rq += " AND i.category::text = ?";
        }
        if (dishName != null) {

            rq += " AND d.name = ?";
        }

        rq += " LIMIT ? OFFSET ?";

        PreparedStatement pstmt = c.prepareStatement(rq);
        int index = 1;
        if (ingredientName != null) {

            pstmt.setString(index++, ingredientName);
        }
        if (category != null) {
            pstmt.setString(index++, category.name());
        }
        if (dishName != null) {
            pstmt.setString(index++, dishName);
        }

        int offset = (page - 1) * size;
        pstmt.setInt(index++, size);
        pstmt.setInt(index, offset);

        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            int ingId = rs.getInt("ingredient_id");
            String ingName = rs.getString("ingredient_name");
            double ingPrice = rs.getDouble("ingredient_price");
            I ingCat = I.valueOf(rs.getString("category"));

            int dId = rs.getInt("dish_id");
            String dName = rs.getString("dish_name");
            double dPrice = rs.getDouble("dish_price");
            D dType = D.valueOf(rs.getString("dish_type"));

            Dish dish = new Dish(dId, dName, dType, null, dPrice); // dishIngredient peut rester null
            Ingredient ingredient = new Ingredient(ingId, ingName, ingPrice, ingCat,null);

            ingredients.add(ingredient);
        }

        rs.close();
        pstmt.close();
        return ingredients;
    }

   public  Ingredient saveIngredient(Ingredient toSave) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String request1 = "SELECT Ingredient.id_ingredient AS id , Ingredient.name AS name FROM Ingredient WHERE Ingredient.id_ingredient = ? AND Ingredient.name = ? ";
        pstmt = c.prepareStatement(request1);
        pstmt.setInt(1, toSave.getId());
        pstmt.setString(2,toSave.getName());

        rs = pstmt.executeQuery();

        if(rs.next()){
            String request2 = "UPDATE Ingredient SET Ingredient.name = ? ,Ingredient.price = ? , Ingredient.category::C = ? WHERE Ingredient.id_ingredient = ?";
            PreparedStatement pstmt2 = c.prepareStatement(request2);

            pstmt2.setString(1,toSave.getName());
            pstmt2.setDouble(2,toSave.getPrice());
            pstmt2.setObject(3,toSave.getIngredientType());
            pstmt2.setInt(4,toSave.getId());

            int lg = pstmt2.executeUpdate();

            System.out.println(lg + "ligne modifiée(s) ");

        }else{
            String request3 = "INSERT INTO Ingredient (id_ingredient,name,price,category) VALUES (?,?,?,?::I)";
            PreparedStatement pstmt3 = c.prepareStatement(request3);
            pstmt3.setInt(1,toSave.getId());
            pstmt3.setString(2,toSave.getName());
            pstmt3.setDouble(3,toSave.getPrice());
            pstmt3.setObject(4,toSave.getIngredientType());

            int lgInsert = pstmt3.executeUpdate();

            System.out.println(lgInsert + "ligne inséré (s)");


            pstmt.close();

            pstmt3.close();


            rs.close();


        }



        return toSave;



    }

    public Ingredient SaveStok(Ingredient toSave) throws SQLException {
        PreparedStatement pstmt = null;


        for(StockMovement sm  : toSave.getStoque()){
            String request = "INSERT INTO StockMovement(id_stockMovement,id_ingredient,quantity,type,unit,creation_datetime) " +
                    "VALUES (?,?,?,?::mouvement_type,?::unit_type,?) ON CONFLICT (id_stockMovement) DO NOTHING ";

           pstmt =  c.prepareStatement(request);
            pstmt.setInt(1,sm.getId());
            pstmt.setInt(2,toSave.getId());
            pstmt.setDouble(3,sm.getValue().getQuantity());
            pstmt.setObject(4,sm.getType().name());
            pstmt.setObject(5,sm.getValue().getUnit().name());
            pstmt.setObject(6,sm.getCreationDateTime());


            int lg  = pstmt.executeUpdate();


            System.out.println(lg + "insert in stokMovement ");


        }
        return toSave;
    }
    public double  getStockValueAt(int idIngredient,LocalDateTime t,List<StockMovement> movements) {
        double stock = 0.0;

        for (StockMovement m : movements) {

            if (m.getId() == idIngredient
                    && !m.getCreationDateTime().isAfter(t)) {

                if (m.getType() == MovementTypeEnum.IN) {
                    stock += m.getValue().getQuantity();
                } else {
                    stock -=  m.getValue().getQuantity();
                }
            }
        }

        return stock;
    }
    public double getCurrentStock(int idIngredient, List<StockMovement> movements) {

        double stock = 0.0;

        for (StockMovement m : movements) {
            if (m.getIngredient().getId() == idIngredient) {
                if (m.getType() == MovementTypeEnum.IN) {
                    stock += m.getValue().getQuantity();
                } else {
                    stock -= m.getValue().getQuantity();
                }
            }
        }

        return stock;
    }


    public Order saveOrder(Order orderToSave, List<StockMovement> allMovements) throws SQLException {

        for (DishOrder dishOrder : orderToSave.getDishOrders()) {
            Dish dish = dishOrder.getDish();

            int quantityOrdered = dishOrder.getQuantity();

            for (DishIngredient di : dish.getDishIngredient()) {

                Ingredient ingredient = di.getIngredient();
                double requiredTotal = di.getRequiredQuantity() * quantityOrdered;
                double stockDisponible = getCurrentStock(ingredient.getId(), allMovements);

                if (stockDisponible < requiredTotal) {
                    throw new RuntimeException("ingredient  est inssufisant :  " + ingredient.getName());
                }
            }
        }


        String insertOrder = "INSERT INTO \"Order\" (id_order, reference, creation_datetime) VALUES (?, ?, ?)";
        PreparedStatement pstmtOrder = c.prepareStatement(insertOrder);

        pstmtOrder.setInt(1, orderToSave.getId());
        pstmtOrder.setString(2, orderToSave.getReference());
        pstmtOrder.setObject(3, orderToSave.getCreationDateTime());

        int lg = pstmtOrder.executeUpdate();
        System.out.println(lg + " inséré(s) dans la table Order");


        pstmtOrder.close();


        for (DishOrder dishOrder : orderToSave.getDishOrders()) {
            Dish dish = dishOrder.getDish();
            int quantityOrdered = dishOrder.getQuantity();

            for (DishIngredient di : dish.getDishIngredient()) {
                Ingredient ingredient = di.getIngredient();
                double usedQuantity = di.getRequiredQuantity() * quantityOrdered;

                StockMovement movementOut = new StockMovement(0, new stockValue(usedQuantity, di.getUnitType()),
                        MovementTypeEnum.OUT, LocalDateTime.now(), ingredient);


                String insertStock = "INSERT INTO StockMovement (id_ingredient, quantity, type, unit, creation_datetime) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement pstmtStock = c.prepareStatement(insertStock);
                pstmtStock.setInt(1, ingredient.getId());
                pstmtStock.setDouble(2, usedQuantity);
                pstmtStock.setString(3, MovementTypeEnum.OUT.name());
                pstmtStock.setString(4, di.getUnitType().name());
                pstmtStock.setObject(5, LocalDateTime.now());

                pstmtStock.executeUpdate();
                pstmtStock.close();
            }
        }

        return orderToSave;
    }

    public  Order findOrderByReference(String reference) throws SQLException {
        Order order= null;
        String request = "SELECT \"Order\".id_order  AS id , \"Order\".reference AS reference , \"Order\".creation_datetime AS time FROM  \"Order\" ";
        PreparedStatement preparedStatement = c.prepareStatement(request);
        ResultSet rs = preparedStatement.executeQuery();

        while (rs.next()){
            String ref = rs.getString("reference");
            if (ref.equals(reference)){
                int id_order  =  rs.getInt("id");
                LocalDateTime ldt = (LocalDateTime) rs.getObject("time");

                 order = new Order(id_order,ref,ldt,null);

                break;

            }

            if (!ref.equals(reference)){
                throw new RuntimeException("commande introuvable" + reference);

            }





        }
        return order;







    }











}





