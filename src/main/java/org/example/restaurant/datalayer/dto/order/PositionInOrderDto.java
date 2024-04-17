package org.example.restaurant.datalayer.dto.order;

import org.example.restaurant.datalayer.dto.position.PositionDto;

public class PositionInOrderDto {
    private Long id;
    private Integer positionCount;
    private PositionDto position;
    private OrderDto order;

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
}
