import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    public static void main(String[] args) throws ClassNotFoundException {
        try {
            Connection database = DriverManager.getConnection("jdbc:postgresql://localhost:5432/socket");
            System.out.println("Opened database successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
