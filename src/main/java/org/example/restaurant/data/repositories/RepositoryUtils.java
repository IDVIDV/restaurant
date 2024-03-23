package org.example.restaurant.data.repositories;

import java.util.Map;

public class RepositoryUtils {
    static String getValueInQuotes(String value) {
        return "'" + value + "'";
    }

    static String buildGetAllQuery(String tableName) {
        return "SELECT * FROM " + tableName;
    }

    static String buildGetByIdQuery(long id, String tableName, String idColumnName) {
        return "SELECT * FROM " + tableName + " WHERE " + idColumnName + " = " + id;
    }

    static String buildAddQuery(String tableName, Map<String, String> columnValues) {
        StringBuilder queryHead = new StringBuilder("INSERT INTO " + tableName + "(");
        StringBuilder queryTail = new StringBuilder("VALUES (");

        columnValues.forEach((columnName, columnValue) -> {
            queryHead.append(columnName).append(",");
            queryTail.append(columnValue).append(",");
        });


        queryHead.deleteCharAt(queryHead.length() - 1);
        queryHead.append(") ");

        queryTail.deleteCharAt(queryTail.length() - 1);
        queryTail.append(");");

        queryHead.append(queryTail);

        return queryHead.toString();
    }

    static String buildUpdateQuery(long id, String tableName, String idColumnName, Map<String, String> columnValues) {
        StringBuilder query = new StringBuilder("UPDATE " + tableName + " SET ");

        columnValues.forEach((columnName, columnValue) -> {
            query.append(columnName).append(" = ").append(columnValue).append(",");
        });

        query.deleteCharAt(query.length() - 1);
        query.append(" WHERE ").append(idColumnName).append(" = ").append(id);

        return query.toString();
    }

    static String buildDeleteQuery(long id, String tableName, String idColumnName) {
        return "DELETE FROM " + tableName + " WHERE " + idColumnName + " = " + id;
    }
}
