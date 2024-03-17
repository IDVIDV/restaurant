package org.example.restaurant.data.entities;

import java.util.Objects;

public class PositionInOrder extends Entity {
    private Position position;
    private Order order;
    private int positionCount;

    public PositionInOrder() {}

    public PositionInOrder(long id, Position position, Order order, int positionCount) {
        super(id);
        this.position = position;
        this.order = order;
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

    public int getPositionCount() {
        return positionCount;
    }

    public void setPositionCount(int positionCount) {
        this.positionCount = positionCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PositionInOrder that = (PositionInOrder) o;
        return positionCount == that.positionCount &&
                Objects.equals(position, that.position) &&
                Objects.equals(order, that.order);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), position, order, positionCount);
    }
}
