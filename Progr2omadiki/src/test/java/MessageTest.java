import org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;


public class MessageTest {
    User obj;
    java.sql.Statement stm;
    ResultSet rs;
    int gID;

    @BeforeEach 
    void setUp() {     
        Connection conn = Main.connect(); // establishing connection with the DB
        obj = new User(conn); 
        try {
			stm = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
        String sqlstm = "INSERT INTO MsgGroups(MsgGroupName, MsgGroupCreator, MsgGroupKeywords) VALUES('testcase1', 'testcase1', 'testcase1')"; // inserting a test-group in the DB
        try {
			stm.executeUpdate(sqlstm);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
        String getGroupID = "SELECT MAX(MsgGroupID) FROM MsgGroups";
        try {
			rs = stm.executeQuery(getGroupID);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
        try {
			gID = rs.getInt(1);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
        String createMsg = "INSERT INTO Messages(MsgUserName, MsgGroup, MsgText) VALUES ('testcase2', " + gID + ", 'txtest')";
        try {
			stm.executeUpdate(createMsg);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
    }

    @Test 
    void createMessageTest() {
        String lastTxt = "SELECT MsgText FROM Messages WHERE MsgID = (SELECT MAX(MsgID) FROM Messages)";
        try {
			rs = stm.executeQuery(lastTxt);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
        String text = null;
		try {
			text = rs.getString(1);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
        Assertions.assertEquals("txtest", text);
    }
}
