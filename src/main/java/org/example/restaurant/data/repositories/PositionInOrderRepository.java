package org.example.restaurant.data.repositories;

import org.example.restaurant.data.ConnectionProvider;
import org.example.restaurant.data.entities.PositionInOrder;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PositionInOrderRepository {

    private final String tableName;
    private final ConnectionProvider connectionProvider;
    private final PositionRepository positionRepository;
    private final OrderRepository orderRepository;

    public PositionInOrderRepository(String tableName, ConnectionProvider connectionProvider,
                                     PositionRepository positionRepository, OrderRepository orderRepository) {
        this.tableName = tableName;
        this.connectionProvider = connectionProvider;
        this.positionRepository = positionRepository;
        this.orderRepository = orderRepository;
    }

    protected Map<String, String> getColumnValuesMap(PositionInOrder positionInOrder) {
        return Map.of(
                "position_id", Long.toString(positionInOrder.getPosition().getId()),
                "order_id", Long.toString(positionInOrder.getOrder().getId()),
                "position_count", Integer.toString(positionInOrder.getPositionCount())
        );
    }

    protected PositionInOrder mapEntityFromResultSet(ResultSet resultSet) {
        PositionInOrder positionInOrder = new PositionInOrder();

        try {
            positionInOrder.setId(resultSet.getLong("id_pio"));
            positionInOrder.setPosition(positionRepository.getById(resultSet.getLong("position_id")));
            positionInOrder.setOrder(orderRepository.getById(resultSet.getLong("order_id")));
            positionInOrder.setPositionCount(resultSet.getInt("position_count"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return positionInOrder;
    }

    public List<PositionInOrder> getAll() {
        List<PositionInOrder> result = new ArrayList<>();

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

    public PositionInOrder getById(long id) {
        PositionInOrder result = null;

        try (Connection connection = connectionProvider.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    RepositoryUtils.buildGetByIdQuery(id, tableName, "id_pio")
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

    public void add(PositionInOrder positionInOrder) {
        try (Connection connection = connectionProvider.getConnection()) {
            Statement statement = connection.createStatement();
            statement.executeUpdate(RepositoryUtils.buildAddQuery(tableName, getColumnValuesMap(positionInOrder)));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(PositionInOrder positionInOrder) {
        try (Connection connection = connectionProvider.getConnection()) {
            Statement statement = connection.createStatement();
            statement.executeUpdate(RepositoryUtils.buildUpdateQuery(positionInOrder.getId(), tableName,
                    "id_pio", getColumnValuesMap(positionInOrder)));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(long id) {
        try (Connection connection = connectionProvider.getConnection()) {
            Statement statement = connection.createStatement();
            statement.executeUpdate(RepositoryUtils.buildDeleteQuery(id, tableName, "id_pio"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
