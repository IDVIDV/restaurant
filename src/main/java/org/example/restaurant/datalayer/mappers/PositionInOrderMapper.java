package org.example.restaurant.datalayer.mappers;

import org.example.restaurant.datalayer.dto.order.PositionInOrderDto;
import org.example.restaurant.datalayer.entities.PositionInOrder;

import java.util.Objects;

public class PositionInOrderMapper {
    private final PositionMapper positionMapper;
    private final OrderMapper orderMapper;

    public PositionInOrderMapper() {
        positionMapper = new PositionMapper();
        orderMapper = new OrderMapper();
    }

    public PositionInOrderDto map(PositionInOrder positionInOrder) {
        if (Objects.isNull(positionInOrder)) {
            return null;
        }

        PositionInOrderDto positionInOrderDto = new PositionInOrderDto();
        positionInOrderDto.setId(positionInOrder.getId());
        positionInOrderDto.setPositionCount(positionInOrder.getPositionCount());
        positionInOrderDto.setPosition(positionMapper.map(positionInOrder.getPosition()));
        positionInOrderDto.setOrder(orderMapper.map(positionInOrder.getOrder()));
        return positionInOrderDto;
    }
}
