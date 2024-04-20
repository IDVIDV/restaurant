package org.example.restaurant.datalayer.dto.order;

import org.example.restaurant.datalayer.dto.position.PositionDto;

import java.util.Objects;

public class PositionInOrderDto {
    private Long id;
    private Integer positionCount;
    private PositionDto position;
    private OrderDto order;

    public PositionInOrderDto() {
    }

    public PositionInOrderDto(Long id, Integer positionCount,
                              PositionDto position, OrderDto order) {
        this.id = id;
        this.positionCount = positionCount;
        this.position = position;
        this.order = order;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPositionCount() {
        return positionCount;
    }

    public void setPositionCount(Integer positionCount) {
        this.positionCount = positionCount;
    }

    public PositionDto getPosition() {
        return position;
    }

    public void setPosition(PositionDto position) {
        this.position = position;
    }

    public OrderDto getOrder() {
        return order;
    }

    public void setOrder(OrderDto order) {
        this.order = order;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PositionInOrderDto that = (PositionInOrderDto) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(positionCount, that.positionCount) &&
                Objects.equals(position, that.position) &&
                Objects.equals(order, that.order);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, positionCount, position, order);
    }
}
