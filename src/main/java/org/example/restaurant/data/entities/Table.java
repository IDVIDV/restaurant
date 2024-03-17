package org.example.restaurant.data.entities;

import java.util.Objects;

public class Table extends Entity {
    private int tableNumber;
    private int capacity;

    public Table() {}

    public Table(long id, int tableNumber, int capacity) {
        super(id);
        this.tableNumber = tableNumber;
        this.capacity = capacity;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Table table = (Table) o;
        return tableNumber == table.tableNumber && capacity == table.capacity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), tableNumber, capacity);
    }
}
