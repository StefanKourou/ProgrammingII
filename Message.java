public class Message {

	private Connection connect() {
	        // SQLite connection string
	        String url = "jdbc:sqlite:C://sqlite/db/Progr2.db";  //Connection to database
	        Connection conn = null;
	        try {
	            conn = DriverManager.getConnection(url);
	        } catch (SQLException e) {
	            System.out.println(e.getMessage());
	        }
	        return conn;
    	}

	public void showMessage() {
	        String msgGroups =
	        "SELECT MsgText, MsgUsername;
			FROM Messages;
			WHERE GroupID=2 //EDW THA PAIRNOUME INPUT APO TH MSGGROUPS!!!;
			ORDER BY MsgCreationTime ASC;
			LIMIT 10;
		";

	        try (Connection conn = this.connect();
	             Statement stmt  = conn.createStatement();
	             ResultSet queryresult = stmt.executeQuery(usersselect)){

	        // loop through the result set
	        while (queryresult.next()) {
	        	System.out.println(queryresult.getString("MsgText") + "\t" +
	            			   queryresult.getString("MsgUsername"));
	            }
	        } catch (SQLException e) {
	        	System.out.println(e.getMessage());
	        }
    	}

    	public void createMessage() {
		        String newMessage =
		        "INSERT INTO Messages (
		        Username, GroupID, MsgText, MsgCreationTime)  //USERNAME APO TH LOGIN GROUPID APO TH MESSAGE GROUP
				VALUES (
				‘Spinelis’, 2, ‘για να δουμε πως φτιαχνεται’, DATETIME('now','localtime'))
				";

		        try (Connection conn = this.connect();
		             Statement stmt  = conn.createStatement();
		             ResultSet queryresult = stmt.executeQuery(usersselect)){

		        // loop through the result set
		        while (queryresult.next()) {
		        	System.out.println(queryresult.getString("Username") + "\t" +
		            			   queryresult.getString("GroupID") + "\t" +
		            			   queryresult.getBoolean("MsgText") + "\t" +
		            			   queryresult.getString("MsgCreationTime"));
		            }
		        } catch (SQLException e) {
		            System.out.println(e.getMessage());
		        }
		        //query like showMessage
    	}

    	public void reactionsMessage() {
		String newReaction =
		"INSERT INTO Reactions (MsgID, Username, Reaction)
		 VALUES (10, ‘testuser3’, 1)              //same me panw
		";

		try (Connection conn = this.connect();
			Statement stmt  = conn.createStatement();
			ResultSet queryresult = stmt.executeQuery(usersselect)){

			// loop through the result set
			while (queryresult.next()) {
			        System.out.println(queryresult.getString("MsgID") + "\t" +
			            		   queryresult.getString("Username") + "\t" +
			            		   queryresult.getBoolean("Reaction"));
			  	}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
			//query like showMessage
    	}

    	public static void main(String[] args) {
	    	Message msgGroups = new Message();
	        msgGroups.showMessage();
	        Message newMessage = new Message();
	        newMessage.createMessage();
	        Message reactionMessage = new Message();
	        newReaction.reactionMessage();
    	}
    }




