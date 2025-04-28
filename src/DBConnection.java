import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/employeeData?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root"; // Change if needed
    private static final String PASS = "maisha2013"; // Your MySQL password

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    //  Test connection method
    public static void main(String[] args) {
        try {
            // Manually load the MySQL JDBC driver (required for Java 17+)
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection conn = getConnection();
            if (conn != null) {
                System.out.println(" Successfully connected to the employeeData database!");
                conn.close();
            }
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println( "Connection failed.");
            e.printStackTrace();
        }
    }
}
