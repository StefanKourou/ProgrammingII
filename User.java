import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.sql.Connection;

public class User {
    Scanner in = new Scanner(System.in);

    public ResultSet CheckExistingUser(Connection conn, String name) {
        String sql = "SELECT name FROM USERS WHERE NAME="+name;
        ResultSet  result = null;
        try {    
            Statement stm = conn.createStatement();
            result = stm.executeQuery(sql);    
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;       
    }

    public String RegisterNewUser(Connection conn) {
        while (true) {
            String name = in.nextLine();
            try { 
                if (CheckExistingUser(conn, name).getString("name").equals(name)) {
                        System.out.println("User with username: " + name + " already exists!");
                        System.out.println("Please choose a different UserName: ");
                    } else {
                        String pw = CreatePw();
                        String email = CreateEmail();
                        InserUserInDB(conn, name, pw, email, "login_time");

                    }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
    
    public String CreatePw() {
        System.out.print("Please choose a valid Password: ");        
        while (true) {
          String  pw = in.nextLine();
            if (pw.length() <= 5 || (!pw.contains("!") && !pw.contains("@") && !pw.contains("#") && !pw.contains("$"))) {
                System.out.print("Your password is really weak! Please choose a different one: ");
            } else {
                System.out.println("Password has been successfully created!");
                return pw;
            }
        }
    }
    public String CreateEmail() {
        System.out.print("Please enter your email: ");
        while (true) {
            String email = in.nextLine();
            if (email.contains("@") && ((email.contains(".com") || email.contains(".gr")))) {
                    System.out.println("Valid email sucessfully created!");
                    return email;
            } else {
                System.out.println("Invalid email!");
                System.out.print("Please enter a valid email: ");
            }
        }
        
    }
        
        public void InserUserInDB(Connection conn, String name, String pw, String email, String login_time) {
            String sql = "INSERT INTO USERS(Username, Password, Email, LoginTime) VALUES(?, ?, ?, ?)";
            try {
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, name);
                ps.setString(2, pw);
                ps.setString(3, email);
                ps.setString(4, login_time);
                ps.executeUpdate();
    
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }    
    

}
