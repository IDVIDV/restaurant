package org.example.restaurant.datalayer.repositories;

import org.example.restaurant.datalayer.ConnectionProvider;
import org.example.restaurant.datalayer.entities.PositionInOrder;
import org.example.restaurant.datalayer.exceptions.DataBaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PositionInOrderRepository {
    private static final String TABLE_NAME = "position_in_order";
    private static final String SELECT_ALL_QUERY = "SELECT * FROM " + TABLE_NAME;
    private static final String SELECT_BY_ID_QUERY = "SELECT * FROM " + TABLE_NAME + " WHERE id_pio = (?)";
    private static final String ADD_QUERY = "INSERT INTO " + TABLE_NAME + " VALUES (DEFAULT,(?),(?),(?))";
    private static final String UPDATE_QUERY = "UPDATE " + TABLE_NAME + " SET position_id = (?), order_id = (?), " +
            "position_count = (?) WHERE id_pio = (?)";
    private static final String DELETE_QUERY = "DELETE FROM " + TABLE_NAME + " WHERE id_pio = (?)";
    private final ConnectionProvider connectionProvider;

    public PositionInOrderRepository(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    protected PositionInOrder mapEntityFromResultSet(ResultSet resultSet) {
        PositionInOrder positionInOrder = new PositionInOrder();

        try {
            positionInOrder.setId(resultSet.getLong("id_pio"));
            positionInOrder.setPositionId(resultSet.getLong("position_id"));
            positionInOrder.setOrderId(resultSet.getLong("order_id"));
            positionInOrder.setPositionCount(resultSet.getInt("position_count"));
        } catch (SQLException e) {
            throw new DataBaseException(e.getMessage());
        }

        return positionInOrder;
    }

    protected void prepareStatement(PositionInOrder positionInOrder, PreparedStatement statement) throws SQLException {
        statement.setLong(1, positionInOrder.getPositionId());
        statement.setLong(2, positionInOrder.getOrderId());
        statement.setInt(3, positionInOrder.getPositionCount());
    }

    public List<PositionInOrder> getAll() {
        List<PositionInOrder> result = new ArrayList<>();

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

    public PositionInOrder getById(Long id) {
        PositionInOrder result = null;

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

    public PositionInOrder add(PositionInOrder positionInOrder) {
        try (Connection connection = connectionProvider.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(ADD_QUERY, Statement.RETURN_GENERATED_KEYS);

            prepareStatement(positionInOrder, statement);

            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();

            if (generatedKeys.next()) {
                positionInOrder.setId(generatedKeys.getLong("id_pio"));
            }

        } catch (SQLException e) {
            throw new DataBaseException(e.getMessage());
        }

        return positionInOrder;
    }

    public PositionInOrder update(PositionInOrder positionInOrder) {
        try (Connection connection = connectionProvider.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY);

            prepareStatement(positionInOrder, statement);
            statement.setLong(4, positionInOrder.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataBaseException(e.getMessage());
        }

        return positionInOrder;
    }

    public boolean delete(Long id) {
        try (Connection connection = connectionProvider.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(DELETE_QUERY);

            statement.setLong(1, id);

            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new DataBaseException(e.getMessage());
        }
    }
}
