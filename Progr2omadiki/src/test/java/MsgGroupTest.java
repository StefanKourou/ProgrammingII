import java.beans.Transient;
import java.sql.ResultSet;

public class MsgGroupTest {
    User obj;
    java.sql.Statement stm;
    ResultSet rs;
    String trueGname;

    @before 
    void setUp() {   
        obj = new User(conn);   
        Connection conn = connect(); // establishing connection with the DB
        stm = conn.createStatement();
        String sqlstm = "INSERT INTO MsgGroups(MsgGroupID, MsgGroupName, MsgGroupCreator, MsgGroupKeywords) " +
        "VALUES('testcase1', 'testcase1', 'testcase1')"; // inserting a test-group in the DB
        stm.executeUpdate(sqlstm);
        assertEquals(obj.checkExistingUser("testcase1", true));
    }

    @test 
    void createMsgGroupTest() {
        String getGname = "SELECT MsgGroupName " +
                         "FROM MsgGroups " +
                         "WHERE MsgGroupID = ( " + 
                                    "SELECT MAX(MsgGroupID) " + 
                                    "FROM MsgGroups)";
        rs = stm.executeQuery(sqltest);
        trueGname = rs.getInt("MsgGroupName");
        assertEquals("testcase1", trueGname);
    }

}
