
import java.util.Arrays;
import java.util.ArrayList;
import java.util.LinkedList;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
public class Logout {
	public void doGet() {
		System.out.println("Do you want to logout?");
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in)); 
		String respond1 = input.readLine();
		while (respond1.equals("YES") == false && respond1.equals("NO") == false) {
	    	System.out.println("Invalid input");
	    	System.out.println("Give your answer again");
	    	respond1 = input.readLine();
	    }
		if (respond1.equals("YES")) {
                String sql1 = "UPDATE Users SET LastLogoutTime = DATETIME('now','localtime') WHERE Username=" + loggedUsername;
                //remember the logout date
			    //go to login page
		} else {
			System.out.println("If you want to return in an online group conversation press 1, else if you want to return in an online private conversation press 2");
			String respond2 = input.readLine();
			while (respond2.equals(1) == false && respond2.equals(2) == false) {
	    		System.out.println("Invalid input");
	    		System.out.println("Give your answer again");
	    		respond2 = input.readLine();
	    	}
			if (respond2.equals(1)) {
				MsgGroups a =  new MsgGroups();
				System.out.println("Write your message");
				String respond3 = input.readLine();
				a.showMsgGroups(respond3);
			} else {
				Messages b = new Messages();
				System.out.println("Write your message");
				String respond4 = input.readLine();
				b.createMessage(respond4);
			//go to messengers page
		}
	}
	
}
