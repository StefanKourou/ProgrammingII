import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

import javax.naming.spi.DirStateFactory.Result;

import java.sql.Connection;

public class User {
    Scanner in = new Scanner(System.in);

    public ResultSet CheckExistingUser(Connection conn, String name) {
        Statement stm = conn.createStatement();
        String sql = "SELECT name FROM USERS WHERE NAME="+name;
        ResultSet  result = stm.executeQuery(sql);
        return result;
    }

    public String RegisterNewUser() {
       // Statement stm = conn.createStatement();
        //String sql = "";
        String name = in.nextLine();
        while (true) {
            if (!CheckExistingUser(name).equals(null)) {
                System.out.println("User with username: " + name + " already exists!");
                System.out.println("Please choose a different UserName!");
            } else {
                String pw = CreatePw();
                String email = CreateEmail();


            }
        }
    }
    
    public String CreatePw() {
        System.out.print("Please choose a valid Password: ");        
        String pw;
        while (true) {
            pw = in.nextLine();
            if (pw.length() <= 5) {
                System.out.print("Your password is really weak! Please choose a different one: ");
            } else {
                System.out.println("Password has been successfully created!");
                break;
            }
        }
      return pw;
    }
    public String CreateEmail() {
        System.out.print("Please enter your email: ");
        while (true) {
            String email = in.nextLine();
            if (email.contains("@") && (email.contains(".com") || email.contains(".gr"))) {
                    System.out.println("Valid email sucessfully created!");
                    return email;
            } else {
                System.out.println("Invalid email!");
                System.out.print("Please enter a valid email: ");
            }
        }
        //return null;
    }
            //String[] part = email.split();
            //String[] del = email.split(".");
            //String[][] Farray;
            //Farray[0] = part;
            //Farray[1] = del;
            //for (String[] var : Farray) {
              //  for (String var2 : var)
                //    if (var2.equals("@") || var2.equals("com") || var2.equals("gr")) {
                  //      System.out.println("Valid email sucessfully created! ");
                    //    break;
                    //}
            
                //}
            //}
        

    

    public void InserUserInDB(String name, String pw, String email, String login_time) {
            String sql = "INSERT INTO USERS(Username, Password, Email, LoginTime) VALUES(?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, pw);
            ps.setString(3, email);
            ps.setString(4, login_time);
            ps.executeUpdate();
    }

    
    

}
