package datalayer;

import org.example.restaurant.datalayer.ConnectionProvider;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class TestConnectionProvider implements ConnectionProvider {

    private final String url;
    private final String user;
    private final String pass;


    public TestConnectionProvider() {
        Properties properties = new Properties();
        try {
            properties.load(Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream("test.properties"));
            Class.forName(properties.getProperty("db.driver"));
            url = properties.getProperty("db.url");
            user = properties.getProperty("db.user");
            pass = properties.getProperty("db.pass");
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, pass);
    }
}
