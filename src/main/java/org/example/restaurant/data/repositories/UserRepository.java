package org.example.restaurant.data.repositories;

import org.example.restaurant.data.ConnectionFactory;
import org.example.restaurant.data.entities.Table;
import org.example.restaurant.data.entities.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserRepository {

    private final String tableName;
    private final ConnectionFactory connectionFactory;

    public UserRepository(String tableName, ConnectionFactory connectionFactory) {
        this.tableName = tableName;
        this.connectionFactory = connectionFactory;
    }

    protected Map<String, String> getColumnValuesMap(User user) {
        return Map.of(
                "login", RepositoryUtils.getValueInQuotes(user.getLogin()),
                "password", user.getPassword(),
                "phone_number", user.getPhoneNumber(),
                "role", user.getRole()
        );
    }

    protected User mapEntityFromResultSet(ResultSet resultSet) {
        User user = new User();

        try {
            user.setId(resultSet.getLong("id_user"));
            user.setLogin(resultSet.getString("login"));
            user.setPassword(resultSet.getString("password"));
            user.setPhoneNumber(resultSet.getString("phone_number"));
            user.setRole(resultSet.getString("role"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return user;
    }

    public List<User> getAll() {
        List<User> result = new ArrayList<>();

        try (Connection connection = connectionFactory.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(RepositoryUtils.buildGetAllQuery(tableName));

            while (resultSet.next()) {
                result.add(mapEntityFromResultSet(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    public User getById(long id) {
        User result = null;

        try (Connection connection = connectionFactory.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    RepositoryUtils.buildGetByIdQuery(id, tableName, "id_user")
            );

            if (resultSet.next()) {
                result = mapEntityFromResultSet(resultSet);
            }
            //TODO: добавить обработку случая, когда нет сущности

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    public void add(User user) {
        try (Connection connection = connectionFactory.getConnection()) {
            Statement statement = connection.createStatement();
            statement.executeUpdate(RepositoryUtils.buildAddQuery(tableName, getColumnValuesMap(user)));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(User user) {
        try (Connection connection = connectionFactory.getConnection()) {
            Statement statement = connection.createStatement();
            statement.executeUpdate(RepositoryUtils.buildUpdateQuery(user.getId(), tableName,
                    "id_user", getColumnValuesMap(user)));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(long id) {
        try (Connection connection = connectionFactory.getConnection()) {
            Statement statement = connection.createStatement();
            statement.executeUpdate(RepositoryUtils.buildDeleteQuery(id, tableName, "id_user"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

