import org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;


public class MsgGroupTest {
	Connection conn = null;
    User obj;
    java.sql.Statement stm;
    ResultSet rs;
    String trueGname;

    @BeforeEach 
    void setUp() {   
        Connection conn = Main.connect(); // establishing connection with the DB
        obj = new User(conn);   
        try {
			stm = conn.createStatement();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
        String sqlstm = "INSERT INTO MsgGroups(MsgGroupName, MsgGroupCreator, MsgGroupKeywords) VALUES('testcase1', 'testcase1', 'testcase1')"; // inserting a test-group in the DB
        try {
			stm.executeUpdate(sqlstm);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
}

    @Test 
    void createMsgGroupTest() {
        String getGname = "SELECT MsgGroupName FROM MsgGroups WHERE MsgGroupID = (SELECT MAX(MsgGroupID) FROM MsgGroups)";
        try {
			rs = stm.executeQuery(getGname);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
        try {
			trueGname = rs.getString("MsgGroupName");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
        Assertions.assertEquals("testcase1", trueGname);
    }

    @AfterEach 
    void setDown() {
        String destroy = "DELETE FROM MsgGroups WHERE MsgGroupName = testcase1";
        try {
			stm.executeUpdate(destroy);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
    }
}
