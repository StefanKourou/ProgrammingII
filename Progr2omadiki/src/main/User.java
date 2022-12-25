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
    String loggedUsername; // edw mpainei to onoma tou xrhsth pou einai logged in ayth thn stigmh(to vazei h login method)
           
    public User(Connection conn) {
        this.conn = conn;
    }

    // registers a new user in the DB
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
                        login();
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
        } catch (SQLException e) {
            System.out.println("Oops! Something Went Wrong");
        } 
        return userExists;
    }

    // creates a password for a user
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

    // creates an email for a user
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

    // creates a key word for a user
    public String createKword() {
        System.out.println("Please enter a hobby/thing you like, so we can better customize your experience!");
        System.out.println("or press -1 if you don't wish to");
        var ans = in.nextLine();
        return ans;
    }

    // creates a discoverability preference for a user
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

    // inserts a user tuple in the DB
    public void inserUserInDB(String name, String pw, String email, String kword, int disc) {
        String sql = "INSERT INTO USERS(Username, Password, Email, UserKeywords, Discoverable) VALUES(?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
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

    // verify the user about to login
    public void login() {
        System.out.println("------Login------");
        String username;
        String password;
        while (true) {
            System.out.println("Please insert username:");
            username = in.nextLine();
            clearScreen();
            System.out.println("Please insert Password:") ;
            password = in.nextLine();
            clearScreen();
            if (checkExistingUser(username)){
                ResultSet rs = null;
                try {
                    Statement stm = conn.createStatement();
                    rs = stm.executeQuery("SELECT Password FROM Users WHERE Username='" + username + "'");
                    String correctpw = rs.getString("Password"); // PREPEI NA BALOYME METHODO POY NA EPISTREFEI WS STRING TO PW
                    if (correctpw.equals(password)){
                        loggedUsername = username;                   
                        String sql = "UPDATE Users SET LastLoginTime = DATETIME('now','localtime') WHERE Username='" + loggedUsername + "'";
                        stm.executeUpdate(sql);
                        System.out.println("Successful Login!");
                        break;
                    } else {
                        System.err.println("Incorrect password, please try again!");
                    }
                } catch (SQLException e) {
                    System.err.println("Oops! something went wrong while logging in, please try again");
                }
            } else {
                int a;
                do {
                    System.out.println("Unrecognized User: " + username + ". Press 1 to try logging in again with a different name, or 2 to create a new account...");
                    a = in.nextInt();
                    clearScreen();
                } while (a != 1 && a != 2 );
                in.nextLine(); // discard unwanted input from the buffer
                switch (a) {
                    case 1:
                        continue;
                    case 2 :
                        registerNewUser();
                        continue;
                }     
            }
        }
        System.out.println("What Would You Like to Do?");
        System.out.println("1 : See wich of your groups have new messages");
        System.out.println("2 : See All Your Groups");
        System.out.println("3 : Create a Group");
        int ans;
        do {
            ans = in.nextInt();
            switch (ans) {
                case 1:
                    // call method that shows wich groups have new msgs
                    System.out.println("Case 1");
                    break;
                case 2:
                    //this.showMsgGroups();
                    System.out.println("Case 2");
                    break;
                case 3:
                    //this.createMsgGroup();
                    System.out.println("Case 3");
                    break;
                default:
                    clearScreen();
                    System.out.println("Invalid input! please type 1, 2 or 3");
                    continue;
            }
            in.nextLine(); // discard unwanted input from the buffer
            break;
        } while(true);
       
    }

    // logout
    public void doGet() {
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
                // register the Logout time in DB
                String sql1 = "UPDATE Users SET LastLogoutTime = DATETIME('now','localtime') WHERE Username = ' " + loggedUsername + " ' ";
		    try {
                PreparedStatement ps = conn.prepareStatement(sql1);
                ps.executeUpdate();
            } catch (Exception e) {
                System.err.println("Something Went Wrong While Logging Out!");
            }       
	       // if the user doesn't want to logout, same screen as right after login
		} else {
            System.out.println("What Would You Like to Do?");
            System.out.println("1 : See wich of your groups have new messages");
            System.out.println("2 : See All Your Groups");
            System.out.println("3 : Create a Group");
            int ans;
            do {
                ans = in.nextInt();
                switch (ans) {
                    case 1:
                        // call method that shows wich groups have new msgs
                        System.out.println("Case 1");
                        break;
                    case 2:
                        //this.showMsgGroups();
                        System.out.println("Case 2");
                        break;
                    case 3:
                        //this.createMsgGroup();
                        System.out.println("Case 3");
                        break;
                    default:
                        clearScreen();
                        System.out.println("Invalid input! please type 1, 2 or 3");
                        continue;
                }
                in.nextLine(); // discard unwanted input from the buffer
                break;
            } while(true);
	    }
    }

    // clears the CL Screen for better readability
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

    // kills the CL Screen
    public void killScreen() {
        try {
            Runtime.getRuntime().exec("taskkill /f /im cmd.exe") ;
        } catch (Exception e) {
            System.err.println("Something Went Wrong When Exiting the App!");
        }
    }
}

