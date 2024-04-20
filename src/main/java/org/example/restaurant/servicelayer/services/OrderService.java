package org.example.restaurant.servicelayer.services;

import org.example.restaurant.datalayer.dto.order.CloseUnfinishedOrderDto;
import org.example.restaurant.datalayer.dto.order.OrderDto;
import org.example.restaurant.datalayer.dto.order.PositionInOrderDto;
import org.example.restaurant.datalayer.entities.Order;
import org.example.restaurant.datalayer.entities.Position;
import org.example.restaurant.datalayer.entities.PositionInOrder;
import org.example.restaurant.datalayer.mappers.OrderMapper;
import org.example.restaurant.datalayer.mappers.PositionInOrderMapper;
import org.example.restaurant.datalayer.repositories.OrderRepository;
import org.example.restaurant.datalayer.repositories.PositionInOrderRepository;
import org.example.restaurant.datalayer.repositories.PositionRepository;
import org.example.restaurant.servicelayer.OperationResult;
import org.example.restaurant.servicelayer.validators.OrderValidator;

import java.util.List;
import java.util.Objects;

public class OrderService {
    private final OrderValidator orderValidator;
    private final OrderMapper orderMapper;
    private final PositionInOrderMapper positionInOrderMapper;
    private final PositionRepository positionRepository;
    private final OrderRepository orderRepository;
    private final PositionInOrderRepository positionInOrderRepository;

    public OrderService(OrderValidator orderValidator, OrderMapper orderMapper,
                        PositionInOrderMapper positionInOrderMapper,
                        PositionRepository positionRepository, OrderRepository orderRepository,
                        PositionInOrderRepository positionInOrderRepository) {
        this.orderValidator = orderValidator;
        this.orderMapper = orderMapper;
        this.positionInOrderMapper = positionInOrderMapper;
        this.positionRepository = positionRepository;
        this.orderRepository = orderRepository;
        this.positionInOrderRepository = positionInOrderRepository;
    }

    public OperationResult<OrderDto> getById(Long orderId) {
        if (Objects.isNull(orderId) || orderId <= 0) {
            return new OperationResult<>("Invalid order id");
        }

        OrderDto order = orderMapper.map(orderRepository.getById(orderId));

        if (Objects.isNull(order)) {
            return new OperationResult<>("Order with given id does not exist");
        }

        return new OperationResult<>(order);
    }

    public OperationResult<List<OrderDto>> getFinishedOrdersByUserId(Long userId) {
        if (Objects.isNull(userId) || userId <= 0) {
            return new OperationResult<>("Invalid user id");
        }

        List<OrderDto> result = orderRepository.getFinishedByUserId(userId).stream()
                .map(orderMapper::map).toList();

        return new OperationResult<>(result);
    }

    public OperationResult<List<PositionInOrderDto>> getFinishedOrderPositions(Long orderId) {
        if (Objects.isNull(orderId) || orderId <= 0) {
            return new OperationResult<>("Invalid order id");
        }

        List<PositionInOrderDto> result = positionInOrderRepository.getAllByOrderId(orderId).stream()
                .map(positionInOrderMapper::map).toList();

        return new OperationResult<>(result);
    }

    public OperationResult<OrderDto> openNewOrderByUserId(Long userId) {
        if (Objects.isNull(userId) || userId <= 0) {
            return new OperationResult<>("Invalid user id");
        }

        Order openedOrder = new Order();
        openedOrder.setUserId(userId);
        openedOrder.setFinished(false);

        openedOrder = orderRepository.add(openedOrder);

        if (Objects.isNull(openedOrder)) {
            return new OperationResult<>("Couldn't open new order");
        }

        return new OperationResult<>(orderMapper.map(openedOrder));
    }

    public OperationResult<OrderDto> getUnfinishedOrderByUserId(Long userId) {
        if (Objects.isNull(userId) || userId <= 0) {
            return new OperationResult<>("Invalid user id");
        }

        OrderDto unfinishedOrder = orderMapper.map(orderRepository.getUnfinishedByUserId(userId));

        if (Objects.isNull(unfinishedOrder)) {
            OperationResult<OrderDto> openResult = openNewOrderByUserId(userId);

            if (openResult.isSuccess()) {
                unfinishedOrder = openResult.getResult();
            } else {
                return new OperationResult<>(openResult.getFailReason());
            }
        }

        return new OperationResult<>(unfinishedOrder);
    }

