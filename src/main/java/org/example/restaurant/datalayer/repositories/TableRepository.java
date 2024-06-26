package org.example.restaurant.datalayer.repositories;

import org.example.restaurant.datalayer.ConnectionProvider;
import org.example.restaurant.datalayer.entities.Table;
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

public class TableRepository {
    private static final Logger logger = LoggerFactory.getLogger(TableRepository.class);
    private static final String TABLE_NAME = "\"table\"";
    private static final String SELECT_ALL_QUERY = "SELECT id_table, table_number, capacity FROM " + TABLE_NAME;
    private static final String SELECT_BY_ID_QUERY = "SELECT id_table, table_number, capacity FROM " + TABLE_NAME +
            " WHERE id_table = (?)";
    private static final String ADD_QUERY = "INSERT INTO " + TABLE_NAME + " VALUES (DEFAULT,(?),(?))";
    private static final String UPDATE_QUERY = "UPDATE " + TABLE_NAME + " SET table_number = (?), capacity = (?) " +
            "WHERE id_table = (?)";
    private static final String DELETE_QUERY = "DELETE FROM " + TABLE_NAME + " WHERE id_table = (?)";
    private final ConnectionProvider connectionProvider;

    public TableRepository(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    public List<Table> getAll() {
        List<Table> result = new ArrayList<>();

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

    public Table getById(Long id) {
        Table result = null;

        try (Connection connection = connectionProvider.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID_QUERY);

            statement.setLong(1, id);

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

    public Table add(Table table) {
        Table result = null;

        try (Connection connection = connectionProvider.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(ADD_QUERY, Statement.RETURN_GENERATED_KEYS);

            prepareStatement(table, statement);

            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();

            if (generatedKeys.next()) {
                table.setId(generatedKeys.getLong("id_table"));
                result = table;
            }

        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DataBaseException(e.getMessage());
        }

        return result;
    }

    public Table update(Table table) {
        Table result = null;

        try (Connection connection = connectionProvider.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY);

            prepareStatement(table, statement);
            statement.setLong(3, table.getId());

            if (statement.executeUpdate() == 1) {
                result = table;
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

            statement.setLong(1, id);

            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DataBaseException(e.getMessage());
        }
    }

    private Table mapEntityFromResultSet(ResultSet resultSet) {
        Table table = new Table();

        try {
            table.setId(resultSet.getLong("id_table"));
            table.setTableNumber(resultSet.getInt("table_number"));
            table.setCapacity(resultSet.getInt("capacity"));
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DataBaseException(e.getMessage());
        }

        return table;
    }

    private void prepareStatement(Table table, PreparedStatement statement) throws SQLException {
        statement.setObject(1, table.getTableNumber());
        statement.setObject(2, table.getCapacity());
    }
}
