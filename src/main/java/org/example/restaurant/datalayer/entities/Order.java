package org.example.restaurant.datalayer.entities;

import java.sql.Date;
import java.util.Objects;

public class Order extends Entity {
    private User user;
    private Table table;
    private Date orderDate;

    public Order() {}

    public Order(long id, User user, Table table, Date orderDate) {
        super(id);
        this.user = user;
        this.table = table;
        this.orderDate = orderDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Order order = (Order) o;
        return Objects.equals(user, order.user) &&
                Objects.equals(table, order.table) &&
                Objects.equals(orderDate, order.orderDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), user, table, orderDate);
    }
}
