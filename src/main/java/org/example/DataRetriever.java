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

        String requete1 = "SELECT Dish.id_dish AS Dish_id, Dish.name AS Dish_name, Dish.dish_type AS Dish_type, Dish.selling_price AS price, " +
                   "Ingredient.id_ingredient AS Ingredient_id, Ingredient.name AS Ingredient_name, Ingredient.price AS price_Ingredient," +
                   "DishIngredient.id_dishIngredient AS id_dishIngredient," +
                   "DishIngredient.quantity_required AS Q," +
                   "DishIngredient.unit AS UNIT " +
                   "FROM Dish JOIN DishIngredient " +
                   "ON Dish.id_dish = DishIngredient.id_dish " +
                   "JOIN Ingredient " +
                   "ON Ingredient.id_ingredient = DishIngredient.id_ingredient " +
                   "WHERE Dish.id_dish = ?";


           try (PreparedStatement pstmt = c.prepareStatement(requete1)) {
               List<DishIngredient> di = new ArrayList<>();
               Dish d = null;

               if (id != null) {
                   pstmt.setInt(1, id);
               }

                try(ResultSet rs = pstmt.executeQuery()){
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
                                    new Ingredient(id_Ingredient, name_Ingredient, price, null, null);
                            DishIngredient dii = new DishIngredient(id_dishIngredient, quantityRequired, u, d, i1);

                            di.add(dii);

                        }
                    }
                    if (d == null) {
                        throw new RuntimeException("introuvé");
                    }
                }

                return d;
           }
    }
    public List<Ingredient> findIngredients(int page, int size) throws SQLException {
        List<Ingredient> i = new ArrayList<>();
        int offset = (page - 1 ) * size ;


       String request1 = ("SELECT Ingredient.id_ingredient AS Ingredient_id,Ingredient.name AS Ingredient_name,Ingredient.price AS Ingredient_price,Ingredient.category AS Cat " +
                "FROM Ingredient " +
                "LIMIT ? OFFSET ? ");
        try(PreparedStatement pstmt = c.prepareStatement(request1)){
            pstmt.setInt(1,size);
            pstmt.setInt(2,offset);
            try ( ResultSet rs = pstmt.executeQuery()){
                while(rs.next()){
                    int id = rs.getInt("Ingredient_id");
                    String name = rs.getString("Ingredient_name");
                    double price = rs.getDouble("Ingredient_price");
                    I type = I.valueOf(rs.getString("Cat"));

                    Ingredient ing = new Ingredient(id,name,price,type,null);
                    i.add(ing);
                }

            }

        }
        return i;

    }
    public List<Ingredient> createIngredients(List<Ingredient> newIngredients) throws SQLException {

            String requete = "SELECT Ingredient.name  AS name , Ingredient.category FROM Ingredient " +
                    "WHERE Ingredient.name = ? AND Ingredient.category::text = ?";

            try(PreparedStatement pstmt = c.prepareStatement(requete)) {
                for (Ingredient in : newIngredients) {
                    String name = in.getName();
                    I cat = in.getIngredientType();
                    String catN = cat.name();

                    pstmt.setString(1, name);
                    pstmt.setString(2, catN);


                    try (ResultSet sr = pstmt.executeQuery()) {
                        if (sr.next()) {
                            throw new RuntimeException("deja existente dans la base " + name);
                        }
                    }
                }


                String insertSQL = "INSERT INTO Ingredient(id_ingredient, name, price, category, dish_id) " +
                        "VALUES (?, ?, ?, ?::C, ?)";
                try (PreparedStatement insertPstmt = c.prepareStatement(insertSQL)) {
                    for (Ingredient in : newIngredients) {
                        insertPstmt.setInt(1, in.getId());
                        insertPstmt.setString(2, in.getName());
                        insertPstmt.setDouble(3, in.getPrice());
                        insertPstmt.setString(4, in.getIngredientType().name());


                        insertPstmt.executeUpdate();
                    }
                }
                return newIngredients;
            }
    }


    public Dish saveDish(Dish dishToSave) throws SQLException{

        String requete = "SELECT Dish.name AS name , Dish.dish_type AS type FROM Dish " +
                    "WHERE Dish.name = ? AND Dish.id_dish = ?";

            try(PreparedStatement  pstmt = c.prepareStatement(requete)) {

                pstmt.setString(1,dishToSave.getName());
                pstmt.setInt(2,dishToSave.getId());

                try(ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()){
                        String UpdateRq = "UPDATE Dish SET name = ?, dish_type = ?::D, price = ? WHERE id_dish = ?";



                        try(PreparedStatement UpdatePstmt = c.prepareStatement(UpdateRq)){
                            UpdatePstmt.setString(1,dishToSave.getName());
                            UpdatePstmt.setString(2,dishToSave.getDishType().name());
                            UpdatePstmt.setObject(3, dishToSave.getPrice());
                            UpdatePstmt.setInt(4,dishToSave.getId());
                            int sr = UpdatePstmt.executeUpdate();
                            System.out.println(sr + "ligne mis à jour");

                        }

                    }else {
                        String InsertRq = "INSERT INTO Dish (id_dish,name,dish_type) VALUES (?,?,?::D) ";

                        try(PreparedStatement InsertPstmt = c.prepareStatement(InsertRq)){
                            InsertPstmt.setInt(1,dishToSave.getId());
                            InsertPstmt.setString(2,dishToSave.getName());
                            InsertPstmt.setString(3,dishToSave.getDishType().name());

                            int InsertSr = InsertPstmt.executeUpdate();

                            System.out.println(InsertSr + "ligne inséré(s)");

                        }
                    }
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

        try(PreparedStatement pstmt = c.prepareStatement(rq)){
            pstmt.setString(1, ingredientName);
                try(ResultSet rs = pstmt.executeQuery()){
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

                }
        }
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

        try(PreparedStatement pstmt = c.prepareStatement(rq)){
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

            try(ResultSet rs = pstmt.executeQuery()){
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

            }
        }

        return ingredients;
    }

   public  Ingredient saveIngredient(Ingredient toSave) throws SQLException {

        String request1 = "SELECT Ingredient.id_ingredient AS id , Ingredient.name AS name FROM Ingredient WHERE Ingredient.id_ingredient = ? AND Ingredient.name = ? ";
        try(PreparedStatement pstmt = c.prepareStatement(request1)){
            pstmt.setInt(1, toSave.getId());
            pstmt.setString(2,toSave.getName());

            try(ResultSet rs = pstmt.executeQuery()){
                if(rs.next()){
                    String request2 = "UPDATE Ingredient SET Ingredient.name = ? ,Ingredient.price = ? , Ingredient.category::C = ? WHERE Ingredient.id_ingredient = ?";
                    try(PreparedStatement pstmt2 = c.prepareStatement(request2)){
                        pstmt2.setString(1,toSave.getName());
                        pstmt2.setDouble(2,toSave.getPrice());
                        pstmt2.setObject(3,toSave.getIngredientType());
                        pstmt2.setInt(4,toSave.getId());

                        int lg = pstmt2.executeUpdate();

                        System.out.println(lg + "ligne modifiée(s) ");
                    }

                }else{
                    String request3 = "INSERT INTO Ingredient (id_ingredient,name,price,category) VALUES (?,?,?,?::I)";
                    try(PreparedStatement pstmt3 = c.prepareStatement(request3)){
                        pstmt3.setInt(1,toSave.getId());
                        pstmt3.setString(2,toSave.getName());
                        pstmt3.setDouble(3,toSave.getPrice());
                        pstmt3.setObject(4,toSave.getIngredientType());

                        int lgInsert = pstmt3.executeUpdate();

                        System.out.println(lgInsert + "ligne inséré (s)");

                    }
                }
            }
        }

        return toSave;
    }


    public Ingredient SaveStok(Ingredient toSave) throws SQLException {


        for(StockMovement sm  : toSave.getStoque()){
            String request = "INSERT INTO StockMovement(id_stockMovement,id_ingredient,quantity,type,unit,creation_datetime) " +
                    "VALUES (?,?,?,?::mouvement_type,?::unit_type,?) ON CONFLICT (id_stockMovement) DO NOTHING ";

            try(PreparedStatement pstmt =  c.prepareStatement(request)){
                pstmt.setInt(1,sm.getId());
                pstmt.setInt(2,toSave.getId());
                pstmt.setDouble(3,sm.getValue().getQuantity());
                pstmt.setObject(4,sm.getType().name());
                pstmt.setObject(5,sm.getValue().getUnit().name());
                pstmt.setObject(6,sm.getCreationDateTime());

                int lg  = pstmt.executeUpdate();
                System.out.println(lg + "insert in stokMovement ");

            }

        }
        return toSave;
    }
    public List<StockMovement> findAllStockMovements() throws SQLException {
        List<StockMovement> movements = new ArrayList<>();

        String sql = " SELECT sm.id_stockmovement,sm.id_ingredient,sm.quantity,sm.type,sm.unit,sm.creation_datetime,i.name " +
                "FROM StockMovement sm " +
                "JOIN Ingredient i " +
                "ON sm.id_ingredient = i.id_ingredient ";

        try(PreparedStatement ps = c.prepareStatement(sql)){
            try(ResultSet rs = ps.executeQuery()){
                while (rs.next()) {
                    int idMovement = rs.getInt("id_stockmovement");
                    int idIngredient = rs.getInt("id_ingredient");
                    double quantity = rs.getDouble("quantity");

                    MovementTypeEnum type = MovementTypeEnum.valueOf(rs.getString("type"));
                    Unit unit = Unit.valueOf(rs.getString("unit"));

                    LocalDateTime date = rs.getObject("creation_datetime", LocalDateTime.class);

                    Ingredient ingredient = new Ingredient(idIngredient, rs.getString("name"), 0, null, null);

                    stockValue value = new stockValue(quantity, unit);

                    StockMovement sm = new StockMovement(idMovement,value,type,date,ingredient);

                    movements.add(sm);
                }

            }

        }

        return movements;
    }

    public double getStockValueAt(int idIngredient, LocalDateTime t, List<StockMovement> movements) {
        double stock = 0.0;

        for (StockMovement m : movements) {
            if (m.getIngredient().getId() == idIngredient && !m.getCreationDateTime().isAfter(t)) {
                if (m.getType() == MovementTypeEnum.IN) {
                    stock += m.getValue().getQuantity();
                } else if (m.getType() == MovementTypeEnum.OUT) {
                    stock -= m.getValue().getQuantity();
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


    public Order saveOrder(Order orderToSave, List<StockMovement> allMovements, List<Table> allTables) throws SQLException {

        for (DishOrder dishOrder : orderToSave.getDishOrders()) {
            Dish dish = dishOrder.getDish();
            int quantityOrdered = dishOrder.getQuantity();

            for (DishIngredient di : dish.getDishIngredient()) {
                Ingredient ingredient = di.getIngredient();
                double requiredTotal = di.getRequiredQuantity() * quantityOrdered;
                double stockDisponible = getCurrentStock(ingredient.getId(), allMovements);



                if (stockDisponible < requiredTotal) {
                    throw new RuntimeException("Ingrédient insuffisant : " + ingredient.getName());


                }
            }
        }

        Table tableDemandee = orderToSave.getTable();
        boolean tableOccupee = false;

        if (tableDemandee.getOrders() != null) {
            for (Order o : tableDemandee.getOrders()) {
                      if (orderToSave.getHeureEntrer().isBefore(o.getHeureSortie()) && orderToSave.getHeureSortie().isAfter(o.getHeureEntrer())) {
                         tableOccupee = true;
                    break;
                }
            }
        }

        if (tableOccupee) {
            List<Integer> tablesLibres = new ArrayList<>();
            for (Table t : allTables) {
                boolean libre = true;
                if (t.getOrders() != null) {
                    for (Order o : t.getOrders()) {
                        if (orderToSave.getHeureEntrer().isBefore(o.getHeureSortie()) && orderToSave.getHeureSortie().isAfter(o.getHeureEntrer())) {
                            libre = false;
                            break;
                        }
                    }
                }
                if (libre){
                    tablesLibres.add(t.getNumeroTable());
                }
            }

            if (tablesLibres.isEmpty()) {
                throw new RuntimeException("La table " + tableDemandee.getNumeroTable() + " est occupée. Aucune table libre disponible.");
            } else {
                throw new RuntimeException("La table " + tableDemandee.getNumeroTable() + " est occupée. Tables libres : " + tablesLibres);
            }
        }

        String insertOrder = "INSERT INTO \"Order\" (reference, creation_datetime, id_table, heure_entrer, heure_sortie) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING id_order";
        try(PreparedStatement pstmtOrder = c.prepareStatement(insertOrder)){
            pstmtOrder.setString(1, orderToSave.getReference());
            pstmtOrder.setObject(2, orderToSave.getCreationDateTime());
            pstmtOrder.setInt(3, tableDemandee.getId());
            pstmtOrder.setObject(4, orderToSave.getHeureEntrer());
            pstmtOrder.setObject(5, orderToSave.getHeureSortie());

            try(ResultSet rs = pstmtOrder.executeQuery()){
                if (rs.next()) {
                    int generatedId = rs.getInt(1);
                    orderToSave.setId(generatedId);
                }

            }

        }
            System.out.println("Commande enregistrée : " + orderToSave.getReference());

        return orderToSave;
    }






    public Order findOrderByReference(String reference) throws SQLException {
        Order order = null;

        String request = "SELECT o.id_order, o.reference, o.creation_datetime, o.id_table, o.heure_entrer, o.heure_sortie, " +
                "t.table_numero " +
                "FROM \"Order\" o " +
                "JOIN \"table\" t ON o.id_table = t.id_space " +
                "WHERE o.reference = ?";

        try(PreparedStatement preparedStatement = c.prepareStatement(request)){
            preparedStatement.setString(1, reference);

            try(ResultSet rs = preparedStatement.executeQuery()){
                if (rs.next()) {
                    int idOrder = rs.getInt("id_order");
                    String ref = rs.getString("reference");
                    LocalDateTime creation = (LocalDateTime) rs.getObject("creation_datetime");
                    LocalDateTime heureEntrer = (LocalDateTime) rs.getObject("heure_entrer");
                    LocalDateTime heureSortie = (LocalDateTime) rs.getObject("heure_sortie");
                    int idTable = rs.getInt("id_table");
                    int numeroTable = rs.getInt("table_numero");

                    Table table = new Table(idTable, numeroTable, null);
                    order = new Order(idOrder, ref, creation, null, table, heureEntrer, heureSortie);
                } else {
                    throw new RuntimeException("Commande introuvable : " + reference);
                }

            }
        }


        return order;
    }
    public stockValue getStockValueAt(LocalDateTime t, Integer ingredientIdentifier) throws SQLException {

        String sql = """
            SELECT unit,
               SUM(
                    CASE
                        WHEN type = 'OUT' THEN quantity * -1
                        ELSE quantity
                    END
               ) AS actual_quantity
        FROM StockMovement
        WHERE id_ingredient = ?
        AND creation_datetime <= ?
        GROUP BY unit
    """;

        try(PreparedStatement ps = c.prepareStatement(sql)){
            ps.setInt(1, ingredientIdentifier);
            ps.setObject(2, t);

            try(ResultSet rs = ps.executeQuery()){
                if (rs.next()) {
                    double quantity = rs.getDouble("actual_quantity");
                    Unit unit = Unit.valueOf(rs.getString("unit"));

                    return new stockValue(quantity, unit);
                }

                return new stockValue(0, Unit.KG);


            }


        }


    }

    public Double getDishCost(Integer dishId) throws SQLException {

        String sql = """
        SELECT SUM(i.price * di.quantity_required) AS cost
        FROM DishIngredient di
        JOIN Ingredient i ON di.id_ingredient = i.id_ingredient
        WHERE di.id_dish = ?
    """;

        try(PreparedStatement ps = c.prepareStatement(sql)){
            ps.setInt(1, dishId);

            try(ResultSet rs = ps.executeQuery()){
                if (rs.next()) {
                    return rs.getDouble("cost");
                }


            }

        }

        return 0.0;
    }

    public void getStockStatistics(String period, LocalDateTime min, LocalDateTime max) throws SQLException {

        String sql = """
        SELECT sm.id_ingredient,
               i.name,
               DATE_TRUNC(?, sm.creation_datetime) AS period,
               SUM(
                    CASE
                        WHEN sm.type='OUT' THEN sm.quantity*-1
                        ELSE sm.quantity
                    END
               ) AS stock
        FROM StockMovement sm
        JOIN Ingredient i ON sm.id_ingredient = i.id_ingredient
        WHERE sm.creation_datetime BETWEEN ? AND ?
        GROUP BY sm.id_ingredient, i.name, period
        ORDER BY period
    """;

        try(PreparedStatement ps = c.prepareStatement(sql)){
            ps.setString(1, period);
            ps.setObject(2, min);
            ps.setObject(3, max);

            try(ResultSet rs = ps.executeQuery()){
                while (rs.next()) {
                    System.out.println(rs.getString("name " )  +
                                    rs.getObject(" period ")  +
                                    rs.getDouble("stock")
                    );
                }

            }


        }





    }








}





