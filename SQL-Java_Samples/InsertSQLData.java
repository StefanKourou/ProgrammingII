import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class InsertSQLData {

    private Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:C://sqlite/db/Progr2.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }


    public void insert(String Username, String Password, boolean Discoverable, String Email) {
        String insertrec = "INSERT INTO Users(Username,Password,Discoverable,Email,LoginTime) VALUES(?,?,?,?,?)";

        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(insertrec)) {
        	
        	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");
        	String now = LocalDateTime.now().format(dtf).toString();
        	        	
        	pstmt.setString(1, Username);
            pstmt.setString(2, Password);
            pstmt.setBoolean(3, Discoverable);
            pstmt.setString(4, Email);
            pstmt.setString(5, now);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    
    public static void main(String[] args) {

        InsertData app = new InsertData();
        app.insert("George1", "Pa$$1", true, "geo1@minfin.gr");
        app.insert("George2", "Pa$$2", false, "geo2@minfin.gr");
    }

}
