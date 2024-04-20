package org.example.restaurant.datalayer.entities;

import java.sql.Date;
import java.util.Objects;

public class Order extends Entity {
    private Long userId;
    private Table table;
    private Date orderDate;
    private Boolean isFinished;

    public Order() {
    }

    public Order(Long id, Long userId, Table table,
                 Date orderDate, Boolean isFinished) {
        super(id);
        this.userId = userId;
        this.table = table;
        this.orderDate = orderDate;
        this.isFinished = isFinished;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Boolean isFinished() {
        return isFinished;
    }

    public void setFinished(Boolean finished) {
        isFinished = finished;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Order order = (Order) o;
        return Objects.equals(userId, order.userId) && Objects.equals(table, order.table) && Objects.equals(orderDate, order.orderDate) && Objects.equals(isFinished, order.isFinished);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), userId, table, orderDate, isFinished);
    }
}
