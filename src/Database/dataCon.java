package Database;

import java.sql.*;
/**

public class dataCon {

    private static final String con = "jdbc:sqlite:CommunityService.db";

    public static Connection getConnection()throws SQLException {

        try{

            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection(con);
            return c;
        }
        catch(SQLException | ClassNotFoundException e) {

            System.err.println("Error " + e);
        }
        return null;
    }

}
*/