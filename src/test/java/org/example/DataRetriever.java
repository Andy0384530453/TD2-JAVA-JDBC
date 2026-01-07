package org.example;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

class DataRetrieverTest {

    @Test
    void testFindDishById() throws Exception {
        DBConnection db = new DBConnection();
        Connection conn = DriverManager.getConnection(
            db.getUrl(),
            db.getMdp(),
            db.getUser()
        );

        DataRetriever dt = new DataRetriever(conn);

        Dish result = dt.findDishById(1);

        assertNotNull(result);
        assertEquals("Salade Fraîche",result.getName());
        assertEquals(2, result.getIngredients().size());
        assertEquals("Laitue",result.getIngredients().get(0).getName());
        assertEquals("Tomate",result.getIngredients().get(1).getName());



    }
}
