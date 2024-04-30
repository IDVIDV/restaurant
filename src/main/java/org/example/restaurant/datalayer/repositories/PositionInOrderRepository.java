package org.example.restaurant.datalayer.repositories;

import org.example.restaurant.datalayer.ConnectionProvider;
import org.example.restaurant.datalayer.entities.PositionInOrder;
import org.example.restaurant.datalayer.exceptions.DataBaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PositionInOrderRepository {
    private static final Logger logger = LoggerFactory.getLogger(PositionInOrderRepository.class);
    private static final String TABLE_NAME = "position_in_order";
    private static final String SELECT_ALL_QUERY = "SELECT id_pio, position_id, order_id, position_count FROM " +
            TABLE_NAME;
    private static final String SELECT_ALL_BY_ORDER_ID_QUERY = "SELECT id_pio, position_id, order_id, position_count FROM " +
            TABLE_NAME + " WHERE order_id = (?)";
    private static final String SELECT_BY_ID_QUERY = "SELECT id_pio, position_id, order_id, position_count FROM " +
            TABLE_NAME + " WHERE id_pio = (?)";
    private static final String SELECT_BY_ORDER_AND_POSITION_ID_QUERY = "SELECT id_pio, position_id, order_id, position_count FROM " +
            TABLE_NAME + " WHERE order_id = (?) AND position_id = (?)";
    private static final String ADD_QUERY = "INSERT INTO " + TABLE_NAME + " VALUES (DEFAULT,(?),(?),(?))";
    private static final String UPDATE_QUERY = "UPDATE " + TABLE_NAME + " SET position_id = (?), order_id = (?), " +
            "position_count = (?) WHERE id_pio = (?)";
    private static final String DELETE_QUERY = "DELETE FROM " + TABLE_NAME + " WHERE id_pio = (?)";
    private final ConnectionProvider connectionProvider;
    private final PositionRepository positionRepository;
    private final OrderRepository orderRepository;

    public PositionInOrderRepository(ConnectionProvider connectionProvider,
                                     PositionRepository positionRepository,
                                     OrderRepository orderRepository) {
        this.connectionProvider = connectionProvider;
        this.positionRepository = positionRepository;
        this.orderRepository = orderRepository;
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
            logger.error(e.getMessage());
            throw new DataBaseException(e.getMessage());
        }

        return result;
    }

    public List<PositionInOrder> getAllByOrderId(Long orderId) {
        List<PositionInOrder> result = new ArrayList<>();

        try (Connection connection = connectionProvider.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SELECT_ALL_BY_ORDER_ID_QUERY);

            statement.setLong(1, orderId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                result.add(mapEntityFromResultSet(resultSet));
            }

        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DataBaseException(e.getMessage());
        }

        return result;
    }

    public PositionInOrder getById(Long id) {
        PositionInOrder result = null;

        try (Connection connection = connectionProvider.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID_QUERY);

            statement.setObject(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                result = mapEntityFromResultSet(resultSet);
            }

        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DataBaseException(e.getMessage());
        }

        return result;
    }

    public PositionInOrder getByOrderAndPositionId(Long orderId, Long positionId) {
        PositionInOrder result = null;

        try (Connection connection = connectionProvider.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SELECT_BY_ORDER_AND_POSITION_ID_QUERY);

            statement.setObject(1, orderId);
            statement.setObject(2, positionId);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                result = mapEntityFromResultSet(resultSet);
            }

        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DataBaseException(e.getMessage());
        }

        return result;
    }

    public PositionInOrder add(PositionInOrder positionInOrder) {
        PositionInOrder result = null;

        try (Connection connection = connectionProvider.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(ADD_QUERY, Statement.RETURN_GENERATED_KEYS);

            prepareStatement(positionInOrder, statement);

            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();

            if (generatedKeys.next()) {
                positionInOrder.setId(generatedKeys.getLong("id_pio"));
                result = positionInOrder;
            }

        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DataBaseException(e.getMessage());
        }

        return result;
    }

    public PositionInOrder update(PositionInOrder positionInOrder) {
        PositionInOrder result = null;

        try (Connection connection = connectionProvider.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY);

            prepareStatement(positionInOrder, statement);
            statement.setObject(4, positionInOrder.getId());

            if (statement.executeUpdate() == 1) {
                result = positionInOrder;
            }

        } catch (SQLException e) {
            logger.error(e.getMessage());
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
            logger.error(e.getMessage());
            throw new DataBaseException(e.getMessage());
        }
    }

    private PositionInOrder mapEntityFromResultSet(ResultSet resultSet) {
        PositionInOrder positionInOrder = new PositionInOrder();

        try {
            positionInOrder.setId(resultSet.getLong("id_pio"));
            positionInOrder.setPositionId(resultSet.getLong("position_id"));
            positionInOrder.setOrderId(resultSet.getLong("order_id"));
            positionInOrder.setPositionCount(resultSet.getInt("position_count"));
            positionInOrder.setPosition(positionRepository.getById(positionInOrder.getPositionId()));
            positionInOrder.setOrder(orderRepository.getById(positionInOrder.getOrderId()));
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DataBaseException(e.getMessage());
        }

        return positionInOrder;
    }

    private void prepareStatement(PositionInOrder positionInOrder, PreparedStatement statement) throws SQLException {
        statement.setObject(1, positionInOrder.getPositionId());
        statement.setObject(2, positionInOrder.getOrderId());
        statement.setObject(3, positionInOrder.getPositionCount());
    }
}
