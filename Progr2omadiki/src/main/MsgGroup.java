import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.sql.*;
/** 
 * Contains all MessageGroup related operations
 * Authors: Theodora Iakovaki, Haris Barbaris, Ilias Mpourdakos
*/

public class MsgGroup extends User {

	/**
	 * passes the Connection object to the super-class
	 */
	public MsgGroup(Connection conn) {
		super(conn);
	}

	/**
	 * creates a MsgGroup for the logged in user and inserts it to the Database.
	 */

	public void createMsgGroup() {
		in.nextLine(); // clear the buffer
		clearScreen();
		System.out.println("Please insert a name for your MessageGroup");
		String groupName = in.nextLine();
		System.out.println("Now insert the group's theme");
		String groupKeyword = in.nextLine();
		String sql = "INSERT INTO MsgGroups (MsgGroupName, MsgGroupKeywords, MsgGroupCreator) "
						+ "VALUES (?, ?, ?)";
		try  {
			PreparedStatement pstmt = conn.prepareStatement(sql);
		    pstmt.setString(1, groupName);
		    pstmt.setString(2, groupKeyword);
		    pstmt.setString(3, loggedUsername);
		    pstmt.executeUpdate();
			clearScreen();
			System.out.println(groupName + " has been succsesfully created, have fun!");
			Thread.sleep(1500);
			System.out.println("Want to Add Someone to your super awesome Group? (YES/NO)");
			var ans = in.nextLine();
			while (!ans.equals("YES") && !ans.equals("NO")) {
				System.out.println("Invalid input, Please Type YES or NO");
				ans = in.nextLine();
			}
			if (ans.equals("YES")) {
				addUser(1); // give 1 as an parameter so it can know to calculate the groupID properly
			}
		 } catch (SQLException e) {
			System.err.println("Oops, Something Went Wrong While Creating the Group!");
		 } catch (InterruptedException e) {}
	}

	/**
	 * adds a user to a group, takes a number (0,1) as an parameter
	 * @param ad takes a binary number(0 or 1) so it can know how to calculate the said group (to know if the group has just been created or not)
	 */
	
	public void addUser(int ad) {
		while (true) {
			clearScreen();
			System.out.println("Please enter the username you would like "
												+ "to add to the Group.");
   			String name = in.nextLine();
			String usersselect = "SELECT Discoverable " +
										"FROM Users WHERE Username ='"+name+"'";
			try (Statement stmt  = conn.createStatement();) {
				// check if user exists
				if (checkExistingUser(name)) {
					ResultSet queryresult = stmt.executeQuery(usersselect);
					// check if the user selected has his discoverable preference as 1
					if (queryresult.getInt(1) == 1) {
						if (ad == 1) {
							// get the grouID(last group that was created)
							String sql1 = "SELECT MAX(MsgGroupID) " +
											"FROM MsgGroups " +
											"WHERE MsgGroupCreator='" + loggedUsername + "'";
							String sql2 = "INSERT INTO GroupUsersRelations (RelUsername, RelMsgGroup) "
								+ "VALUES (?, ?)";
							queryresult = stmt.executeQuery(sql1);
							int id = queryresult.getInt(1);
							PreparedStatement pstmt = conn.prepareStatement(sql2);
							pstmt.setString(1, name);
							pstmt.setInt(2, id);
							pstmt.executeUpdate();
						} else {
							Message m = (Message) this; // downgrade this object so it can access the groupID
							String sql = "INSERT INTO GroupUsersRelations (RelUsername, RelMsgGroup)  " +
											"VALUES (?, ?)";
							PreparedStatement pstmt = conn.prepareStatement(sql);
							pstmt.setString(1, name);
							pstmt.setString(2, String.valueOf(m.groupID));
							pstmt.executeUpdate();
						}
						clearScreen();
						System.out.println("User added successfully!");
						Thread.sleep(1000);
						System.out.println("Want to add another user to the same Group? (YES/NO)");
						var ans = in.nextLine();
						while (!ans.equals("YES") && !ans.equals("NO")) {
							System.out.println("Invalid input, Please Type YES or NO");
							ans = in.nextLine();
						}
						if (ans.equals("NO")) break;
					} else {
						clearScreen();
						System.out.println("This user wants to remain private and can't get added to the group, sorry");
						Thread.sleep(1000);
						System.out.println("Want to try again? (YES/NO)");
						var ans = in.nextLine();
						while (!ans.equals("YES") && !ans.equals("NO")) {
							System.out.println("Invalid input, Please Type YES or NO");
							ans = in.nextLine();
						}
						if (ans.equals("NO")) break;
					}
				} else {
					clearScreen();
					System.out.println("Username was not found!");
					Thread.sleep(1000);
					System.out.println("Want to try again? (YES/NO)");
					var ans = in.nextLine();
					while (!ans.equals("YES") && !ans.equals("NO")) {
						System.out.println("Invalid input, Please Type YES or NO");
						ans = in.nextLine();
					}
					if (ans.equals("NO")) break;
				}
			} catch (SQLException e) {
				System.err.println("Oops, something went wrong!");
				break;
			} catch (InterruptedException e) {}
		}
	}

	/**
	 *  shows all MessageGroups that the user owns/is a member of.
	 */

