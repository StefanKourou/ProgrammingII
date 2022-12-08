package core;

import java.io.IOException;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
import java.sql.*;

public class Connect {

    public static void connect() {
        Connection conn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            String url = "jdbc:sqlite:C:/sqlite/db/Progr2.db";
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) { 
            System.err.println(e);
        }finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        connect();
        try {
			final Process driver = new ProcessBuilder("CMD", "/C", "java -classpath C:\\sqlite\\sqlite-jdbc-3.40.0.0.jar").start();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
