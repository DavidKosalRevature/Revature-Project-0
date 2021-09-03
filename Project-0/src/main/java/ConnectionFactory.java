import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ConnectionFactory {
    private static Connection conn = null;

    private ConnectionFactory() {

    }

    public static Connection getConnection() throws SQLException {
        if (conn == null) {
            ResourceBundle bundle = ResourceBundle.getBundle("dbConfig");
            String url = bundle.getString("url");
            String username = bundle.getString("username");
            String password = bundle.getString("password");
            conn = DriverManager.getConnection(url, username, password);
        }

        return conn;
    }
}
