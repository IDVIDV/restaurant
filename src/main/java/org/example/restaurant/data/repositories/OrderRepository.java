package org.example.restaurant.data.repositories;

import org.example.restaurant.data.ConnectionProvider;
import org.example.restaurant.data.entities.Order;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderRepository {

    private final String tableName;
    private final ConnectionProvider connectionProvider;
    private final UserRepository userRepository;
    private final TableRepository tableRepository;

    public OrderRepository(String tableName, ConnectionProvider connectionProvider,
                           UserRepository userRepository, TableRepository tableRepository) {
        this.tableName = tableName;
        this.connectionProvider = connectionProvider;
        this.userRepository = userRepository;
        this.tableRepository = tableRepository;
    }

    protected Map<String, String> getColumnValuesMap(Order order) {
        return Map.of(
                "user_id", Long.toString(order.getUser().getId()),
                "table_id", Long.toString(order.getTable().getId()),
                "order_date", RepositoryUtils.getValueInQuotes(order.getOrderDate().toString())
        );
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

    public List<Order> getAll() {
        List<Order> result = new ArrayList<>();

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

    public Order getById(long id) {
        Order result = null;

        try (Connection connection = connectionProvider.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    RepositoryUtils.buildGetByIdQuery(id, tableName, "id_order")
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

    public void add(Order order) {
        try (Connection connection = connectionProvider.getConnection()) {
            Statement statement = connection.createStatement();
            statement.executeUpdate(RepositoryUtils.buildAddQuery(tableName, getColumnValuesMap(order)));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Order order) {
        try (Connection connection = connectionProvider.getConnection()) {
            Statement statement = connection.createStatement();
            statement.executeUpdate(RepositoryUtils.buildUpdateQuery(order.getId(), tableName,
                    "id_order", getColumnValuesMap(order)));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(long id) {
        try (Connection connection = connectionProvider.getConnection()) {
            Statement statement = connection.createStatement();
            statement.executeUpdate(RepositoryUtils.buildDeleteQuery(id, tableName, "id_order"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
