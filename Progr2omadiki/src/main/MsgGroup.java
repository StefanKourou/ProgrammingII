import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.security.cert.CertPathChecker;
import java.sql.*;

public class MsgGroup extends User {

	public MsgGroup(Connection conn) {
		super(conn);
	}

	// creates a new msg group for the logged in user
	public void createMsgGroup() {
		clearScreen();
		in.nextLine(); // clear the buffer
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

	// adds a user to said group, the ad parameter is used for the method to know how to calculate the GroupID
	public void addUser(int ad) {
		while (true) {
			clearScreen();
			System.out.println("Please enter the username you would like "
												+ "to add to the Group.");
   			String name = in.nextLine();
			String usersselect = "SELECT Discoverable " +
										"FROM Users WHERE Username ='"+name+"'";
			try (Statement stmt  = conn.createStatement();)  {
				if (checkExistingUser(name)) {
					ResultSet queryresult = stmt.executeQuery(usersselect);
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
							Message m = (Message)this; // downgrade this object so it can access the groupID
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

	// shows all msg groups of the user logged in
	public void showMsgGroups() {
        Message ms = (Message) this;
		clearScreen();
		// check if the user has any groups
		String sqltest = "SELECT COUNT(DISTINCT MsgGroupID) FROM MsgGroups, GroupUsersRelations " +
							"WHERE (MsgGroups.MsgGroupCreator='" + loggedUsername + "') OR " +
							"(MsgGroups.MsgGroupID = GroupUsersRelations.RelMsgGroup " +
							"AND GroupUsersRelations.RelUsername='" + loggedUsername + "')";
        try (Statement stmt = conn.createStatement();) {
			System.out.println("Loading groups....");
			Thread.sleep(1000);
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
				System.out.println("Which message group would you like to open? Give id");
				int groupID = in.nextInt();
				clearScreen();
				System.out.println("Opening Group...");
				Thread.sleep(1000);
				ms.showLastMessages(groupID);
				rs.close();
			} else {
				System.out.println("It looks like you don't have any groups yet!");
				Thread.sleep(1000);
			}
        } catch (SQLException e) {
            System.err.println("Oops, Something Went Wrong While Loading the Groups!");
        } catch (InterruptedException e) {}
    }

	// shows all msg groups of the logged in user that have new msgs, with the said msgs
	public void showNewMessages() {
		clearScreen();
		// check if the user has any new messages (from all groups)
		String sqltest = "SELECT COUNT(MsgGroupID) " +
							"FROM MsgGroups, GroupUsersRelations, Messages, Users " +
 							"WHERE MsgGroups.MsgGroupID = GroupUsersRelations.RelMsgGroup " +
							"AND MsgGroups.MsgGroupID = Messages.MsgGroup AND Users.Username = MsgGroups.MsgGroupCreator " +
							"AND (GroupUsersRelations.RelUsername = '" + loggedUsername + "' " +
							"OR MsgGroups.MsgGroupCreator = '" + loggedUsername + "') " +
							"AND Messages.MsgCreationTime > Users.LastLogoutTime " +
							"GROUP BY MsgGroupID " +
							"HAVING MAX(MsgID)";
		try (Statement stmt = conn.createStatement();) {
			Message ms = (Message)this;
			System.out.println("Searching For New Messages...");
			Thread.sleep(1000);
			ResultSet rs = stmt.executeQuery(sqltest);
			// if the user has at least 1 new message, show all of them to the screen with the group
			if (rs.getInt(1) > 0) {
				String sql = "SELECT MsgGroupID, MsgGroupName, MsgText, MAX(MsgID) as max " +
								"FROM MsgGroups, GroupUsersRelations, Messages, Users " +
								"WHERE MsgGroups.MsgGroupID = GroupUsersRelations.RelMsgGroup " +
								"AND MsgGroups.MsgGroupID = Messages.MsgGroup AND (Users.Username = MsgGroups.MsgGroupCreator OR Users.Username = GroupUsersRelations.RelUsername) " +
								"AND (GroupUsersRelations.RelUsername = '" + loggedUsername + "' " + 
								"OR MsgGroups.MsgGroupCreator = '" + loggedUsername + "') " +
								"AND Messages.MsgCreationTime > Users.LastLogoutTime " +
								"GROUP BY MsgGroupID " +
								"HAVING MAX(MsgID)";
				rs = stmt.executeQuery(sql);
				Thread.sleep(1000);
				while (rs.next()) {
					System.out.println(rs.getInt("MsgGroupID") + "\t" +
									rs.getString("MsgGroupname") + "\t" +
									rs.getString("MsgText") + "\t" +
									rs.getInt("max"));
				}
				System.out.println("Which message group would you like to open? Give id");
				int groupID = in.nextInt();
				clearScreen();
				System.out.println("Opening Group...");
				Thread.sleep(1000);
				ms.showLastMessages(groupID);
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
}