	public void showMsgGroups() {
        Message ms = (Message) this;
		clearScreen();
		// check if the user has any groups
		String sqltest = "SELECT COUNT(DISTINCT MsgGroupID) FROM MsgGroups, GroupUsersRelations " +
							"WHERE (MsgGroups.MsgGroupCreator='" + loggedUsername + "') OR " +
							"(MsgGroups.MsgGroupID = GroupUsersRelations.RelMsgGroup " +
							"AND GroupUsersRelations.RelUsername='" + loggedUsername + "')";
        try (Statement stmt = conn.createStatement();) {
			System.out.print("Loading groups");
			Thread.sleep(500);
			System.out.print(".");
			Thread.sleep(500);
			System.out.print(".");
			Thread.sleep(500);
			System.out.print(".");
			Thread.sleep(500);
			ResultSet rs = stmt.executeQuery(sqltest);       
			clearScreen();
			// if the user has at least 1 group, show them to the screen
			if (rs.getInt(1) > 0) {
				String sql ="SELECT Distinct MsgGroupID, MsgGroupName " +
								"FROM MsgGroups, GroupUsersRelations " +
								"WHERE (MsgGroups.MsgGroupCreator='" + loggedUsername + "') "+
								"OR (MsgGroups.MsgGroupID = GroupUsersRelations.RelMsgGroup " +
								"AND GroupUsersRelations.RelUsername='" + loggedUsername + "')";
				rs = stmt.executeQuery(sql);
				System.out.println("ID  NAME");
				while (rs.next()) {
					int id = rs.getInt("MsgGroupID");
					String name = rs.getString("MsgGroupName");
					System.out.println(id + " " + name);
				}
				System.out.println("Which message group would you like to open? Type its id");
				int groupID = in.nextInt();
				// check if the User is a member/owner of the group
				if (checkGroupUserExists(groupID)) {
					ms.showLastMessages(groupID);
				} else {
					clearScreen();
					System.out.println("No Group Found With The ID Of : " + groupID);
					Thread.sleep(1500);
				}
				rs.close();
			} else {
				System.out.println("It looks like you don't have any groups yet!");
				Thread.sleep(1000);
			}
        } catch (SQLException e) {
            System.err.println("Oops, Something Went Wrong While Loading the Groups!");
        } catch (InterruptedException e) { }
    }

	/**
	 * shows all MessageGroups of the logged in user that have new msgs, alongside said msgs
	 */
 
	public void showNewMessages() {
		clearScreen();
		// check if the user has any new messages (from all groups)
		String sqltest = "SELECT Count(DISTINCT MsgGroupID) " +
							"FROM Messages, MsgGroups, GroupUsersRelations, Users " +
							"WHERE Messages.MsgCreationTime > Users.LastLogoutTime AND " +
							"Messages.MsgGroup = MsgGroups.MsgGroupID AND " +								
							"(MsgGroups.MsgGroupCreator = '" + loggedUsername + "' OR " +			
							"(GroupUsersRelations.RelUsername = '" + loggedUsername + "' " +
							"AND GroupUsersRelations.RelMsgGroup = MsgGroups.MsgGroupID))";
		try (Statement stmt = conn.createStatement();) {
			Message ms = (Message)this;
			System.out.print("Searching For New Messages");
			Thread.sleep(500);
			System.out.print(".");
			Thread.sleep(500);
			System.out.print(".");
			Thread.sleep(500);
			System.out.print(".");
			Thread.sleep(500);
			ResultSet rs = stmt.executeQuery(sqltest);
			// if the user has at least 1 new message, show all of them to the screen with the group
			if (rs.getInt(1) > 0) {
				String sql = "SELECT DISTINCT MsgGroupID, MsgGroupName, MsgText " +
								"FROM Messages, MsgGroups, GroupUsersRelations, Users " +
								"WHERE Messages.MsgCreationTime > Users.LastLogoutTime AND " +
								"Messages.MsgGroup = MsgGroups.MsgGroupID AND " +								
								"(MsgGroups.MsgGroupCreator = '" + loggedUsername + "' OR " +			
								"(GroupUsersRelations.RelUsername = '" + loggedUsername + "' " +
								"AND GroupUsersRelations.RelMsgGroup = MsgGroups.MsgGroupID))";

				rs = stmt.executeQuery(sql);
				Thread.sleep(1000);
				while (rs.next()) {
					System.out.println(rs.getInt("MsgGroupID") + "\t" +
									rs.getString("MsgGroupname") + "\t" +
									rs.getString("MsgText"));
				}
				System.out.println("Which message group would you like to open? Type its id");
				int groupID = in.nextInt();
				// check if the User is a member/owner of the group
				if (checkGroupUserExists(groupID)) {
					ms.showLastMessages(groupID);
				} else {
					clearScreen();
					System.out.println("No Group Found With The ID Of : " + groupID);
					Thread.sleep(1500);
				}
				rs.close();
			} else {
				clearScreen();
				System.out.println("There are no New Messages Right Now, Try Again Later!");
				Thread.sleep(1000);
			}
		} catch (SQLException e) {
			System.err.println("Oops, Something Went Wrong While Loading the Groups!");
		} catch (InterruptedException e) {}
	}

	/**
	 * Checks if the user currently logged-in is a member or the owner of a group
	 * @param groupID takes as an input a Groups' ID
	 * @return a boolean value(true/false)
	*/
	public boolean checkGroupUserExists(int groupID) {
		boolean GroupUserExists = false; // assume the logged-in user is not a member/owner of said group
		String sql = "SELECT DISTINCT 1 " +
						"FROM GroupUsersRelations GU " +
						"WHERE (GU.RelMsgGroup =" + groupID + " " +
						"AND GU.RelUsername ='" + loggedUsername + "') " +
						"OR (SELECT MsgGroupCreator " +
								"FROM MsgGroups " +
								"WHERE MsgGroupID =" + groupID + ") ='" + loggedUsername + "'";
		try(Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);) {
			GroupUserExists = rs.getInt(1) > 0; // if the user is a member or an owner of the said group, put true
		} catch (SQLException e) {
			System.err.println("Oops, Something Went Wrong!");
		}
		return GroupUserExists;
	}
}
