import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.sql.Connection;
import java.util.Random;

/**
 *  Responsible for basic User needs, such as Registration, Login and Logout and the main menu of the app.
 *  Authors: Ilias Mpourdakos, Ioannis Papadakis, Ioanna Karitsioti
*/

public class User {
    Connection conn;
    Random r;
    Scanner in = new Scanner(System.in);
    String loggedUsername; // logged users' name (given by login method)
    boolean wantsToLogout;

    /**
    *  Sets the Connection object with the signature of the Database.
    */

    public User(Connection conn) {
        this.conn = conn;
    }

    /** 
     * Registers a new user in the Database.
    */
	public void registerNewUser() {
	        while (true) {
                System.out.println("Enter a username!");
	            String name = in.nextLine();
                clearScreen();
	                if (checkExistingUser(name)) {
	                    System.err.println("User with username: " + name + " already exists!");
	                    System.out.println("Please choose a different UserName: ");
	                } else {
                        String pw = createPw(); // create password
                        clearScreen();
	                    String email = createEmail(); // create email
                        clearScreen();
                        String temp = createKword(); // temporary
                        clearScreen();
                        String kword = (!temp.equals("-1")?temp:null); // if user didn't give Keyword, insert null
                        int disc = createDisc();
                        clearScreen();
	                    inserUserInDB(name, pw, email, kword, disc); // insert the tuple in the DB
                        System.out.println("Account Successfully created!");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {}
                        clearScreen();
                        break;
                    } 
	        }
    }
    
    /** 
     * Returns true if the user exists, or false differently.
     * @param name of the user that we want to check
     * @return a boolean value (true/false)
     */  
    public boolean checkExistingUser(String name) {
        String sql = "SELECT COUNT(Username) as count " +
                     "FROM Users " +
                     "WHERE Username= '" + name+ "'";
        boolean userExists = false; // assume user doesn't exist
        try (Statement stm = conn.createStatement();
            ResultSet result = stm.executeQuery(sql);){
            userExists = result.getInt(1) > 0; // if the user exists, put true
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } 
        return userExists;
    }

