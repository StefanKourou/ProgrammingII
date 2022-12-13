import java.sql.*;
public class msgGroups extends {

    public static void showMsgGroups(String userName) {
        Connection c = null;
        Connection c2 = null;
        Statement stmt = null;
        Message ms = new Message();
        try {
            Class.forName("org.sqlite.JBDC");
            c = DriverManager.getConnection("MsgGroups.db");
            c2 = DriverManager.getConnection("GroupUsersRelations");
            c.setAutoCommit(false);
            c2.setAutoCommit(false);
            ResultSet rs = stmt.executeQuery("SELECT MsgGroupID, MsgGroupName FROM MsgGroups, GroupUsersRelations WHERE MsgGroups.MsgGroupID = GroupUsersRelations.GroupID AND GroupUsersRelations.Username='userName'");
            while (rs.next()) {
                int id = rs.getInt("MsgGroupID");
                ms.showMessage(id) //καλώ την μέθοδο του Μανώλη//
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }
}