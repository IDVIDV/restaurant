package org.example.restaurant.servicelayer.services;

import org.example.restaurant.datalayer.dto.table.TableDto;
import org.example.restaurant.datalayer.entities.Table;
import org.example.restaurant.datalayer.mappers.TableMapper;
import org.example.restaurant.datalayer.repositories.TableRepository;
import org.example.restaurant.servicelayer.OperationResult;

import java.util.List;
import java.util.Objects;

public class TableService {
    private final TableMapper tableMapper;
    private final TableRepository tableRepository;

    public TableService(TableMapper tableMapper, TableRepository tableRepository) {
        this.tableMapper = tableMapper;
        this.tableRepository = tableRepository;
    }

    public List<TableDto> getAll() {
        return tableRepository.getAll().stream().map(tableMapper::map).toList();
    }
    public OperationResult<TableDto> getById(Long tableId) {
        if (Objects.isNull(tableId) || tableId <= 0) {
            return new OperationResult<>("Invalid table id");
        }

        Table table = tableRepository.getById(tableId);

        if (Objects.isNull(table)) {
            return new OperationResult<>("Table with given id does not exist");
        }

        return new OperationResult<>(tableMapper.map(table));
    }
}
