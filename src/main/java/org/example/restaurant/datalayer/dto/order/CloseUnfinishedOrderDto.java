package org.example.restaurant.datalayer.dto.order;

import org.example.restaurant.datalayer.dto.table.TableDto;

import java.sql.Date;
import java.util.Objects;

public class CloseUnfinishedOrderDto {
    private Long id;
    private Long userId;
    private Date orderDate;
    private TableDto tableDto;

    public CloseUnfinishedOrderDto() {
    }

    public CloseUnfinishedOrderDto(Long id, Long userId, Date orderDate,
                                   TableDto tableDto) {
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

    public void setTable(TableDto tableDto) {
        this.tableDto = tableDto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CloseUnfinishedOrderDto that = (CloseUnfinishedOrderDto) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(orderDate, that.orderDate) &&
                Objects.equals(tableDto, that.tableDto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, orderDate, tableDto);
    }
}
