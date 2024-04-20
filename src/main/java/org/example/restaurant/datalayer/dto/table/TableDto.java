package org.example.restaurant.datalayer.dto.table;

import java.util.Objects;

public class TableDto {
    private Long id;
    private Integer tableNumber;
    private Integer capacity;

    public TableDto() {
    }

    public TableDto(Long id, Integer tableNumber, Integer capacity) {
        this.id = id;
        this.tableNumber = tableNumber;
        this.capacity = capacity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(Integer tableNumber) {
        this.tableNumber = tableNumber;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TableDto tableDto = (TableDto) o;
        return Objects.equals(id, tableDto.id) &&
                Objects.equals(tableNumber, tableDto.tableNumber) &&
                Objects.equals(capacity, tableDto.capacity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tableNumber, capacity);
    }
}
