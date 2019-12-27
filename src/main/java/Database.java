import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    public static void main(String[] args) throws ClassNotFoundException {
        try {
            Connection database = DriverManager.getConnection("jdbc:postgresql://localhost:5432/socket");
            Statement statement = database.createStatement();
            String createTableSchema = "CREATE TABLE CUSTOMER " +
                    "(ID SERIAL PRIMARY KEY NOT NULL," +
                    "NAME VARCHAR(10) NOT NULL," +
                    "STATUS INT DEFAULT 1 NOT NULL)";

            String insertFirstRowSchema = "INSERT INTO CUSTOMER(NAME, STATUS) VALUES('PREETI', 0)";

//            statement.executeUpdate(createTableSchema);
            statement.executeUpdate(insertFirstRowSchema);
            statement.close();
            database.close();
            System.out.println("Opened database successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
