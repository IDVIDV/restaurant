package org.example.restaurant.datalayer.repositories;

import org.example.restaurant.datalayer.ConnectionProvider;
import org.example.restaurant.datalayer.entities.Position;
import org.example.restaurant.datalayer.exceptions.DataBaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PositionRepository {
    private static final String TABLE_NAME = "position";
    private static final String SELECT_ALL_QUERY = "SELECT id_position, position_name, price, weight, protein," +
            " fat, carbohydrate, vegan, ingredients FROM " + TABLE_NAME;
    private static final String SELECT_ALL_BY_NAME_QUERY = "SELECT id_position, position_name, price, weight, protein," +
            " fat, carbohydrate, vegan, ingredients FROM " + TABLE_NAME + " WHERE position_name LIKE (?)";
    private static final String SELECT_BY_ID_QUERY = "SELECT id_position, position_name, price, weight, protein," +
            " fat, carbohydrate, vegan, ingredients FROM " + TABLE_NAME + " WHERE id_position = (?)";
    private static final String ADD_QUERY = "INSERT INTO " + TABLE_NAME + " VALUES (DEFAULT,(?),(?),(?),(?),(?),(?),(?),(?))";
    private static final String UPDATE_QUERY = "UPDATE " + TABLE_NAME + " SET position_name = (?), " +
            "price = (?), weight = (?), protein = (?), fat = (?), carbohydrate = (?), vegan = (?), ingredients = (?) " +
            "WHERE id_position = (?)";
    private static final String DELETE_QUERY = "DELETE FROM " + TABLE_NAME + " WHERE id_position = (?)";
    private final ConnectionProvider connectionProvider;

    public PositionRepository(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    public List<Position> getAll() {
        List<Position> result = new ArrayList<>();

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

    public List<Position> getByName(String positionName) {
        List<Position> result = new ArrayList<>();

        try (Connection connection = connectionProvider.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SELECT_ALL_BY_NAME_QUERY);
            statement.setString(1, positionName + "%");

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                result.add(mapEntityFromResultSet(resultSet));
            }

        } catch (SQLException e) {
            throw new DataBaseException(e.getMessage());
        }

        return result;
    }

    public Position getById(Long id) {
        Position result = null;

        try (Connection connection = connectionProvider.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID_QUERY);

            statement.setObject(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                result = mapEntityFromResultSet(resultSet);
            }

        } catch (SQLException e) {
            throw new DataBaseException(e.getMessage());
        }

        return result;
    }

    public Position add(Position position) {
        try (Connection connection = connectionProvider.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(ADD_QUERY, Statement.RETURN_GENERATED_KEYS);

            prepareStatement(position, statement);

            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                position.setId(generatedKeys.getLong("id_position"));
            }

        } catch (SQLException e) {
            throw new DataBaseException(e.getMessage());
        }

        return position;
    }

    public Position update(Position position) {
        Position result = null;
        try (Connection connection = connectionProvider.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY);

            prepareStatement(position, statement);
            statement.setObject(9, position.getId());

            if (statement.executeUpdate() == 1) {
                result = position;
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

    private Position mapEntityFromResultSet(ResultSet resultSet) {
        Position position = new Position();

        try {
            position.setId(resultSet.getLong("id_position"));
            position.setPositionName(resultSet.getString("position_name"));
            position.setPrice(resultSet.getBigDecimal("price"));
            position.setWeight(resultSet.getDouble("weight"));
            position.setProtein(resultSet.getDouble("protein"));
            position.setFat(resultSet.getDouble("fat"));
            position.setCarbohydrate(resultSet.getDouble("carbohydrate"));
            position.setVegan(resultSet.getBoolean("vegan"));
            position.setIngredients(resultSet.getString("ingredients"));
        } catch (SQLException e) {
            throw new DataBaseException(e.getMessage());
        }

        return position;
    }

    private void prepareStatement(Position position, PreparedStatement statement) throws SQLException {
        statement.setObject(1, position.getPositionName());
        statement.setBigDecimal(2, position.getPrice());
        statement.setObject(3, position.getWeight());
        statement.setObject(4, position.getProtein());
        statement.setObject(5, position.getFat());
        statement.setObject(6, position.getCarbohydrate());
        statement.setObject(7, position.isVegan());
        statement.setObject(8, position.getIngredients());
    }
}
