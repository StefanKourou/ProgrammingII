import org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;

public class UserTest {
Connection conn = null;
    @BeforeEach 
    void setUp() {
        try {
            Connection conn = Main.connect(); // establishing connection with the DB         
            this.conn = conn;
            PreparedStatement stm = conn.prepareStatement("INSERT INTO Users(Username, Password, Discoverable, Email) VALUES (?, ?, ?, ?)"); // inserting a test-user in the DB
            stm.setString(1, "testcase1");
            stm.setString(2, "testpassword1");
            stm.setInt(3, 0);
            stm.setString(4, "testemail@gmail.com");
            stm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Something went wrong!");
        }
    }
    @Test
    void testCheckExistinUser() {
        User obj = new User(conn);
        Assertions.assertTrue(obj.checkExistingUser("testcase1"), "Failure! method does not return expected results.");  
    }

    @AfterEach
    void setDown() {
        User obj = new User(conn);
        ResultSet r = null;
        try {
            String string1 = "DELETE FROM Users WHERE Username = 'testcase1'";
        	java.sql.Statement stm = conn.createStatement();
            stm.executeUpdate(string1);
        } catch (SQLException e) {
            System.out.println("Something went wrong!");
        }
    }
}
