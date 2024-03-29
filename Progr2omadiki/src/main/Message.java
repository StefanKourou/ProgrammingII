import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;

/**
 * Contains all Message related operations given a groupID.
 * @author Giorgos Tsakalos
 * @author Manolis Gialouris
 * @author Ilias Mpourdakos
 * @author Stefanos Kouroupakis
 */

public class Message extends MsgGroup {

	/** 
	 * The Group that the user is in right now.
	 * It's being set by the showLastMessages(int) method
	 */
    int groupID; 

	/** 
	 * Passes the Connection object to the super-class.
	 * @param conn a Connection object with a Database signature
	 */
    public Message(Connection conn) {
        super(conn);
    }

	/**  
	 * shows the last 10 Messages of a group
	 * @param groupID the groupID of the said group
	*/
	public void showLastMessages(int groupID) {
		in.nextLine(); // clear the buffer
		this.groupID = groupID;
		int n = 0; // used to parameterize the ShowMsgs query to show more msgs
		clearScreen();
		// check if this group has any messages
		String sqltest = "SELECT COUNT(MsgText) " +
						"FROM Messages " +
						"WHERE MsgGroup = " + groupID;
	    try (Statement stmt  = conn.createStatement();) {
			System.out.print("Opening Group");
			Thread.sleep(500);
			System.out.print(".");
			Thread.sleep(500);
			System.out.print(".");
			Thread.sleep(500);
			System.out.print(".");
			Thread.sleep(500);
			do {
				String groupName = "SELECT MsgGroupName " +
									"FROM MsgGroups " +
									"WHERE msgGroupID = " + groupID;
				ResultSet rs = stmt.executeQuery(groupName);
				clearScreen();
				System.out.println("------- " + rs.getString("MsgGroupName") + " -------");
				rs = stmt.executeQuery(sqltest);
				int msgCount;
				// if this group has at least 1 message, show the last 10 on the screen
				if ((msgCount = rs.getInt(1)) > 0) {
					String msgs = "SELECT MsgText, MsgUsername, MsgID " +
									"FROM (SELECT MsgText, MsgUsername, MsgCreationTime, MsgID " +
											"FROM Messages " +
											"WHERE MsgGroup = " + groupID + " " +
											"ORDER BY MsgCreationTime DESC LIMIT 10 OFFSET " + n + ")" +
									"ORDER BY MsgCreationTime ASC";
					rs = stmt.executeQuery(msgs);
					// loop through the result set
					while (rs.next()) {
						System.out.println(rs.getString("MsgUsername") + "\t" +
												"(" + rs.getInt("MsgID") + ")" +
												rs.getString("MsgText") + "\t");
					}
				}else {
					System.out.println("This Group doesn't have any Messages. Total silence....");
				}
				System.out.println("< and > for more messages");
				Thread.sleep(1000);
				System.out.println("\n" + "type NEW if you want to send a new message, SEL if you wish to select a message, \n" +
										"or MEM to see its members");
			
				System.out.println("type NO to return to main screen");
				var ans = in.nextLine();
				while (!ans.equals("NEW") && !ans.equals("NO") && !ans.equals("SEL") && 
						 !ans.equals("<") && !ans.equals(">") && !ans.equals("MEM")) {

					System.out.println("Invalid input, please type NEW, SEL, MEM or NO ");
					ans = in.nextLine();
				}
				if (ans.equals("NEW")) {
					clearScreen();
					System.out.print("Type your nice words: ");
					var text = in.nextLine();
					createMessage(text);
					n = 0; // go to the bottom of the chat
					continue; // after sending the msg, show again all the group msgs(including the one just send)
				} else if (ans.equals("SEL")) {
					System.out.print("Give the ID of the message you want to select: ");
					var msgID = in.nextInt();
					showMessageInfo(msgID);
					n = 0; // go to the bottom of the chat
					continue; // after selecting the msg, show again all the group msgs
				} else if (ans.equals("MEM")) {
					clearScreen();
					showGroupMembers();
					n = 0; // go to the bottom of the chat
					continue; // after seeing the members, show again all the group msgs
				} else if (ans.equals("<")) {
					if (msgCount - n > 10) {
						n += 5; // increment the offset by 5, to go back 5 msgs to the screen
					} else {
						System.out.println("You reached the start of the chat!");
						System.out.println("(What were you looking for?)");
						Thread.sleep(1500);
					}
					continue;
				} else if (ans.equals(">")) {
					if (n != 0) {
						n -= 5; // decrement the offset by 5 if there are more messages to show
					} else {
						System.out.println("There are not more messages on this chat!");
						System.out.println("(You can change that)");
						Thread.sleep(1500);
					}
					continue; // show the chat again
				}
				break;
			} while (true);
	    } catch (SQLException e) {
			System.err.println("Something went wrong when accessing the messages from this group!");
	    } catch (InterruptedException e) {}
    }

