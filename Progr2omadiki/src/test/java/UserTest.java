import org.junit.jupiter.api.BeforeEach;

public class UserTest {

    @BeforeEach 
    void setUp() {
        Connection conn = Connect.connect(); // establishing connection with the DB
        Statement stm = conn.createStatement();
        String sqlstm = "INSERT INTO USERS(Username, Password, Discoverable, Email)" +
        "VALUES('testcase1', 'testpassword1', 0, 'testemail@gmail.com')"; // inserting a test-user in the DB
        ResultSet r = stm.executeQuery(sqlstm);
        assertEquals();
    }

}