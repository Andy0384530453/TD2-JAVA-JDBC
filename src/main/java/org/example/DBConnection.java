package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBConnection {
    String url = System.getenv("JDBC_URL");
    String user = System.getenv("name_user");
    String mdp = System.getenv("mdp");


    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getMdp() {
        return mdp;
    }


    public Connection getDBConnection () throws SQLException {
        return DriverManager.getConnection(url,user,mdp);

    }
}
