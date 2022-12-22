msgGroups.java
import java.sql.*;
public class msgGroups extends {

    public static void showMsgGroups() {
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
                String name = rs.getString("MsgGroupName");
                System.out.println(id + " " + name);
            }
            System.out.println("Which message group would you like to open? Give id");
            Scanner sc = new Scanner(System.in);
            int groupID = sc.nextInt();
            ms.showLastMessages(groupID);
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }
}
