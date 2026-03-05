package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Connector {
    public static Connection getConnector() throws SQLException {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("people");
        String url = resourceBundle.getString("url");
        String user = resourceBundle.getString("user");
        String password = resourceBundle.getString("password");
        return DriverManager.getConnection(url, user, password);
    }
}