    public OperationResult<List<PositionInOrderDto>> getUnfinishedOrderPositionsByUserId(Long userId) {
        if (Objects.isNull(userId) || userId <= 0) {
            return new OperationResult<>("Invalid user id");
        }

        OrderDto unfinishedOrder = orderMapper.map(orderRepository.getUnfinishedByUserId(userId));

        if (Objects.isNull(unfinishedOrder)) {
            OperationResult<OrderDto> openResult = openNewOrderByUserId(userId);

            if (openResult.isSuccess()) {
                unfinishedOrder = openResult.getResult();
            } else {
                return new OperationResult<>(openResult.getFailReason());
            }
        }

        List<PositionInOrderDto> result = positionInOrderRepository.getAllByOrderId(unfinishedOrder.getId()).stream()
                .map(positionInOrderMapper::map).toList();

        return new OperationResult<>(result);
    }

    public OperationResult<PositionInOrderDto> addPositionToOrder(Long userId, Long positionId) {
        if (Objects.isNull(userId) || userId <= 0) {
            return new OperationResult<>("Invalid user id");
        }

        if (Objects.isNull(positionId) || positionId <= 0) {
            return new OperationResult<>("Invalid position id");
        }

        Position position = positionRepository.getById(positionId);

        if (Objects.isNull(position)) {
            return new OperationResult<>("No position with given id");
        }

        Order unfinishedOrder = orderRepository.getUnfinishedByUserId(userId);

        if (Objects.isNull(unfinishedOrder)) {
            Order order = new Order();
            order.setUserId(userId);
            unfinishedOrder = orderRepository.add(order);

            if (Objects.isNull(unfinishedOrder)) {
                return new OperationResult<>("Couldn't open new order");
            }
        }

        PositionInOrder positionInOrder = positionInOrderRepository
                .getByOrderAndPositionId(unfinishedOrder.getId(), positionId);

        if (Objects.isNull(positionInOrder)) {
            positionInOrder = new PositionInOrder();
            positionInOrder.setOrderId(unfinishedOrder.getId());
            positionInOrder.setPositionId(positionId);
            positionInOrder.setPosition(position);
            positionInOrder.setPositionCount(1);
            positionInOrder = positionInOrderRepository.add(positionInOrder);
        } else {
            positionInOrder.setPositionCount(positionInOrder.getPositionCount() + 1);
            positionInOrder = positionInOrderRepository.update(positionInOrder);
        }

        if (Objects.isNull(positionInOrder)) {
            return new OperationResult<>("Couldn't add position to order");
        }

        return new OperationResult<>(positionInOrderMapper.map(positionInOrder));
    }

    public OperationResult<Boolean> removePositionFromOrder(Long userId, Long positionId) {
        if (Objects.isNull(userId) || userId <= 0) {
            return new OperationResult<>("Invalid user id");
        }

        if (Objects.isNull(positionId) || positionId <= 0) {
            return new OperationResult<>("Invalid position id");
        }

        if (Objects.isNull(positionRepository.getById(positionId))) {
            return new OperationResult<>("No position with given id");
        }

        Order unfinishedOrder = orderRepository.getUnfinishedByUserId(userId);

        if (Objects.isNull(unfinishedOrder)) {
            return new OperationResult<>("Opened order does not exist");
        }

        PositionInOrder positionInOrder = positionInOrderRepository.getByOrderAndPositionId(unfinishedOrder.getId(), positionId);

        if (Objects.isNull(positionInOrder)) {
            return new OperationResult<>("Position in opened order does not exist");
        } else if (positionInOrder.getPositionCount() > 1){
            positionInOrder.setPositionCount(positionInOrder.getPositionCount() - 1);
            positionInOrder = positionInOrderRepository.update(positionInOrder);
        } else {
            if (!positionInOrderRepository.delete(positionInOrder.getId())) {
                return new OperationResult<>("Couldn't delete position from order");
            }
        }

        if (Objects.isNull(positionInOrder)) {
            return new OperationResult<>("Couldn't delete position from order");
        }

        return new OperationResult<>(true);
    }

    public OperationResult<Boolean> closeOpenedOrder(CloseUnfinishedOrderDto closeUnfinishedOrderDto) {
        if (!orderValidator.isCloseUnfinishedValid(closeUnfinishedOrderDto)) {
            return new OperationResult<>("Provided data is invalid");
        }

        Order orderToFinish = orderMapper.map(closeUnfinishedOrderDto);

        orderToFinish = orderRepository.update(orderToFinish);

        if (Objects.isNull(orderToFinish)) {
            return new OperationResult<>("Couldn't close order");
        }

        return new OperationResult<>(true);
    }
}
