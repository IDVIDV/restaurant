package org.example.restaurant.datalayer.entities;

import org.example.restaurant.datalayer.dto.table.TableDto;

import java.sql.Date;

public class Order extends Entity {
    private Long userId;
    private Table table;
    private Date orderDate;
    private Boolean isFinished;

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
}
