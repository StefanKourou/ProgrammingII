import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;

public class User {

    public boolean CheckExistingUser(String name);

    public String RegisterNewUser() {
        Statement stm = conn.createStatement();
        String sql = "";
    }
    

    public void InserUserInDB(String name, String pw, String email, String login_time) {
            String sql = "INSERT INTO USERS(Username, Password, Email, LoginTime) VALUES(?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, pw);
            ps.setString(3, email);
            ps.setString(4, login_time);
            ps.executeUpdate();
    }

    
    public void InserUserInDB2(String name, String pw, String email, String login_time) {
        String sql = "INSERT OR ROLLBACK INTO USERS(Username, Password, Email, LoginTime) VALUES(?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, name);
        ps.setString(2, pw);
        ps.setString(3, email);
        ps.setString(4, login_time);
        ps.executeUpdate();
}
}
