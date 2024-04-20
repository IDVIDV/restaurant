package org.example.restaurant.datalayer.entities;

import java.util.Objects;

public class Table extends Entity {
    private Integer tableNumber;
    private Integer capacity;

    public Table() {
    }

    public Table(Long id, Integer tableNumber, Integer capacity) {
        super(id);
        this.tableNumber = tableNumber;
        this.capacity = capacity;
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
        Table table = (Table) o;
        return Objects.equals(tableNumber, table.tableNumber) && Objects.equals(capacity, table.capacity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tableNumber, capacity);
    }
}
