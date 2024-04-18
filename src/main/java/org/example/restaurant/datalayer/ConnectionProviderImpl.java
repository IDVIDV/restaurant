package org.example.restaurant.datalayer;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionProviderImpl implements ConnectionProvider {
    protected final DataSource dataSource;
    private static final ConnectionProviderImpl CONNECTION_PROVIDER = new ConnectionProviderImpl();

    public static ConnectionProviderImpl getInstance() {
        return CONNECTION_PROVIDER;
    }

    private ConnectionProviderImpl() {
        InitialContext ic;
        try {
            ic = new InitialContext();
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }

        try {
            dataSource = (DataSource) ic.lookup("java:comp/env/jdbc/postgres/restaurant");
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
