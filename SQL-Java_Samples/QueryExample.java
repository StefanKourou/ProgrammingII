
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class QueryExample {

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

    
    /**
     * select all rows in the warehouses table
     */
    public void selectAll(){
        String usersselect = "SELECT Username, Password, Discoverable, Email, LoginTime FROM Users";
        
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet queryresult = stmt.executeQuery(usersselect)){
            
            // loop through the result set
            while (queryresult.next()) {
            	System.out.println(queryresult.getString("Username") + "\t" + 
            			queryresult.getString("Password") + "\t" +
            			queryresult.getBoolean("Discoverable") + "\t" +
            			queryresult.getString("Email") + "\t" +
            			queryresult.getTimestamp("LoginTime"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }



    public static void main(String[] args) {
    	QueryExample app = new QueryExample();
        app.selectAll();
    }

}
