package org.example.restaurant.datalayer.entities;

import java.util.Objects;

public class PositionInOrder extends Entity {
    private Long positionId;
    private Long orderId;
    private Integer positionCount;
    private Position position;
    private Order order;

    public PositionInOrder(){
    }

    public PositionInOrder(Long id, Integer positionCount,
                           Position position, Order order) {
        super(id);
        this.positionId = position.getId();
        this.orderId = order.getId();
        this.positionCount = positionCount;
        this.position = position;
        this.order = order;
    }

    public Long getPositionId() {
        return positionId;
    }

    public void setPositionId(Long positionId) {
        this.positionId = positionId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Integer getPositionCount() {
        return positionCount;
    }

    public void setPositionCount(Integer positionCount) {
        this.positionCount = positionCount;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PositionInOrder that = (PositionInOrder) o;
        return Objects.equals(positionId, that.positionId) && Objects.equals(orderId, that.orderId) && Objects.equals(positionCount, that.positionCount) && Objects.equals(position, that.position) && Objects.equals(order, that.order);
    }

    @Override
    public int hashCode() {
        return Objects.hash(positionId, orderId, positionCount, position, order);
    }
}
