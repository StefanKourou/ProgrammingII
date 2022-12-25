import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.util.Scanner;

public class Message extends User {
    int groupID;

    public Message(Connection conn) {
        super(conn);
    }

	public void showLastMessages(int groupID) {
		this.groupID = groupID;
		String msgGroups =
	    "SELECT MsgText, MsgUsername" +
		"FROM Messages" +
		"WHERE MsgGroup=" + groupID +
		"ORDER BY MsgCreationTime ASC" +
		"LIMIT 10";
	    try (Statement stmt  = conn.createStatement();
	        ResultSet queryresult = stmt.executeQuery(msgGroups)) {
	      	// loop through the result set
	       	while (queryresult.next()) {
	        System.out.println(queryresult.getString("MsgText") + "\t" +
	            			queryresult.getString("MsgUsername"));
	        }
	    } catch (SQLException e) {
			System.out.println(e.getMessage());
	        System.err.println("There are no new messages. Try again later!");
	    }
    }

    public void createMessage(String msgText) {
	String newMessage =
		"INSERT INTO Messages (" +
		    "MsgUsername, MsgGroup, MsgText, MsgCreationTime)" +  //USERNAME APO TH LOGIN GROUPID APO TH MESSAGE GROUP
			"VALUES (" +			
			"'" + loggedUsername + "'" + "," + groupID + ",'" + msgText + "'', DATETIME('now','localtime'))";
		try (Statement stmt  = conn.createStatement();) {
			stmt.executeUpdate(newMessage); 
		} catch (SQLException e) {
			System.err.println("Oops! Something went wrong while sending the message!");
		}
    }
	
	public void showMessageInfo(int msgID) {
	    String msgInfo = "SELECT MsgUsername, MsgText, MsgCreationTime FROM Messages where MsgID =" + msgID;
        try (Statement stmt  = conn.createStatement();
            ResultSet queryresult = stmt.executeQuery(msgInfo)){           
            System.out.println("The user who sent it was: " + queryresult.getString("MsgUsername") + "\t" + 
            		" The message was: " + queryresult.getBoolean("MsgText") + "\t" +
            		" This message was created at: " + queryresult.getString("MsgCreationTime"));
			System.out.println("Do you wish to react to this message?");
			System.out.println("1: like, 2: dislike, 3: heart, or 4: no");
			in = new Scanner(System.in);
			int re = in.nextInt();
			if (re != 4) reactionsMessage(msgID, re);
        } catch (SQLException e) {
            System.err.println("Oops! Something went wrong when retrieving the message.");
        }
    }

    public void reactionsMessage(int msgID, int re) {
        String newReaction =
        "INSERT INTO Reactions (ReMsg, ReUsername, Reaction)" +
        "VALUES (" + msgID + ",'" + loggedUsername + "'," + re + ")";
		try (Statement stmt  = conn.createStatement()) {
			stmt.executeUpdate(newReaction);
		} catch (SQLException e) {
			System.err.println("Oops! Something went wrong when reacting to the message.");
		}
    }

}
