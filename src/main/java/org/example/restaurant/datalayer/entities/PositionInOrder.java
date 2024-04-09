package org.example.restaurant.datalayer.entities;

import java.util.Objects;

public class PositionInOrder extends Entity {
    //private Position position;
    //private Order order;
    private long positionId;
    private long orderId;
    private int positionCount;

    public PositionInOrder() {
    }

//    public PositionInOrder(long id, Position position, Order order, int positionCount) {
//        super(id);
//        this.position = position;
//        this.order = order;
//        this.positionCount = positionCount;
//    }

    public PositionInOrder(long id, long positionId, long orderId, int positionCount) {
        super(id);
        this.positionId = positionId;
        this.orderId = orderId;
        this.positionCount = positionCount;
    }

//    public Position getPosition() {
//        return position;
//    }
//
//    public void setPosition(Position position) {
//        this.position = position;
//    }
//
//    public Order getOrder() {
//        return order;
//    }
//
//    public void setOrder(Order order) {
//        this.order = order;
//    }

    public long getPositionId() {
        return positionId;
    }

    public void setPositionId(long positionId) {
        this.positionId = positionId;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public int getPositionCount() {
        return positionCount;
    }

    public void setPositionCount(int positionCount) {
        this.positionCount = positionCount;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        if (!super.equals(o)) return false;
//        PositionInOrder that = (PositionInOrder) o;
//        return positionCount == that.positionCount &&
//                Objects.equals(position, that.position) &&
//                Objects.equals(order, that.order);
//    }

//    @Override
//    public int hashCode() {
//        return Objects.hash(super.hashCode(), position, order, positionCount);
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PositionInOrder that = (PositionInOrder) o;
        return positionId == that.positionId && orderId == that.orderId && positionCount == that.positionCount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), positionId, orderId, positionCount);
    }
}
