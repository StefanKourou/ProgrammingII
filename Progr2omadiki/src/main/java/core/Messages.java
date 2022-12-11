package core;

public class Messages {
	
	//Pairnw to connection apo ton hlia

	public void showMessage() {
	        String msgGroups =
	        "SELECT MsgText, MsgUsername 
			FROM Messages 
			WHERE GroupID=2 //EDW THA PAIRNOUME INPUT APO TH MSGGROUPS!!!
			ORDER BY MsgCreationTime ASC 
			LIMIT 10;"

	        try (Connection conn = this.connect(); // Hlias: tha exeis to conn obj apo thn Connect class, den xreiazetai na to dhmiourgeis 
	             Statement stmt  = conn.createStatement();
	             ResultSet queryresult = stmt.executeQuery(msgGroups)){

	        // loop through the result set
	        while (queryresult.next()) {
	        	System.out.println(queryresult.getString("MsgText") + "\t" +
	        			queryresult.getString("MsgID") + "\t" +
	        			queryresult.getString("MsgCreationTime") + "\t" +
	        			queryresult.getString("MsgUsername"));
	            }
	        } catch (SQLException e) {
	        	System.out.println(e.getMessage());
	        }
    	}

    	public void createMessage() {
		        String newMessage =
		        "INSERT INTO Messages (MsgUsername, MsgGroup, MsgText, MsgCreationTime) // USERNAME APO TH LOGIN GROUPID APO TH MESSAGE GROUP \\
				VALUES (			// Hlias: prepei na pairines os orisma to MsgText
				‘Spinelis’, 2, ‘για να δουμε πως φτιαχνεται’, DATETIME('now','localtime'));" // pithanotata xrhsimopoihsh ths getTime()

		        try (Connection conn = this.connect();
		             Statement stmt  = conn.createStatement();
		             ResultSet queryresult = stmt.executeQuery(newMessage)){

		        // loop through the result set
		        while (queryresult.next()) {
		        	System.out.println(queryresult.getString("MsgUsername") + "\t" +
		            			   queryresult.getBoolean("MsgText") + "\t";
		            }
		        } catch (SQLException e) {
		            System.out.println(e.getMessage());
		        }
		        //query like showMessage
    	}

    	public void reactionsMessage() {
		String newReaction =
		"INSERT INTO Reactions (ReMsg, ReUsername, Reaction)
		 VALUES (10, ‘testuser3’, 1)              //same me panw
		";

		try (Connection conn = this.connect();
			Statement stmt  = conn.createStatement();
			ResultSet queryresult = stmt.executeQuery(newReaction)){

			// loop through the result set
			while (queryresult.next()) {
			        System.out.println(queryresult.getString("ReMsg") + "\t" +
			            		   queryresult.getString("ReUsername") + "\t" +
			            		   queryresult.getBoolean("Reaction"));
			  	}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
			//query like showMessage
    	}

    	public static void main(String[] args) {           //theloume 3 main? // OXI, TI ENNOEIS
	    	Messages msgGroups = new Messages();
	        msgGroups.showMessage();
	        Messages newMessage = new Messages();
	        newMessage.createMessage();
	        Messages newReaction = new Messages();
	        newReaction.reactionsMessage();
    	}
    }
