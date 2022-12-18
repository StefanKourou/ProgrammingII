package core;

import java.util.Scanner;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.sql.ResultSet;
import java.sql.Statement;

public class MsgGroups extends User {
	
	public MsgGroups(Connection conn) {
		super(conn);
		// TODO Auto-generated constructor stub
	}


	Scanner input = new Scanner(System.in);
	private Connection connect() {
		String url = "jdbc:sqlite:C://sqlite/db/Progr2.db";
		Connection conn = null;
		 try {
			 conn = DriverManager.getConnection(url);
		 } catch (SQLException e) {
			 System.out.println(e.getMessage());
		 }
		 return conn;
	}

	public void createMsgGroup() {
		System.out.println("Please insert a name for your MessageGroup");
		String groupName= input.nextLine();
		System.out.println("Now insert the group's theme");
		String groupKeyword = input.nextLine();
		String sql = "INSERT INTO MsgGroups (MsgGroupName, GroupKeywords, MsgGroupCreator) "
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
		
		
	public void addUsersAfter(int id, String name) throws SQLException {
		System.out.println("Please enter the username you would like"
					 + "add to the Group.");
        	String username = input.nextLine();
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
	
	public void addUsersDirectly(String name) throws SQLException {
		System.out.println("Please enter the username you would like"
					 + "add to the Group.");
        	String username = input.nextLine();
		while (true) {	
			if (checkExistingUser(name)) { 
				String usersselect = "SELECT Discoverable FROM Users WHERE Username ='"+name+"'";
				try  {
					Statement stmt  = conn.createStatement();
					ResultSet queryresult = stmt.executeQuery(usersselect);
					if (queryresult.getInt(1) == 1) {
						String id = "SELECT MAX(MsgGroupID) 
								FROM MsgGroups 
								WHERE MsgGroupCreator='" + loggedUsername + "'"
						String sql = "INSERT INTO GroupUserRelations (RelUsername, RelMsgGroup)  "
						      + "VALUES (?, ?)";
						try {
							PreparedStatement pstmt = conn.prepareStatement(sql);
							pstmt.setString(1, username);
							pstmt.setInt(2, id);
							pstmt.executeUpdate();
							System.out.println("User added successfully!");
							System.out.println("Want to add another user to the same Group? (Y/N)");
							if (in.nextInt().equals("N")) {
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
}


