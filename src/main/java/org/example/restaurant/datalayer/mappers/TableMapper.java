package org.example.restaurant.datalayer.mappers;

import org.example.restaurant.datalayer.dto.table.TableDto;
import org.example.restaurant.datalayer.entities.Table;

import java.util.Objects;

public class TableMapper {
    private static final TableMapper INSTANCE = new TableMapper();

    public static TableMapper getInstance() {
        return INSTANCE;
    }

    private TableMapper() {
    }

    public TableDto map(Table table) {
        if (Objects.isNull(table)) {
            return null;
        }

        TableDto tableDto = new TableDto();
        tableDto.setId(table.getId());
        tableDto.setTableNumber(table.getTableNumber());
        tableDto.setCapacity(table.getCapacity());
        return tableDto;
    }

    public Table map(TableDto tableDto) {
        if (Objects.isNull(tableDto)) {
            return null;
        }

        Table table = new Table();
        table.setId(tableDto.getId());
        table.setTableNumber(tableDto.getTableNumber());
        table.setCapacity(tableDto.getCapacity());
        return table;
    }
}
