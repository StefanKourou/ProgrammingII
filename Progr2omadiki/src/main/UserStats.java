import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;

public class UserStats {
    
    static Connection conn;
    static String loggedUsername;

    public static void setConn(Connection conne) {
        conn = conne;
    }

    public static void setLogName(String name) {
        loggedUsername = name;
    }

    public static void showFullStats() {
        System.out.println("----" + loggedUsername + "----");
        showMsgCount();
        showTimeLoggedIn();
        showReactions();
    }

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
