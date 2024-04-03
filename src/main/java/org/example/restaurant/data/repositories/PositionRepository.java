package org.example.restaurant.data.repositories;

import org.example.restaurant.data.ConnectionProvider;
import org.example.restaurant.data.entities.Position;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PositionRepository {
    private static final String TABLE_NAME = "position";
    private static final String SELECT_ALL_QUERY = "SELECT * FROM " + TABLE_NAME;
    private static final String SELECT_BY_ID_QUERY = "SELECT * FROM " + TABLE_NAME + " WHERE id_position = (?)";
    private static final String ADD_QUERY = "INSERT INTO " + TABLE_NAME + " VALUES (DEFAULT,(?),(?),(?),(?),(?),(?),(?),(?))";
    private static final String UPDATE_QUERY = "UPDATE " + TABLE_NAME + " SET position_name = (?), " +
            "price = (?), weight = (?), protein = (?), fat = (?), carbohydrate = (?), vegan = (?), ingredients = (?) " +
            "WHERE id_position = (?)";
    private static final String DELETE_QUERY = "DELETE FROM " + TABLE_NAME + " WHERE id_position = (?)";
    private final ConnectionProvider connectionProvider;

    public PositionRepository(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    protected Position mapEntityFromResultSet(ResultSet resultSet) {
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
            throw new RuntimeException(e);
        }

        return position;
    }

    protected void prepareStatement(Position position, PreparedStatement statement) throws SQLException {
        statement.setString(1, position.getPositionName());
        statement.setBigDecimal(2, position.getPrice());
        statement.setDouble(3, position.getWeight());
        statement.setDouble(4, position.getProtein());
        statement.setDouble(5, position.getFat());
        statement.setDouble(6, position.getCarbohydrate());
        statement.setBoolean(7, position.isVegan());
        statement.setString(8, position.getIngredients());
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
            throw new RuntimeException(e);
        }

        return result;
    }

    public Position getById(long id) {
        Position result = null;

        try (Connection connection = connectionProvider.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID_QUERY);

            statement.setLong(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                result = mapEntityFromResultSet(resultSet);
            }
            //TODO: добавить обработку случая, когда нет сущности

        } catch (SQLException e) {
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
        }

        return position;
    }

    public Position update(Position position) {
        try (Connection connection = connectionProvider.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY);

            prepareStatement(position, statement);
            statement.setLong(9, position.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return position;
    }

    public boolean delete(long id) {
        try (Connection connection = connectionProvider.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(DELETE_QUERY);

            statement.setLong(1, id);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
