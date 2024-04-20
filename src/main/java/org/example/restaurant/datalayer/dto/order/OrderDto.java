package org.example.restaurant.datalayer.dto.order;

import org.example.restaurant.datalayer.dto.table.TableDto;

import java.sql.Date;
import java.util.Objects;

public class OrderDto {
    private Long id;
    private Long userId;
    private Date orderDate;
    private TableDto tableDto;

    public OrderDto() {
    }

    public OrderDto(Long id, Long userId, Date orderDate, TableDto tableDto) {
        this.id = id;
        this.userId = userId;
        this.orderDate = orderDate;
        this.tableDto = tableDto;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderDto orderDto = (OrderDto) o;
        return Objects.equals(id, orderDto.id) &&
                Objects.equals(userId, orderDto.userId) &&
                Objects.equals(orderDate, orderDto.orderDate) &&
                Objects.equals(tableDto, orderDto.tableDto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, orderDate, tableDto);
    }
}
