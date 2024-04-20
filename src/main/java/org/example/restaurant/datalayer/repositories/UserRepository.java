package org.example.restaurant.datalayer.repositories;

import org.example.restaurant.datalayer.ConnectionProvider;
import org.example.restaurant.datalayer.entities.User;
import org.example.restaurant.datalayer.exceptions.DataBaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private static final String TABLE_NAME = "\"user\"";
    private static final String SELECT_ALL_QUERY = "SELECT * FROM " + TABLE_NAME;
    private static final String SELECT_BY_ID_QUERY = "SELECT * FROM " + TABLE_NAME + " WHERE id_user = (?)";
    private static final String SELECT_BY_LOGIN_QUERY = "SELECT * FROM " + TABLE_NAME + " WHERE login = (?)";
    private static final String ADD_QUERY = "INSERT INTO " + TABLE_NAME + " VALUES (DEFAULT,(?),(?),(?),(?))";
    private static final String UPDATE_QUERY = "UPDATE " + TABLE_NAME + " SET login = (?), password = (?)," +
            "phone_number = (?), role = (?) WHERE id_user = (?)";
    private static final String DELETE_QUERY = "DELETE FROM " + TABLE_NAME + " WHERE id_user = (?)";
    private final ConnectionProvider connectionProvider;

    public UserRepository(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    public List<User> getAll() {
        List<User> result = new ArrayList<>();

        try (Connection connection = connectionProvider.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SELECT_ALL_QUERY);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                result.add(mapEntityFromResultSet(resultSet));
            }

        } catch (SQLException e) {
            throw new DataBaseException(e.getMessage());
        }

        return result;
    }

    public User getById(Long id) {
        User result = null;

        try (Connection connection = connectionProvider.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID_QUERY);

            statement.setLong(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                result = mapEntityFromResultSet(resultSet);
            }

        } catch (SQLException e) {
            throw new DataBaseException(e.getMessage());
        }

        return result;
    }

    public User getByLogin(String login) {
        User result = null;

        try (Connection connection = connectionProvider.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SELECT_BY_LOGIN_QUERY);

            statement.setString(1, login);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                result = mapEntityFromResultSet(resultSet);
            }

        } catch (SQLException e) {
            throw new DataBaseException(e.getMessage());
        }

        return result;
    }

    public User add(User user) {
        User result = null;

        try (Connection connection = connectionProvider.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(ADD_QUERY, Statement.RETURN_GENERATED_KEYS);

            prepareStatement(user, statement);

            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();

            if (generatedKeys.next()) {
                user.setId(generatedKeys.getLong("id_user"));
                result = user;
            }

        } catch (SQLException e) {
            throw new DataBaseException(e.getMessage());
        }

        return result;
    }

    public User update(User user) {
        User result = null;

        try (Connection connection = connectionProvider.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY);

            prepareStatement(user, statement);
            statement.setObject(5, user.getId());

            if (statement.executeUpdate() == 1) {
                result = user;
            }

        } catch (SQLException e) {
            throw new DataBaseException(e.getMessage());
        }

        return result;
    }

    public boolean delete(Long id) {
        try (Connection connection = connectionProvider.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(DELETE_QUERY);

            statement.setObject(1, id);

            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new DataBaseException(e.getMessage());
        }
    }

    private User mapEntityFromResultSet(ResultSet resultSet) {
        User user = new User();

        try {
            user.setId(resultSet.getLong("id_user"));
            user.setLogin(resultSet.getString("login"));
            user.setPassword(resultSet.getString("password"));
            user.setPhoneNumber(resultSet.getString("phone_number"));
            user.setRole(resultSet.getString("role"));
        } catch (SQLException e) {
            throw new DataBaseException(e.getMessage());
        }

        return user;
    }

    private void prepareStatement(User user, PreparedStatement statement) throws SQLException {
        statement.setObject(1, user.getLogin());
        statement.setObject(2, user.getPassword());
        statement.setObject(3, user.getPhoneNumber());
        statement.setObject(4, user.getRole());
    }
}

