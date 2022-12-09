import java.util.Scanner;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;


public class UserLogin {
    Scanner input = new Scanner(System.in);
    public String login() {
        String returned = "null";
        String username="null";
        String password="null";
        boolean gotoregister = false;
        while (result.equals(null)) do {
            System.out.println("Please insert username:");
            username = input.nextline();
            System.out.println("Please insert Password:") ;
            password = input.nextline();
            
            if (checkExistingUser(username)){
                ResultSet rs = stmt.executeQuery("SELECT * FROM Users WHERE Username="+username;);
                String correctpw = rs.getString("Password");
                if (correctpw == password){
                    System.out.println("Successful Login!");
                    returned = rs.getString("Username");
                    //allagh last login time
                }
            } else {
                int a;
                do {
                    System.out.println("Oops! Something went wrong! Press 1 to try logging in again or 2 to create a new account...")
                    a = input.nextInt();
                } while (a=!1 && a=!2 );
                if ( a==1 ) {
                   continue;
                } else {
                    registerNewUser();
                    continue;
                }
            }
            return returned;
        }
    }
}