package org.example.restaurant.data.repositories;

import org.example.restaurant.data.ConnectionFactory;
import org.example.restaurant.data.entities.Entity;

import java.sql.ResultSet;
import java.util.List;

public abstract class Repository {
    protected final String tableName;
    protected final ConnectionFactory connectionFactory;

    public Repository(String tableName, ConnectionFactory connectionFactory) {
        this.tableName = tableName;
        this.connectionFactory = connectionFactory;
    }

    protected String getAllQueryTemplate() {
        return "SELECT * FROM " + tableName;
    }

    protected String getByIdQueryTemplate(long id) {
        return "SELECT * FROM " + tableName + " WHERE {idColumn} = " + id;
    }

    protected abstract Entity mapEntityFromResultSet(ResultSet resultSet);

    public abstract List<Entity> getAll();

    public abstract Entity getById(long id);

    public abstract boolean add(Entity entity);

    public abstract boolean update(Entity entity);

    public abstract boolean delete(long id);
}
