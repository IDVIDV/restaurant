package org.example.restaurant.datalayer.mappers;

import org.example.restaurant.datalayer.dto.order.CloseUnfinishedOrderDto;
import org.example.restaurant.datalayer.dto.order.OrderDto;
import org.example.restaurant.datalayer.entities.Order;

import java.util.Objects;

public class OrderMapper {
    private final TableMapper tableMapper;

    public OrderMapper() {
        tableMapper = new TableMapper();
    }

    public OrderDto map(Order order) {
        if (Objects.isNull(order)) {
            return null;
        }

        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setUserId(order.getUserId());
        orderDto.setOrderDate(order.getOrderDate());
        orderDto.setTableDto(tableMapper.map(order.getTable()));
        return orderDto;
    }

    public Order map(CloseUnfinishedOrderDto closeOpenedOrderDto) {
        if (Objects.isNull(closeOpenedOrderDto)) {
            return null;
        }

        Order order = new Order();
        order.setId(closeOpenedOrderDto.getId());
        order.setUserId(closeOpenedOrderDto.getUserId());
        order.setOrderDate(closeOpenedOrderDto.getOrderDate());
        order.setTable(tableMapper.map(closeOpenedOrderDto.getTableDto()));
        order.setFinished(true);
        return order;
    }
}
