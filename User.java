import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.sql.Connection;
import java.util.Random;

public class User {
    Connection conn;
    Random r;
    Scanner in = new Scanner(System.in);

    public User(Connection conn) {
        this.conn = conn;
    }

	public void registerNewUser() {
	        while (true) {
	            String name = in.nextLine();
	            try {
	                if (checkExistingUser(name).getString("name").equals(name)) {
	                    System.out.println("User with username: " + name + " already exists!");
	                    System.out.println("Please choose a different UserName: ");
	                } else {
	                    String pw = createPw();
	                    String email = createEmail();
	                    inserUserInDB(name, pw, email);

	                }
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
    }

    public ResultSet checkExistingUser(String name) {
        String sql = "SELECT name FROM USERS WHERE NAME=" + name;
        ResultSet result = null;
        try {
            Statement stm = conn.createStatement();
            result = stm.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String createPw() {
        System.out.print("Please choose a valid Password, or type 'help' to get an auto-generated one: ");
        while (true) {
            String pw = in.nextLine();
            if (!pw.equals("help")) {
                if (pw.length() <= 5
                        || (!pw.contains("!") && !pw.contains("@") && !pw.contains("#") && !pw.contains("$"))) {
                    System.out.print("Your password is really weak! Please choose a different one: ");
                } else {
                    System.out.println("Password has been successfully created!");
                    return pw;
                }
            } else {
                r = new Random();
                String an = "0123456789abcdefghijklmnopqrstuvwxyz!@#$";
                while (true) {
                    StringBuilder agpw = new StringBuilder();
                    int len = r.nextInt(10) + 6;
                    int s = 1;
                    for (int i = 0; i < len; i++) {
                        s = r.nextInt(39) + 1;
                        agpw.append(an.substring(s, s + 1));
                    }
                    System.out.println("Your generated password is: " + agpw + " , good choice!");
                    System.out.print("Want to generate a new one? (Y/N) : ");
                    String x = in.nextLine();
                    if (x.equals("Y")) {
                        continue;
                    } else {
                        return agpw.toString();
                    }
                }
            }
        }
    }

    public String createEmail() {
        System.out.print("Please enter your email: ");
        while (true) {
            String email = in.nextLine();
            if (email.contains("@") && ((email.contains(".com") || email.contains(".gr")))) {
                System.out.println("Valid email sucessfully registered!");
                return email;
            } else {
                System.out.println("Invalid email!");
                System.out.print("Please enter a valid email: ");
            }
        }

    }

    public void inserUserInDB(String name, String pw, String email) {
        String sql = "INSERT INTO USERS(Username, Password, Email) VALUES(?, ?, ?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, pw);
            ps.setString(3, email);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
