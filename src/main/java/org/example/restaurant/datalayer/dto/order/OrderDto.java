package org.example.restaurant.datalayer.dto.order;

import org.example.restaurant.datalayer.dto.table.TableDto;

import java.sql.Date;

public class OrderDto {
    private Long id;
    private Long userId;
    private Date orderDate;
    private TableDto tableDto;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public TableDto getTableDto() {
        return tableDto;
    }

    public void setTableDto(TableDto tableDto) {
        this.tableDto = tableDto;
    }
}
