package banking;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    public static Connection getConnection() {

        Connection con = null;

        try {
            String url = "jdbc:mysql://localhost:3306/BANK";
            String user = "root";
            String pass = "Tejakumar12345";

            con = DriverManager.getConnection(url, user, pass);

        } catch (Exception e) {
            System.out.println("Connection Error");
        }

        return con;
    }
}