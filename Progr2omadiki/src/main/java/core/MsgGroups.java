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

public class MsgGroups extends Users {
	
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

	public void createMsgGroup(String name) {
		System.out.println("Please insert a name for your MessageGroup");
		String groupName= input.nextLine();
		System.out.println("Now insert the group's theme");
		String groupKeyword = input.nextLine();
		String sql = "INSERT INTO MsgGroups (MsgGroupName, GroupKeywords, MsgGroupCreator) "
						+ "VALUES (groupName, groupKeyword, name)";
		try  { 
			PreparedStatement pstmt = conn.prepareStatement(sql);
		        pstmt.setString(1, groupName);
		        pstmt.setString(2, groupKeyword);
		        pstmt.setString(3, name);
		        pstmt.executeUpdate();
		 } catch (SQLException e) {
			System.out.println(e.getMessage());
		 }	
	}
		
		
	public void addUsers(int id, String name) throws SQLException {
		System.out.println("Please enter the username you would like"
					 + "add to the Group.");
        	String username = input.nextLine();
        	User user = new User(conn);
		if (user.checkExistingUser(username).getString("name").equals(name)) { 
			String usersselect = "SELECT Username FROM Users WHERE Discoverable==true";
			try  {
				Statement stmt  = conn.createStatement();
				ResultSet queryresult = stmt.executeQuery(usersselect);
		  		while (queryresult.next()) {
		        		System.out.println("Username:" + queryresult.getString("Username") + "\t" + 
					 	   	"Email:" +	queryresult.getString("email"));
		 		}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
				String sql = "INSERT INTO GroupUserRelations (RelUsername, RelMsgGroup)  "
					      + "VALUES (username,id)";
			try {
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, username);
				pstmt.setInt(2, id);
				pstmt.executeUpdate();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}		
			System.out.println("User added successfully!");
		} else {
	 	System.out.println("Username was not found, try again!");
		}
	}
}


