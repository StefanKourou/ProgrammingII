package core;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.sql.Connection;
import java.util.Random;


public class Users {
    Connection conn;
    Scanner in = new Scanner(System.in);
    Random r = new Random();

    public Users(Connection conn) {
        this.conn = conn;
    }

	public void registerNewUser() {
	        while (true) {
	            String name = this.in.nextLine();
	            try {
	                if (checkExistingUser(name).getString("name").equals(name)) {
	                    System.out.println("User with username: " + name + " already exists!");
	                    System.out.println("Please choose a different UserName: ");
	                } else {
	                    String pw = createPw();
	                    String discover = creatediscover();
	                    String email = createEmail();
	                    String real = createReal();
	                    String logintime = createLogTime();
	                    inserUserInDB(name, pw, discover, email, real, logintime);

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
            Statement stm = this.conn.createStatement();
            result = stm.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String createPw() {
        System.out.print("Please choose a valid Password, or type 'help' to get an auto-generated one: ");
        while (true) {
            String pw = this.in.nextLine();
            if (!pw.equals("help")) {
                if (pw.length() <= 5
                        || (!pw.contains("!") && !pw.contains("@") && !pw.contains("#") && !pw.contains("$"))) {
                    System.out.print("Your password is really weak! Please choose a different one: ");
                } else {
                    System.out.println("Password has been successfully created!");
                    return pw;
                }
            } else {
                String an = "0123456789abcdefghijklmnopqrstuvwxyz!@#$";
                while (true) {
                    StringBuilder agpw = new StringBuilder();
                    int len = this.r.nextInt(10) + 6;
                    int s = 1;
                    for (int i = 0; i < len; i++) {
                        s = this.r.nextInt(39) + 1;
                        agpw.append(an.substring(s, s + 1));
                    }
                    System.out.println("Your generated password is: " + agpw + " , good choice!");
                    System.out.print("Want to generate a new one? (Y/N) : ");
                    String x = this.in.nextLine();
                    if (x.equals("Y")) {
                        continue;
                    }
					return agpw.toString();
                }
            }
        }
    }

    public String createEmail() {
        System.out.print("Please enter your email: ");
        while (true) {
            String email = this.in.nextLine();
            if (email.contains("@") && ((email.contains(".com") || email.contains(".gr")))) {
                System.out.println("Valid email sucessfully registered!");
                return email;
            }
			System.out.println("Invalid email!");
			System.out.print("Please enter a valid email: ");
        }

    }

    public void inserUserInDB(String name, String pw, String discover, String email, String real, String logintime) {
        String sql = "INSERT INTO Users (Username, Password, Discoverable, Email, RealName, LastLoginTime) VALUES(?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = this.conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, pw);
            ps.setString(3, discover);
            ps.setString(4, email);
            ps.setString(5, real);
            ps.setString(6, logintime);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
