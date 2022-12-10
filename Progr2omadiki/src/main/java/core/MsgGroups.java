package core;

import java.util.Scanner;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;

public class MsgGroups extends Users {
 public MsgGroups(Connection conn) {
		super(conn);
	}

Scanner input = new Scanner(System.in);

public void createMsgGroup() {
		System.out.println("Please insert a name for your MessageGroup");
		String groupName= this.input.nextLine();
		System.out.println("Now insert the group's theme with a few keywords");
		String groupKeywords = this.input.nextLine();
		String groupCreator = Users.loggedUser;  // εδω παει η μεταβλητη του loggeduser απο τα παιδια//  
		String sql = "INSERT INTO MsgGroups (MsgGroupName, MsgGroupKeywords, MsgGroupCreator) VALUES (?, ?, ?)";
		 try  { 
			 PreparedStatement pstmt = this.conn.prepareStatement(sql);
		        pstmt.setString(1, groupName);
		        pstmt.setString(2, groupKeywords);
		        pstmt.setString(3, groupCreator);
		        pstmt.executeUpdate();
		 } catch (SQLException e) {
			System.out.println(e.getMessage());
		}	
	}


	public void addUsers(int id, String name) 
		
			throws SQLException {
		System.out.println("Please enter the username you would like to add to the Group.");
		
		String username = this.input.nextLine();
		Users user = new Users(this.conn);
	 	if (user.checkExistingUser(username).getString("name").equals(name)) { 
	 	 String usersselect = "SELECT Username FROM Users WHERE Discoverable=1";
		 try  {
			 Statement stmt  = this.conn.createStatement();
		     ResultSet queryresult = stmt.executeQuery(usersselect);
			while (queryresult.next()) {
				System.out.println("Username:" + queryresult.getString("Username") + "\t" + 
					 	"Email:" +	queryresult.getString("email"));
			}
			 } catch (SQLException e) {
		            System.out.println(e.getMessage());
			 }
			 String sql = "INSERT INTO GroupUserRelations (RelUsername, RelMsgGroup) VALUES (?, ?)";
			 try {
				 PreparedStatement pstmt = this.conn.prepareStatement(sql);
				 pstmt.setString(1, username);
				 pstmt.setInt(2, id);
				 pstmt.executeUpdate();
			 } catch (SQLException e) {
			         System.out.println(e.getMessage());
			 }		
		 	 System.out.println("User added successfully!");
	 		} else {
			 System.out.println("Username was not found or doesn't want to be found... try again!");
			 }
		}
}
