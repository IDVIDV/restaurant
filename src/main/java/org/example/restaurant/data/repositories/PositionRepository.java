package org.example.restaurant.data.repositories;

import org.example.restaurant.data.ConnectionProvider;
import org.example.restaurant.data.entities.Position;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PositionRepository {

    private final String tableName;
    private final ConnectionProvider connectionProvider;

    public PositionRepository(String tableName, ConnectionProvider connectionProvider) {
        this.tableName = tableName;
        this.connectionProvider = connectionProvider;
    }

    protected Map<String, String> getColumnValuesMap(Position position) {
        return Map.of(
                "position_name", RepositoryUtils.getValueInQuotes(position.getPositionName()),
                "price", position.getPrice().toString(),
                "weight", Double.toString(position.getWeight()),
                "protein", Double.toString(position.getProtein()),
                "fat", Double.toString(position.getFat()),
                "carbohydrate", Double.toString(position.getCarbohydrate()),
                "vegan", Boolean.toString(position.isVegan()),
                "ingredients", RepositoryUtils.getValueInQuotes(position.getIngredients())
        );
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

    public List<Position> getAll() {
        List<Position> result = new ArrayList<>();

        try (Connection connection = connectionProvider.getConnection()) {
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

    public Position getById(long id) {
        Position result = null;

        try (Connection connection = connectionProvider.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    RepositoryUtils.buildGetByIdQuery(id, tableName, "id_position")
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

    public void add(Position position) {
        try (Connection connection = connectionProvider.getConnection()) {
            Statement statement = connection.createStatement();
            statement.executeUpdate(RepositoryUtils.buildAddQuery(tableName, getColumnValuesMap(position)));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Position position) {
        try (Connection connection = connectionProvider.getConnection()) {
            Statement statement = connection.createStatement();
            statement.executeUpdate(RepositoryUtils.buildUpdateQuery(position.getId(), tableName,
                    "id_position", getColumnValuesMap(position)));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(long id) {
        try (Connection connection = connectionProvider.getConnection()) {
            Statement statement = connection.createStatement();
            statement.executeUpdate(RepositoryUtils.buildDeleteQuery(id, tableName, "id_position"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
