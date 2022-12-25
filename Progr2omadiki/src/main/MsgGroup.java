import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.sql.*;

public class MsgGroup extends User {


	public MsgGroup(Connection conn) {
		super(conn);
	}

	public void createMsgGroup() {
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
		 } catch (SQLException e) {
			System.out.println(e.getMessage());
		 }	
	}
		
		
	public void addUsersAfter(int id, String name) {
		System.out.println("Please enter the username you would like"
					 + "add to the Group.");
        	String username = in.nextLine();
		if (checkExistingUser(name)) { 
			String usersselect = "SELECT Discoverable FROM Users WHERE Username ='"+name+"'";
			try  {
				Statement stmt  = conn.createStatement();
				ResultSet queryresult = stmt.executeQuery(usersselect);
				if (queryresult.getInt(1) == 1) {
					String sql = "INSERT INTO GroupUserRelations (RelUsername, RelMsgGroup)  "
					      + "VALUES (?, ?)";
					try {
						PreparedStatement pstmt = conn.prepareStatement(sql);
						pstmt.setString(1, username);
						pstmt.setInt(2, id);
						pstmt.executeUpdate();
						System.out.println("User added successfully!");
					} catch (SQLException e) {
						System.err.println("Oops, somethign went wrong when adding the user!");
					}
				} else {
					System.err.println("The user wants to remain private and can't get added to the group, sorry");
				}
			} catch (SQLException e) {
				System.err.println("Oops, something went wrong!");
			}
		} else {
	 		System.out.println("Username was not found, try again!");
		}
	}
	
	public void addUsersDirectly(String name) {
		System.out.println("Please enter the username you would like"
					 + "add to the Group.");
        	String username = in.nextLine();
		while (true) {
			if (checkExistingUser(name)) { 
				String usersselect = "SELECT Discoverable FROM Users WHERE Username ='"+name+"'";
				try  {
					Statement stmt  = conn.createStatement();
					ResultSet queryresult = stmt.executeQuery(usersselect);
					if (queryresult.getInt(1) == 1) {
						String sql1 = "SELECT MAX(MsgGroupID)" + 
								"FROM MsgGroups" +
								"WHERE MsgGroupCreator='" + loggedUsername + "'";
						String sql2 = "INSERT INTO GroupUserRelations (RelUsername, RelMsgGroup)  "
						      + "VALUES (?, ?)";
						try {
							queryresult = stmt.executeQuery(sql1);
							int id = queryresult.getInt(1);
							PreparedStatement pstmt = conn.prepareStatement(sql2);
							pstmt.setString(1, username);
							pstmt.setInt(2, id);
							pstmt.executeUpdate();
							System.out.println("User added successfully!");
							System.out.println("Want to add another user to the same Group? (Y/N)");
							if (in.nextLine().equals("N")) {
								break;
							}
						} catch (SQLException e) {
							System.err.println("Oops, somethign went wrong when adding the user!");
						}
					} else {
						System.err.println("The user wants to remain private and can't get added to the group, sorry");
					}
				} catch (SQLException e) {
					System.err.println("Oops, something went wrong!");
				}
			} else {
				System.out.println("Username was not found, try again!");
			}
		}
	}
	
	public void showMsgGroups() {
        ResultSet rs = null;
        Message ms = new Message(conn);
		// check if the user has any groups
		String sqltest = "SELECT COUNT(DISTINCT MsgGroupID)" +
		"FROM MsgGroups, GroupUsersRelations" +
		"WHERE (MsgGroups.MsgGroupCreator='" + loggedUsername + "')" + 
		"OR (MsgGroups.MsgGroupID = GroupUsersRelations.RelMsgGroup" + 
		"AND GroupUsersRelations.RelUsername='" + loggedUsername + "')"; 
				// PROVLIMA SQL error or missing database (near "OR": syntax error)
        try {
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(sqltest);
			// if the user has at least 1 group, show them to the screen
			if (rs.getInt(1) > 0) {
				String sql ="SELECT Distinct MsgGroupID, MsgGroupName " +
				"FROM MsgGroups, GroupUsersRelations" +
				"WHERE (MsgGroups.MsgGroupCreator='" + loggedUsername + "')" + 
				"OR (MsgGroups.MsgGroupID = GroupUsersRelations.RelMsgGroup" + 
				"AND GroupUsersRelations.RelUsername='" + loggedUsername + "')";
				rs = stmt.executeQuery(sql);
				while (rs.next()) {
					int id = rs.getInt("MsgGroupID");
					String name = rs.getString("MsgGroupName");
					System.out.println(id + " " + name);
				}
				System.out.println("Which message group would you like to open? Give id");
				int groupID = in.nextInt();
				ms.showLastMessages(groupID);
				rs.close();
				stmt.close();
			} else {
				System.out.println("It looks like you don't have any groups yet!");
			}   
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}

