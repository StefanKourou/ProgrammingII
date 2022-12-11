//package core;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.sql.Connection;
import java.util.Random;
import java.sql.DriverManager;


public class User {
    Connection conn;
    Random r;
    Scanner in = new Scanner(System.in);
    String name; // edw mpainei to onoma tou xrhsth pou einai logged in ayth thn stigmh(to vazei h login method)

    public static void main(String[] args) {
        Connection conn = null;
        try {
            String url = "C:/sqlite/db/Progr2.db";
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
           
    public User(Connection conn) {
        this.conn = conn;
    }

    // registers a new user in the DB
	public void registerNewUser() {
	        while (true) {
                System.out.println("Enter a username!");
	            String name = in.nextLine();
	                if (checkExistingUser(name)) {
	                    System.err.println("User with username: " + name + " already exists!");
	                    System.out.println("Please choose a different UserName: ");
	                } else {
                        String pw = createPw(); // create password
	                    String email = createEmail(); // create email
                        String temp = createKword(); // temporary 
                        String kword = (!temp.equals("-1")?temp:null); // if user didn't give Keyword, insert null
	                    inserUserInDB(name, pw, email, kword); // insert the tuple in the DB
                        System.out.println("Account Successfully created!");
                        System.out.println("Login:");
                        // kalw thn login
                        break;
                    } 
	        }
    }
    
    // returns true if user exists, or false differently
    public boolean checkExistingUser(String name) {
        String sql = "SELECT COUNT(Username) as count FROM USERS WHERE Username= '" + name+ "'";
        ResultSet result = null;
        boolean userExists = true; // assume user already exists
        try {
            Statement stm = conn.createStatement();
            result = stm.executeQuery(sql);
            if (result.getInt(1) == 0) userExists = false; // user doesn't exist in the DB
        } catch (SQLException e) {} 
        return userExists;
    }

    // creates a password for a user
    public String createPw() {
        System.out.print("Please choose a valid Password, or type 'help' to get an auto-generated one: ");
        while (true) {
            String pw = in.nextLine();
            if (!pw.equals("help")) {
                if (pw.length() <= 5
                        || (!pw.contains("!") && !pw.contains("@") && !pw.contains("#") && !pw.contains("$"))) {
                    System.err.print("Your password is really weak! Please choose a different one: ");
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
                    if (x.equals("Y") || x.equals("y")) {
                        continue;
                    } else {
                        return agpw.toString();
                    }
                }
            }
        }
    }

    // creates an email for a user
    public String createEmail() {
        System.out.print("Please enter your email: ");
        while (true) {
            String email = in.nextLine();
            if (email.contains("@") && ((email.contains(".com") || email.contains(".gr")))) {
                System.out.println("Valid email sucessfully registered!");
                return email;
            } else {
                System.err.println("Invalid email!");
                System.out.print("Please enter a valid email: ");
            }
        }

    }

    // creates a key word for a user
    public String createKword() {
        System.out.println("Please enter a hobby/thing you like, so we can better customize your experience!");
        System.out.println("or press -1 if you don't wish to");
        return in.nextLine();
    }

    // inserts a user tuple in the DB
    public void inserUserInDB(String name, String pw, String email, String kword) {
        String sql = "INSERT INTO USERS(Username, Password, Email, UserKeywords) VALUES(?, ?, ?, ?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, pw);
            ps.setString(3, email);
            ps.setString(4, kword);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
