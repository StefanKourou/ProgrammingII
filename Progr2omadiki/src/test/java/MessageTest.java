import java.sql.ResultSet;

public class MessageTest {
    User obj;
    java.sql.Statement stm;
    ResultSet rs;
    int gID;

    @before 
    void setUp() {     
        Connection conn = Main.connect(); // establishing connection with the DB
        obj = new User(conn); 
        stm = conn.createStatement();
        String sqlstm = "INSERT INTO MsgGroups(MsgGroupName, MsgGroupCreator, MsgGroupKeywords) " +
        "VALUES('testcase1', 'testcase1', 'testcase1')"; // inserting a test-group in the DB
        stm.executeUpdate(sqlstm);
        String getGroupID = "SELECT MAX(MsgGroupID) " +
                            "FROM MsgGroups";
        rs = stm.exexuteQuery(getGroupID);
        gID = rs.getInt(1);
        String createMsg = "INSERT INTO Messages(MsgUserName, MsgGroup, MsgText) VALUES ('testcase2', " + gID + ", 'txtest')";
        stm.executeUpdate(createMsg);
    }

    @test 
    void createMessageTest() {
        String lastTxt = "SELECT MsgText " +
                        "FROM Messages " + 
                        "WHERE MsgID = ( " + 
                        "SELECT MAX(MsgID) " +
                        "FROM Messages)";
        rs = stm.executeQuery(lastTxt);
        int text = rs.getInt(1);
        Assertions.assertEquals(text, "txtest");
    }
}