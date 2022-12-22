package core;

public class Messages {
	Scanner in;
	//Pairnw to connection apo ton hlia
	public void showLastMessages(int GroupID) {
	        String msgGroups =
	        "SELECT MsgText, MsgUsername
			FROM Messages
			WHERE GroupID="+GroupID" //EDW THA PAIRNOUME INPUT APO TH MSGGROUPS!!!   <<<< TSAKALOS GRAFEI TO MAX
			ORDER BY MsgCreationTime ASC
			LIMIT 10
		";

	        try (Connection conn = this.connect(); // Hlias: tha exeis to conn obj apo thn Connect class, den xreiazetai na to dhmiourgeis 
	        	Statement stmt  = conn.createStatement();
	                ResultSet queryresult = stmt.executeQuery(msgGroups)) {

	      		// loop through the result set
	       		while (queryresult.next()) {
	        	System.out.println(queryresult.getString("MsgText") + "\t" +
	            			   queryresult.getString("MsgUsername"));
	                }
	        } catch (SQLException e) {
	        	System.out.println("There are no new messages. Try again later!");
	        }
    	}

    	public void createMessage(String msgText) {
		String newMessage =
			"INSERT INTO Messages (
		        Username, GroupID, MsgText, MsgCreationTime)  //USERNAME APO TH LOGIN GROUPID APO TH MESSAGE GROUP
				VALUES (			
				‘Spinelis’, 2, " + msgText + ", DATETIME('now','localtime')) 
				";

		try (Connection conn = this.connect(); //
		     Statement stmt  = conn.createStatement();
		     ResultSet queryresult = stmt.executeQuery(newMessage)) {

		        // loop through the result set
		        while (queryresult.next()) {
		        	System.out.println(queryresult.getString("MsgUsername") + "\t" +
		            			   queryresult.getString("MsgGroup") + "\t" +
		            			   queryresult.getBoolean("MsgText") + "\t" +
		            			   queryresult.getString("MsgCreationTime"));
		        }
		} catch (SQLException e) {
			System.out.println("Oops! Something went wrong!");
		}
		//query like showMessage
    	}
	
	public void showMessageInfo(int id) {
	 String msgInfo = "SELECT MsgUsername, MsgGroup, MsgText, MsgCreationTime FROM Messages where MsgID ="+id;
        
        try (Connection conn = this.connect(); // 
             Statement stmt  = conn.createStatement();
             ResultSet queryresult = stmt.executeQuery(msgInfo)){
            
            // loop through the result set
            while (queryresult.next()) {
            	System.out.println(queryresult.getString("MsgUsername") + "\t" + 
            			queryresult.getString("MsgGroup") + "\t" +
            			queryresult.getBoolean("MsgText") + "\t" +
            			queryresult.getString("MsgCreationTime");
				System.out.println("Do you wish to react to this message?");
				System.out.println("1: like, 2: dislike, 3: heart, or 4: no");
				in = new Scanner(System.in);
				int re = in.nextInt();
				if (re != 4) reactionsMessage(id, re);
            }
        } catch (SQLException e) {
            System.out.println("Oops! Something went wrong!");
        }
    }
	
	}
		

    	public void reactionsMessage(int id, int re) {
		String newReaction =
		"INSERT INTO Reactions (ReMsg, ReUsername, Reaction)
		 VALUES (" + id + ", ‘loginUsername’," + re + ")"; // sto 'loginUsername' tha einai o xrhsths poy einai logged in

		try (Connection conn = this.connect();
			Statement stmt  = conn.createStatement();
			ResultSet queryresult = stmt.executeQuery(newReaction)){

			// loop through the result set
			while (queryresult.next()) {
			        System.out.println(queryresult.getString("MsgID") + "\t" +
			            		   queryresult.getString("Username") + "\t" +
			            		   queryresult.getBoolean("Reaction"));
			  	}
			} catch (SQLException e) {
				System.out.println("Oops! Something went wrong!");
			}
			//query like showMessage
    	}

    	public static void main(String[] args) {           //theloume 3 main? // OXI, TI ENNOEIS
	    	Message msgGroups = new Message();
	        msgGroups.showMessage();
	        Message newMessage = new Message();
	        newMessage.createMessage();
	        Message reactionMessage = new Message();
	        newReaction.reactionMessage();

    	}
    }
