// package net.Progr2;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;



public class Connect {

    public static Connection connect() {
        Connection conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:C:/sqlite/db/Progr2.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            
            System.out.println("Connection to SQLite has been established.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
           /*  try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
    */ }
            return conn;
    }
        
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        boolean end = false;
        Scanner input = new Scanner(System.in);
        Connection conn = connect();
        Message obj = new Message(conn);
        obj.clearScreen();
        System.out.println("---Welcome To UniTeD---");
        do {
            try {
                Thread.sleep(1050);
                obj.clearScreen();
            } catch (InterruptedException e) {}
            System.out.println("Press 1 to Sign in");
            System.out.println("Press 2 to Sign up");
            System.out.println("Press 3 to Exit The App (sorry to see you go..)");
            int ans;
            do {
                ans = input.nextInt();
                obj.clearScreen();
                switch (ans) {
                    case 1:
                        obj.login();
                        break;
                    case 2:
                        obj.registerNewUser();
                        obj.login();
                        break;
                    case 3:
                        end = true;
                        System.out.println("bye...");
                        try {
                            Thread.sleep(1000);
                            obj.killScreen();
                        } catch (InterruptedException e){}
                        break;
                    default:
                        obj.clearScreen();
                        System.out.println("Invalid input! please type 1, 2 or 3");
                        continue;
                }
                input.nextLine(); // clear the buffer
                break;
            } while (true);
        } while (!end);
        input.close();
    }

}