    /** 
     *Creates a password for the user
     */
    public String createPw() {
        System.out.print("Please choose a valid Password, or type 'help' to get an auto-generated one: ");
        while (true) {
            String pw = in.nextLine();
            clearScreen();
            if (!pw.equals("help")) {
                if (pw.length() <= 5
                        || (!pw.contains("!") && !pw.contains("@") && !pw.contains("#") && !pw.contains("$"))) {
                    System.err.print("Your password is really weak! Please choose a different one(or type help): ");
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
                        clearScreen();
                        continue;
                    } else {
                        return agpw.toString();
                    }
                }
            }
        }
    }

     /**
      * Creates an email for the user
      */
    public String createEmail() {
        System.out.print("Please enter your email: ");
        while (true) {
            String email = in.nextLine();
            clearScreen();
            if (email.contains("@") && ((email.contains(".com") || email.contains(".gr")))) {
                System.out.println("Valid email sucessfully registered!");
                return email;
            } else {
                System.err.println("Invalid email!");
                System.out.print("Please enter a valid email: ");
            }
        }

    }

    /**
     *  Creates a keyword for the user
     */
    public String createKword() {
        System.out.println("Please enter a hobby/thing you like, so we can better customize your experience!");
        System.out.println("or press -1 if you don't wish to");
        var ans = in.nextLine();
        return ans;
    }

    /**
     *  Creates a discoverability preference (public/private) for the user
     */
    public int createDisc() {
        System.out.println("Would you like for others to discover you by name-search? 0 = no, 1 = yes");
        System.out.println("Keep in mind that if you press 1, other users can add you to groups without the need of your acceptance");
        int ans;
        do {
            ans = in.nextInt();
            switch (ans) {
                case 0:
                case 1:
                    in.nextLine();
                    return ans;
                default:
                    clearScreen();
                    System.out.println("Invalid input! please type 0 or 1");
                    continue;
            }
        } while(true);
    }

    /** 
     *  Inserts a user tuple in the Database.
     *  @param Name,password,email,keyword and discoverability preference of the given user
     */
    public void inserUserInDB(String name, String pw, String email, String kword, int disc) {
        String sql = "INSERT INTO USERS(Username, Password, Email, UserKeywords, Discoverable)" +
                        "VALUES(?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql);) {           
            ps.setString(1, name);
            ps.setString(2, pw);
            ps.setString(3, email);
            ps.setString(4, kword);
            ps.setInt(5, disc);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Oops! Something Went Wrong During Registration");
        }
    }
    
    /** 
     *  Verifies the user who is about to login.
     *  Provides 5 options to the user (main menu), after successful login.
     */
    public void login() {
        String username;
        String password;
        while (true) {
            System.out.println("------Login------");
            System.out.println("Please Insert Your UserName:");
            username = in.nextLine();
            clearScreen();
            System.out.println("Please Insert Your Password:") ;
            password = in.nextLine();
            clearScreen();
            if (checkExistingUser(username)) {
                String sql = "SELECT Password " +
                                "FROM Users " +
                                "WHERE Username='" + username + "'";
                try (Statement stm = conn.createStatement();
                    ResultSet rs = stm.executeQuery(sql);) {
                    String correctpw = rs.getString("Password");
                    if (correctpw.equals(password)) {
                        loggedUsername = username;                 
                        sql = "UPDATE Users " + 
                                "SET LastLoginTime = DATETIME('now','Localtime') "+
                                "WHERE Username='" + loggedUsername + "'";
                        stm.executeUpdate(sql);
                        System.out.println("Successful Login!");
                        Thread.sleep(1050);
                        break;
                    } else {
                        System.err.println("Incorrect password, please try again!");
                    }
                } catch (SQLException e) {
                    System.err.println("Oops! something went wrong while logging in, please try again");
                } catch (InterruptedException e) {}
            } else {
                int a;
                do {
                    System.out.println("Unrecognized User: " + username + ". Press 1 to try logging in again with a different name, or 2 to create a new account...");
                    a = in.nextInt();
                    clearScreen();
                } while (a != 1 && a != 2 );
                in.nextLine(); // clear the buffer
                switch (a) {
                    case 1:
                        continue;
                    case 2 :
                        registerNewUser(); // Sign up the user, then login
                        continue;
                }
            }
        }
        
	MsgGroup mg = (MsgGroup) this;
        wantsToLogout = false;
        while (!wantsToLogout) {
            clearScreen();
            System.out.println("What Would You Like to Do?");
            System.out.println("1 : See which of your groups have new messages");
            System.out.println("2 : See All Your Groups");
            System.out.println("3 : Create a Group");
            System.out.println("4 : Profile");
            System.out.println("5 : Sign out");
            int ans;
            do {
                ans = in.nextInt();
                switch (ans) {
                    case 1:
                        mg.showNewMessages(); // go to the new msgs screen
                        break;
                    case 2:
                        mg.showMsgGroups(); // go to the group list screen
                        break;
                    case 3:
                        mg.createMsgGroup(); // go to the create new group screen
                        break;
                    case 4:
                        UserProfile.setConn(conn);
                        UserProfile.setLogName(loggedUsername);
                        clearScreen();
                        try {
                            System.out.print("Retrieving Data");
                            Thread.sleep(500);
                            System.out.print(".");
                            Thread.sleep(500);
                            System.out.print(".");
                            Thread.sleep(500);
                            System.out.print(".");
                            clearScreen();
                            UserProfile.showProfile(); // show full stats of logged-in user
                        } catch (InterruptedException e) {}
                        break;
                    case 5:
                        doGet(); // go to logout screen
                        break;
                    default:
                        clearScreen(); // else try again
                        System.out.println("Invalid input! please type 1, 2, 3, 4 or 5");
                        continue;
                }
                break;
            } while(true);
        }
    }

    /** 
     * Logs out the logged-in user.
     * Updates the LastLogoutTime of said user.
    */
    public void doGet() {
        clearScreen();
        in.nextLine(); // clear the buffer
		System.out.println("Do you want to logout? (YES/NO)");
		String respond1 = in.nextLine();
        clearScreen();
		while (!respond1.equals("YES") && !respond1.equals("NO")) {
	    	System.out.println("Invalid input");
	    	System.out.println("Give your answer again");
	    	respond1 = in.nextLine();
            clearScreen();
	    }
		if (respond1.equals("YES")) {
            wantsToLogout = true;
                // register the Logout time in DB
                String sql = "UPDATE Users " + 
                                "SET LastLogoutTime = DATETIME('now','Localtime') "+
                                "WHERE Username = '" + loggedUsername + "'";
		    try (Statement stmt = conn.createStatement();) {        
                stmt.executeUpdate(sql);
                System.out.print("Logging out");
                Thread.sleep(500);
                System.out.print(".");
                Thread.sleep(500);
                System.out.print(".");
                Thread.sleep(500);
                System.out.print(".");
                Thread.sleep(500);
            } catch (Exception e) {
                System.err.println("Something Went Wrong While Logging Out!");
            }
	       // if the user doesn't want to logout, return to the Options Screen
		}
    }

    /**
     *  Clears the CL Screen for better readability.
    */
    public void clearScreen() {
        try {
            final String os = System.getProperty("os.name");
            if (os.contains("Windows"))
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            else
                Runtime.getRuntime().exec("clear");
        } catch (Exception e) {
            System.err.println("Oops! Something Went Wrong With the Execution");
        }
    }

    /**  
     * Kills the CL Screen.
     */
    public void killScreen() {
        try {
            Runtime.getRuntime().exec("taskkill /f /im cmd.exe") ;
        } catch (Exception e) {
            System.err.println("Something Went Wrong When Exiting the App!");
        }
    }
}
