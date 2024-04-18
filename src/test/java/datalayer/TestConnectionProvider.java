package datalayer;

import org.example.restaurant.datalayer.ConnectionProvider;
import org.example.restaurant.datalayer.ConnectionProviderImpl;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class TestConnectionProvider implements ConnectionProvider {
    protected final DataSource dataSource;
    private static final TestConnectionProvider CONNECTION_PROVIDER = new TestConnectionProvider();

    public static TestConnectionProvider getInstance() {
        return CONNECTION_PROVIDER;
    }

    private TestConnectionProvider() {
        InitialContext ic;
        try {
            ic = new InitialContext();
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }

        try {
            dataSource = (DataSource) ic.lookup("java:comp/env/jdbc/postgres/restaurant_test");
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
