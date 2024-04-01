package org.example.restaurant.data.repositories;

import org.example.restaurant.data.ConnectionProvider;
import org.example.restaurant.data.entities.Table;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TableRepository {

    private final String tableName;
    private final ConnectionProvider connectionProvider;

    public TableRepository(String tableName, ConnectionProvider connectionProvider) {
        this.tableName = tableName;
        this.connectionProvider = connectionProvider;
    }

    protected Map<String, String> getColumnValuesMap(Table table) {
        return Map.of(
                "table_number", Integer.toString(table.getTableNumber()),
                "capacity", Integer.toString(table.getCapacity())
        );
    }

    protected Table mapEntityFromResultSet(ResultSet resultSet) {
        Table table = new Table();

        try {
            table.setId(resultSet.getLong("id_table"));
            table.setTableNumber(resultSet.getInt("table_number"));
            table.setCapacity(resultSet.getInt("capacity"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return table;
    }

    public List<Table> getAll() {
        List<Table> result = new ArrayList<>();

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

    public Table getById(long id) {
        Table result = null;

        try (Connection connection = connectionProvider.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    RepositoryUtils.buildGetByIdQuery(id, tableName, "id_table")
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

    public void add(Table table) {
        try (Connection connection = connectionProvider.getConnection()) {
            Statement statement = connection.createStatement();
            statement.executeUpdate(RepositoryUtils.buildAddQuery(tableName, getColumnValuesMap(table)));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Table table) {
        try (Connection connection = connectionProvider.getConnection()) {
            Statement statement = connection.createStatement();
            statement.executeUpdate(RepositoryUtils.buildUpdateQuery(table.getId(), tableName,
                    "id_table", getColumnValuesMap(table)));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(long id) {
        try (Connection connection = connectionProvider.getConnection()) {
            Statement statement = connection.createStatement();
            statement.executeUpdate(RepositoryUtils.buildDeleteQuery(id, tableName, "id_table"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
