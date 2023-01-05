import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.util.Scanner;

/**  
* Contains all User-statistics static methods
* Authors: Ilias Mpourdakos
*/

public class UserProfile {

    static Scanner input = new Scanner(System.in);

    /**
    * Connection object 
    */
    static Connection conn;

    /**
    * logged-in user, all methods are responsible for retrieving data from this user
    */
    static String loggedUsername;

    /**
    *  Sets the Connection object
    */
    public static void setConn(Connection conne) {
        conn = conne;
    }

    /** 
    *  Sets the logged-in user
    */
    public static void setLogName(String name) {
        loggedUsername = name;
    }

    /**
     * shows to the screen a complete profile of the logged-in user
     */
    public static void showProfile() {
        String sql1 = "SELECT Email, Discoverable, UserKeywords " +
                        "FROM Users " +
                        "WHERE Username = '" + loggedUsername + "'";
        System.out.println("----" + loggedUsername + "----");
        try (Statement stmt = conn.createStatement();  
                ResultSet rs = stmt.executeQuery(sql1);) {
            System.out.println("General Profile Info:");
            System.out.println("Email: " + rs.getString("Email"));
            System.out.println("Keyword: " + rs.getString("UserKeywords"));
            System.out.print("Profile status: ");
            if (rs.getInt("Discoverable") == 1) {
                System.out.println("Public");
            } else {
                System.out.println("Private");
            }
        } catch (SQLException e) {
            System.err.println("Something went wrong!");
        }
        System.out.println("\n" + "Want to see your stats? (YES/NO)");
        String ans = input.nextLine();
        while (!ans.equals("YES") && !ans.equals("NO")){
            System.out.println("Invalid input! Please type YES or NO");
            ans = input.nextLine();
        }
        if (ans.equals("YES")) {
            showFullStats();
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {}
        }
    }

    /**  
    *  Shows the full stats of the user
    */
    public static void showFullStats() {
        showMsgCount();
        showTimeLoggedIn();
        showReactions();
    }

    /**  
    *  Shows the Count of the Messages sent by the user
    */
    public static void showMsgCount() {
        int msgs;
        String sql = "SELECT COUNT(MsgID) " +
                        "FROM Messages " +
                        "WHERE MsgUsername ='" + loggedUsername + "'";
        try (Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(sql)) {
                msgs = rs.getInt(1);
                System.out.println("MESSAGES SENT: " + msgs);
        } catch (SQLException e) {
            System.err.print("Opps! Something Went Wrong While Retrieving Data!");
        }
    }

    /**  
    *  Shows the current Login time of the user
    */
    public static void showTimeLoggedIn() {
        String timeLogged;
        String sql = "SELECT (strftime('%M', 'now') - strftime('%M',  LastLoginTime)) as timeLogged " +
                        "FROM Users " +
                        "WHERE Username ='" + loggedUsername + "'";
        try (Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(sql)) {
                timeLogged = rs.getString("timeLogged");
                System.out.println("TIME LOGGED IN: " +timeLogged + " MINUTES");
        } catch (SQLException e) {
            System.err.print("Opps! Something Went Wrong While Retrieving Data!");
        }                
    }

    /**  
    *  Shows the Reactions that the user has given and has been given with a graphical way
    */
    public static void showReactions() {
        int likesUserGave = 0;
        int dislikesUserGave = 0;
        int heartsUserGave = 0;
        int likesUserHas = 0;
        int dislikesUserHas = 0;
        int heartsUserHas = 0;
        // check if this user has reacted to any msgs
        String sqltest = "SELECT COUNT(Reaction) " + 
                            "FROM Reactions " +
                            "WHERE ReUsername ='" + loggedUsername + "'";
        try (Statement stmt  = conn.createStatement();) {
            ResultSet rs = stmt.executeQuery(sqltest);
            // if this user has reacted to at least 1 msg, show the reactions to the screen
            if (rs.getInt(1) > 0) {
                String sql = "SELECT Reaction " +
                                "FROM Reactions " +
                                "WHERE ReUsername ='" + loggedUsername + "'";
                rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    var re = rs.getInt("Reaction");
                    switch (re) {
                        case 1:
                            likesUserGave++;
                            break;
                        case 2:
                            dislikesUserGave++;
                            break;
                        case 3:
                            heartsUserGave++;
                            break;
                    }
                }
            }
            System.out.println("You have:" + "\n\t" + "liked " + likesUserGave + " message(s)" + "\n\t" +
                                    "disliked " + dislikesUserGave + " message(s)" + "\n\t" +
                                    "and loved " + heartsUserGave + " message(s)");
            // check if this user has any msgs that have been reacted to            
            sqltest = "SELECT COUNT(ReMsg) " +
                        "FROM Reactions " +
                        "WHERE ReMsg in (SELECT MsgID " +
                                            "FROM Messages " +  
                                            "WHERE MsgUsername ='" + loggedUsername + "')";
            rs = stmt.executeQuery(sqltest);
            // if this user has at least 1 msg that has been reacted to, show the reactions to the screen
            if (rs.getInt(1) > 0) {
                String sql = "SELECT Reaction " +
                                "FROM Reactions " +
                                "WHERE ReMsg in (SELECT MsgID " +
                                                    "FROM Messages " +  
                                                    "WHERE MsgUsername ='" + loggedUsername + "')";
                rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    var re = rs.getInt("Reaction");
                    switch (re) {
                        case 1:
                            likesUserHas++;
                            break;
                        case 2:
                            dislikesUserHas++;
                            break;
                        case 3:
                            heartsUserHas++;
                            break;
                    }
                }
            }
            System.out.println("Your messages have been:" + "\n\t" + "liked " + likesUserHas + " time(s)" + "\n\t" +
                                    "disliked " + dislikesUserHas + " time(s)" + "\n\t" +
                                    "and loved " + heartsUserHas + " time(s)");
        } catch(SQLException e) {
            System.err.print("Opps! Something Went Wrong While Retrieving Data!");
        }  
    }

    /**  
    *  Returns the Last Login time of the user
    */
    public static String getLoginTime() {
        String logintime;
        String sql = "SELECT LastLoginTime " +
                        "FROM Users " +
                        "WHERE Username ='" + loggedUsername + "'";
        try (Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(sql)) {
                logintime = rs.getString("LastLoginTime");
        } catch (SQLException e) {
            System.err.print("Opps! Something Went Wrong While Retrieving Data!");
            logintime = "-1";
        }              
        return logintime;
    }

    /**  
    *  Returns the Last Logout time of the user
    */
    public static String getLogoutTime() {
        String logouttime;
        String sql = "SELECT LastLogoutTime " +
                        "FROM Users " +
                        "WHERE Username ='" + loggedUsername + "'";
        try (Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(sql)) {
                logouttime = rs.getString("LastLoginTime");
        } catch (SQLException e) {
            System.err.print("Opps! Something Went Wrong While Retrieving Data!");
            logouttime = "-1";
        }              
        return logouttime;
    }
}
