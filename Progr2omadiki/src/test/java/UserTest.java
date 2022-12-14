import static org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.sql.Connection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

public class UserTest {
/*Conection conn = null;
    @BeforeEach 
    void setUp() {
        try {
            Connection conn = Connect.connect(); // establishing connection with the DB         
            this.conn = conn;
            PreparedStatement stm = conn.prepareStatement("INSERT INTO USERS(Username, Password, Discoverable, Email)" +
            "VALUES(?, ?, ?, ?)";); // inserting a test-user in the DB
            stm.setString(1, 'testcase1');
            stm.setString(2, 'testpassword1');
            stm.setString(3, 0);
            stm.setString(4, 'testemail@gmail.com'));   
        } catch (SQLException e) {
            System.out.println("Oops. Something went wrong.");
        }
    }
    @Test
    void testCheckExistinUser() {
        User obj = new User(conn);
        assertTrue(obj.checkExistingUser("testcase1"), "Failure! method does not return expected results.");
    }

    @AfterEach
    void setDown() {
        User obj = new User(conn);
        ResultSet r = null;
        try {
            PreparedStatement stm = conn.prepareStatement("DELETE FROM USERS WHERE Username = (?))");
            stm.setString(1,"testcase1");
        } catch (SQLException e) {
            System.out.println("Oops. Something went wrong.");
        }
    }
*/}