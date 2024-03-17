package org.example.restaurant.data.repositories;

import org.example.restaurant.data.ConnectionFactory;
import org.example.restaurant.data.entities.Entity;
import org.example.restaurant.data.entities.Position;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PositionRepository extends Repository {


    public PositionRepository() {
        super("position", new ConnectionFactory());
    }

    @Override
    protected Entity mapEntityFromResultSet(ResultSet resultSet) {
        Position position = new Position();

        try {
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

    @Override
    public List<Entity> getAll() {
        List<Entity> result = new ArrayList<>();

        try (Connection connection = connectionFactory.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(getAllQueryTemplate());

            while (resultSet.next()) {
                result.add(mapEntityFromResultSet(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    @Override
    public Entity getById(long id) {
        Entity result = null;

        try (Connection connection = connectionFactory.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(getByIdQueryTemplate(id).formatted("id_position"));

            if (resultSet.next()) {
                result = mapEntityFromResultSet(resultSet);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    @Override
    public boolean add(Entity entity) {
        return false;
    }

    @Override
    public boolean update(Entity entity) {
        return false;
    }

    @Override
    public boolean delete(long id) {
        return false;
    }
}
