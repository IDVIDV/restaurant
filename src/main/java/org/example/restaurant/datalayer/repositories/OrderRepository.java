package org.example.restaurant.datalayer.repositories;

import org.example.restaurant.datalayer.ConnectionProvider;
import org.example.restaurant.datalayer.entities.Order;
import org.example.restaurant.datalayer.exceptions.DataBaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OrderRepository {
    private static final String TABLE_NAME = "\"order\"";
    private static final String SELECT_ALL_QUERY = "SELECT id_order, user_id, table_id, order_date, finished FROM " +
            TABLE_NAME;
    private static final String SELECT_FINISHED_BY_USER_ID_QUERY = "SELECT id_order, user_id, table_id, order_date," +
            " finished FROM " + TABLE_NAME + " WHERE finished = true AND user_id = (?)";
    private static final String SELECT_UNFINISHED_BY_USER_ID_QUERY = "SELECT id_order, user_id, table_id, order_date," +
            " finished FROM " + TABLE_NAME + " WHERE finished = false AND user_id = (?)";
    private static final String SELECT_BY_ID_QUERY = "SELECT * FROM " + TABLE_NAME + " WHERE id_order = (?)";
    private static final String ADD_UNFINISHED_QUERY = "INSERT INTO " + TABLE_NAME + " VALUES (DEFAULT,(?),(?),(?),(?))";
    private static final String UPDATE_QUERY = "UPDATE " + TABLE_NAME + " SET user_id = (?), table_id = (?), " +
            "order_date = (?), finished = (?) WHERE id_order = (?)";
    private static final String DELETE_QUERY = "DELETE FROM " + TABLE_NAME + " WHERE id_order = (?)";
    private final ConnectionProvider connectionProvider;
    private final TableRepository tableRepository;

    public OrderRepository(ConnectionProvider connectionProvider, TableRepository tableRepository) {
        this.connectionProvider = connectionProvider;
        this.tableRepository = tableRepository;
    }

    public List<Order> getAll() {
        List<Order> result = new ArrayList<>();

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

    public List<Order> getFinishedByUserId(Long userId) {
        List<Order> result = new ArrayList<>();

        try (Connection connection = connectionProvider.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SELECT_FINISHED_BY_USER_ID_QUERY);

            statement.setObject(1, userId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                result.add(mapEntityFromResultSet(resultSet));
            }

        } catch (SQLException e) {
            throw new DataBaseException(e.getMessage());
        }

        return result;
    }

    public Order getUnfinishedByUserId(Long userId) {
        Order result = null;

        try (Connection connection = connectionProvider.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SELECT_UNFINISHED_BY_USER_ID_QUERY);

            statement.setObject(1, userId);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                result = mapEntityFromResultSet(resultSet);
            }

        } catch (SQLException e) {
            throw new DataBaseException(e.getMessage());
        }

        return result;
    }

    public Order getById(Long id) {
        Order result = null;

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

    public Order add(Order order) {
        Order result = null;

        try (Connection connection = connectionProvider.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(ADD_UNFINISHED_QUERY, Statement.RETURN_GENERATED_KEYS);

            prepareStatement(order, statement);

            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();

            if (generatedKeys.next()) {
                order.setId(generatedKeys.getLong("id_order"));
                result = order;
            }

        } catch (SQLException e) {
            throw new DataBaseException(e.getMessage());
        }

        return result;
    }

    public Order update(Order order) {
        Order result = null;

        try (Connection connection = connectionProvider.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY);

            prepareStatement(order, statement);
            statement.setObject(5, order.getId());

            if (statement.executeUpdate() == 1) {
                result = order;
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

    private Order mapEntityFromResultSet(ResultSet resultSet) {
        Order order = new Order();

        try {
            order.setId(resultSet.getLong("id_order"));
            order.setUserId(resultSet.getLong("user_id"));
            order.setTable(tableRepository.getById(resultSet.getLong("table_id")));
            order.setOrderDate(resultSet.getDate("order_date"));
            order.setFinished(resultSet.getBoolean("finished"));
        } catch (SQLException e) {
            throw new DataBaseException(e.getMessage());
        }

        return order;
    }

    private void prepareStatement(Order order, PreparedStatement statement) throws SQLException {
        statement.setObject(1, order.getUserId());
        if (Objects.isNull(order.getTable())) {
            statement.setObject(2, null);
        } else {
            statement.setLong(2, order.getTable().getId());
        }

        if (Objects.isNull(order.getOrderDate())) {
            statement.setObject(3, null);
        } else {
            statement.setDate(3, order.getOrderDate());
        }
        statement.setObject(4, order.isFinished());
    }
}
