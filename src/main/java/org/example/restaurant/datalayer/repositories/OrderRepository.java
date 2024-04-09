package org.example.restaurant.datalayer.repositories;

import org.example.restaurant.datalayer.ConnectionProvider;
import org.example.restaurant.datalayer.entities.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class OrderRepository {
    private static final String TABLE_NAME = "order";
    private static final String SELECT_ALL_QUERY = "SELECT * FROM " + TABLE_NAME;
    private static final String SELECT_BY_ID_QUERY = "SELECT * FROM " + TABLE_NAME + " WHERE id_order = (?)";
    private static final String ADD_QUERY = "INSERT INTO " + TABLE_NAME + " VALUES (DEFAULT,(?),(?),(?))";
    private static final String UPDATE_QUERY = "UPDATE " + TABLE_NAME + " SET user_id = (?), table_id = (?), " +
            "order_date = (?) WHERE id_order = (?)";
    private static final String DELETE_QUERY = "DELETE FROM " + TABLE_NAME + " WHERE id_order = (?)";
    private final ConnectionProvider connectionProvider;
    private final UserRepository userRepository;
    private final TableRepository tableRepository;

    public OrderRepository(ConnectionProvider connectionProvider,
                           UserRepository userRepository, TableRepository tableRepository) {
        this.connectionProvider = connectionProvider;
        this.userRepository = userRepository;
        this.tableRepository = tableRepository;
    }

    protected Order mapEntityFromResultSet(ResultSet resultSet) {
        Order order = new Order();

        try {
            order.setId(resultSet.getLong("id_order"));
            order.setUser(userRepository.getById(resultSet.getLong("user_id")));
            order.setTable(tableRepository.getById(resultSet.getLong("table_id")));
            order.setOrderDate(resultSet.getDate("order_date"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return order;
    }

    protected void prepareStatement(Order order, PreparedStatement statement) throws SQLException {
        statement.setLong(1, order.getUser().getId());
        statement.setLong(2, order.getTable().getId());
        statement.setDate(3, order.getOrderDate());
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
            throw new RuntimeException(e);
        }

        return result;
    }

    public Order getById(long id) {
        Order result = null;

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

    public Order add(Order order) {
        try (Connection connection = connectionProvider.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(ADD_QUERY, Statement.RETURN_GENERATED_KEYS);

            prepareStatement(order, statement);

            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                order.setId(generatedKeys.getLong("id_order"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return order;
    }

    public Order update(Order order) {
        try (Connection connection = connectionProvider.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY);

            prepareStatement(order, statement);
            statement.setLong(4, order.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return order;
    }

    public boolean delete(long id) {
        try (Connection connection = connectionProvider.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(DELETE_QUERY);

            statement.setLong(1, id);

            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