	/**  
	 * Creates a new Message for a Group.
	 * @param msgText the Message that will be sent to the group
	*/
    public void createMessage(String msgText) {
		String newMessage =
		"INSERT INTO Messages (" +
		    "MsgUsername, MsgGroup, MsgText, MsgCreationTime) " +
			"VALUES (" +			
			"'" + loggedUsername + "'" + "," + groupID + ",'" + msgText + "', DATETIME('now','localtime'))";
		try (Statement stmt  = conn.createStatement();) {
			stmt.executeUpdate(newMessage);
		} catch (SQLException e) {
			System.err.println("Oops! Something went wrong while sending the message!");
		}
    }
	
	/**  
	 * Shows info about a specific Message, including the MessageCreationTime.
	 * @param msgID the ID of the message that we want to see information about
	*/
	public void showMessageInfo(int msgID) {
		clearScreen();
		// check if this group has any msgs with the id of msgID
	    String msgInfoTest = "SELECT COUNT(MsgText) " +
								"FROM Messages where MsgID =" + msgID + " " + 
								"AND MsgGroup = " + groupID;
        try (Statement stmt  = conn.createStatement();) {
			ResultSet queryresult = stmt.executeQuery(msgInfoTest);
			// if this group has at least 1 message matching the criteria, show the msgs to the screen
			if (queryresult.getInt(1) > 0) {
				String msgInfo = "SELECT MsgUsername, MsgText, MsgCreationTime " +
									"FROM Messages where MsgID =" + msgID + " " + 
									"AND MsgGroup = " + groupID;
				queryresult = stmt.executeQuery(msgInfo);
				System.out.println("The user who sent it was: " + queryresult.getString("MsgUsername") + "\n" + 
						"The message was: '" + queryresult.getString("MsgText") + "'\n" +
						"This message was created at: " + queryresult.getString("MsgCreationTime") + "\n");
				System.out.println("Do you wish to react to this message?");
				System.out.println("1: like, 2: dislike, 3: heart");
				System.out.println("4: go back");
				int re = in.nextInt();
				while (re != 1 && re != 2 && re != 3 && re != 4){
					clearScreen();
					System.out.println("Invalid input, please type 1, 2, 3 or 4");
					System.out.println("1: like, 2: dislike, 3: heart");
					System.out.println("4: go back");
					re = in.nextInt();
				}
				if (re != 4) reactionsMessage(msgID, re);
			} else {
				System.out.println("No Messages Found with the ID of " + msgID);
				Thread.sleep(1500);
			}
        } catch (SQLException e) {
            System.err.println("Oops! Something went wrong when retrieving the message.");
		} catch (InterruptedException e) {
        } finally {
			in.nextLine(); // clear the buffer
		}
    }

	/**  
	 * Registers a reaction to a Message
	 * @param msgID the ID of the Message
	 * @param re the reaction
	*/
    public void reactionsMessage(int msgID, int re) {
        String newReaction =
        "INSERT INTO Reactions (ReMsg, ReUsername, Reaction)" +
        "VALUES (" + msgID + ",'" + loggedUsername + "'," + re + ")";
		try (Statement stmt  = conn.createStatement()) {
			stmt.executeUpdate(newReaction);
		} catch (SQLException e) {
			clearScreen();
			try {
				System.err.println("Oops! Something went wrong when reacting to the message (maybe you tried to react to the same message twice?)");	
				Thread.sleep(2000);	
			} catch (InterruptedException ex) {}
		}
    }

	/**
	 * Shows to the screen the members of the selected group.
	 */
	public void showGroupMembers() {
		// query to find the owner of the group
		String findOwnerOfGroup = "SELECT MG.MsgGroupCreator " +
									"FROM MsgGroups MG " +
									"WHERE MG.MsgGroupID =" + groupID;
		// query to find the members of the group							
		String findMembersOfGroup = "SELECT GU.RelUsername as members " +
									"FROM GroupUsersRelations GU " +
									"WHERE GU.RelMsgGroup =" + groupID + " " +
									"EXCEPT " +
									"SELECT MG.MsgGroupCreator " +
									"FROM MsgGroups MG " +
									"WHERE MG.MsgGroupID =" + groupID;		
		try (Statement stmt = conn.createStatement();) {
			ResultSet rs = stmt.executeQuery(findOwnerOfGroup);
			String ownerOfGroup = rs.getString(1);
			System.out.println( "OWNER: " + ownerOfGroup); // show the owner to the screen
			rs = stmt.executeQuery(findMembersOfGroup);
			System.out.println("MEMBERS: ");
			// show the members to the screen
			while (rs.next()) {
				System.out.println(rs.getString("members"));
			}	
			System.out.println("\nDo you want to add a user to the chat? (YES/NO)");
			String ans = in.nextLine();
			while (!ans.equals("YES") && !ans.equals("NO")) {
				System.out.println("Invalid Input! Please type YES or NO");
				ans = in.nextLine();
			}
			if (ans.equals("YES")) addUser(0); // give 0 as an parameter so it can know to calculate the groupID properly
			rs.close();
		} catch (SQLException e) {
			System.err.println("Something went wrong while showing the members!");
		}
	}
}
