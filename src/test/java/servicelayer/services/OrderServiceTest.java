package servicelayer.services;

import org.example.restaurant.datalayer.dto.order.CloseUnfinishedOrderDto;
import org.example.restaurant.datalayer.dto.order.OrderDto;
import org.example.restaurant.datalayer.dto.order.PositionInOrderDto;
import org.example.restaurant.datalayer.dto.position.PositionDto;
import org.example.restaurant.datalayer.dto.table.TableDto;
import org.example.restaurant.datalayer.entities.Order;
import org.example.restaurant.datalayer.entities.Position;
import org.example.restaurant.datalayer.entities.PositionInOrder;
import org.example.restaurant.datalayer.entities.Table;
import org.example.restaurant.datalayer.mappers.OrderMapper;
import org.example.restaurant.datalayer.mappers.PositionInOrderMapper;
import org.example.restaurant.datalayer.repositories.OrderRepository;
import org.example.restaurant.datalayer.repositories.PositionInOrderRepository;
import org.example.restaurant.datalayer.repositories.PositionRepository;
import org.example.restaurant.servicelayer.OperationResult;
import org.example.restaurant.servicelayer.services.OrderService;
import org.example.restaurant.servicelayer.validators.OrderValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    OrderValidator orderValidator;
    @Mock
    OrderMapper orderMapper;
    @Mock
    PositionInOrderMapper positionInOrderMapper;
    @Mock
    PositionRepository positionRepository;
    @Mock
    OrderRepository orderRepository;
    @Mock
    PositionInOrderRepository positionInOrderRepository;
    @InjectMocks
    OrderService orderService;

    Position position1;
    Position position2;
    PositionDto positionDto1;
    PositionDto positionDto2;
    Order unfinishedOrder;
    Order finishedOrder1;
    Order finishedOrder2;
    Order orderToFinish;
    OrderDto finishedOrderDto1;
    OrderDto finishedOrderDto2;
    OrderDto unfinishedOrderDto;
    CloseUnfinishedOrderDto closeUnfinishedOrderDto;
    PositionInOrder positionInOrder1;
    PositionInOrder positionInOrder2;
    PositionInOrderDto positionInOrderDto1;
    PositionInOrderDto positionInOrderDto2;

    @BeforeEach
    void prepareEntities() {
        position1 = new Position(1L, "positionName",
                BigDecimal.valueOf(1),
                1D, 1D, 1D, 1D,
                false, "Ingr");
        position2 = new Position(2L, "AnotherPositionName",
                BigDecimal.valueOf(1),
                1D, 1D, 1D, 1D,
                false, "Ingr");
        positionDto1 = new PositionDto(1L, "positionName",
                BigDecimal.valueOf(1),
                1D, 1D, 1D, 1D,
                false, "Ingr");
        positionDto2 = new PositionDto(2L, "AnotherPositionName",
                BigDecimal.valueOf(1),
                1D, 1D, 1D, 1D,
                false, "Ingr");
        unfinishedOrder = new Order(3L, 1L,
                null, null, false);
        finishedOrder1 = new Order(1L, 1L,
                new Table(1L, 1, 1),
                Date.valueOf(LocalDate.now().plusWeeks(1)),
                true);
        finishedOrder2 = new Order(2L, 1L,
                new Table(1L, 1, 1),
                Date.valueOf(LocalDate.now().plusWeeks(2)),
                true);
        finishedOrderDto1 = new OrderDto(1L, 1L, finishedOrder1.getOrderDate(),
                new TableDto(1L, 1, 1));
        finishedOrderDto2 = new OrderDto(2L, 1L, finishedOrder2.getOrderDate(),
                new TableDto(1L, 1, 1));
        unfinishedOrderDto = new OrderDto(3L, 1L,
                null, null);
        closeUnfinishedOrderDto = new CloseUnfinishedOrderDto(3L, 1L,
                Date.valueOf(LocalDate.now().plusWeeks(3)),
                new TableDto(1L, 1, 1));
        orderToFinish = new Order(3L, 1L,
                new Table(1L, 1, 1),
                Date.valueOf(LocalDate.now().plusWeeks(3)),
                true);
        positionInOrder1 = new PositionInOrder(1L, 1, position1, unfinishedOrder);
        positionInOrder2 = new PositionInOrder(2L, 1, position2, finishedOrder1);
        positionInOrderDto1 = new PositionInOrderDto(1L, 1, positionDto1, unfinishedOrderDto);
        positionInOrderDto2 = new PositionInOrderDto(2L, 1, positionDto2, finishedOrderDto1);
    }

    @Test
    void getOrderByIdTest() {
        when(orderRepository.getById(finishedOrder1.getId())).thenReturn(finishedOrder1);
        when(orderMapper.map(finishedOrder1)).thenReturn(finishedOrderDto1);

        OperationResult<OrderDto> result = orderService.getById(finishedOrder1.getId());

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getResult()).isEqualTo(finishedOrderDto1);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(longs = {-1L})
    void getOrderByInvalidIdTest(Long orderId) {
        OperationResult<OrderDto> result = orderService.getById(orderId);

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getResult()).isNull();
    }

    @Test
    void getUnexistingOrderByIdTest() {
        when(orderRepository.getById(finishedOrder1.getId())).thenReturn(null);

        OperationResult<OrderDto> result = orderService.getById(finishedOrder1.getId());

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getResult()).isNull();
    }

    @Test
    void getFinishedOrdersByUserIdTest() {
        when(orderRepository.getFinishedByUserId(1L))
                .thenReturn(List.of(finishedOrder1, finishedOrder2));
        when(orderMapper.map(finishedOrder1)).thenReturn(finishedOrderDto1);
        when(orderMapper.map(finishedOrder2)).thenReturn(finishedOrderDto2);

        OperationResult<List<OrderDto>> result = orderService.getFinishedOrdersByUserId(1L);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getResult()).isEqualTo(List.of(finishedOrderDto1, finishedOrderDto2));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(longs = {-1L})
    void getFinishedOrdersByInvalidUserIdTest(Long userId) {
        OperationResult<List<OrderDto>> result = orderService.getFinishedOrdersByUserId(userId);

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getResult()).isNull();
    }

    @Test
    void getFinishedOrderPositionsTest() {
        when(positionInOrderRepository.getAllByOrderId(finishedOrder1.getId()))
                .thenReturn(List.of(positionInOrder2));
        when(positionInOrderMapper.map(positionInOrder2)).thenReturn(positionInOrderDto2);

        OperationResult<List<PositionInOrderDto>> result =
                orderService.getFinishedOrderPositions(finishedOrder1.getId());

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getResult()).isEqualTo(List.of(positionInOrderDto2));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(longs = {-1L})
    void getFinishedOrderPositionsTest(Long orderId) {
        OperationResult<List<PositionInOrderDto>> result =
                orderService.getFinishedOrderPositions(orderId);

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getResult()).isNull();
    }

    @Test
    void openNewOrderByUserIdTest() {
        when(orderRepository.add(any())).thenReturn(unfinishedOrder);
        when(orderMapper.map(unfinishedOrder)).thenReturn(unfinishedOrderDto);

        OperationResult<OrderDto> result = orderService.openNewOrderByUserId(1L);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getResult()).isEqualTo(unfinishedOrderDto);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(longs = {-1L})
    void openNewOrderByInvalidUserIdTest(Long userId) {
        OperationResult<OrderDto> result = orderService.openNewOrderByUserId(userId);

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getResult()).isNull();
    }

    @Test
    void openNewOrderByUserIdFailTest() {
        when(orderRepository.add(any())).thenReturn(null);

        OperationResult<OrderDto> result = orderService.openNewOrderByUserId(1L);

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getResult()).isNull();
    }

    @Test
    void getUnfinishedOrderByUserIdTest() {
        when(orderRepository.getUnfinishedByUserId(unfinishedOrder.getUserId()))
                .thenReturn(unfinishedOrder);
        when(orderMapper.map(unfinishedOrder)).thenReturn(unfinishedOrderDto);

        OperationResult<OrderDto> result = orderService.getUnfinishedOrderByUserId(unfinishedOrder.getUserId());

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getResult()).isEqualTo(unfinishedOrderDto);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(longs = {-1L})
    void getUnfinishedOrderByInvalidUserIdTest(Long userId) {
        OperationResult<OrderDto> result = orderService.getUnfinishedOrderByUserId(userId);

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getResult()).isNull();
    }

    @Test
    void getUnfinishedOrderByUserIdWithUnexistingUnfinishedOrderTest() {
        when(orderRepository.getUnfinishedByUserId(unfinishedOrder.getUserId()))
                .thenReturn(null);
        when(orderMapper.map((Order) any())).thenReturn(null);
        when(orderRepository.add(any())).thenReturn(unfinishedOrder);
        when(orderMapper.map(unfinishedOrder)).thenReturn(unfinishedOrderDto);

        OperationResult<OrderDto> result = orderService.getUnfinishedOrderByUserId(unfinishedOrder.getUserId());

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getResult()).isEqualTo(unfinishedOrderDto);
    }

    @Test
    void getUnfinishedOrderByUserIdFailTest() {
        when(orderRepository.getUnfinishedByUserId(unfinishedOrder.getUserId()))
                .thenReturn(null);
        when(orderMapper.map((Order) any())).thenReturn(null);
        when(orderRepository.add(any())).thenReturn(null);

        OperationResult<OrderDto> result = orderService.getUnfinishedOrderByUserId(unfinishedOrder.getUserId());

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getResult()).isNull();
    }

    @Test
    void getUnfinishedOrderPositionsByUserIdTest() {
        when(orderRepository.getUnfinishedByUserId(unfinishedOrder.getUserId()))
                .thenReturn(unfinishedOrder);
        when(orderMapper.map(unfinishedOrder)).thenReturn(unfinishedOrderDto);
        when(positionInOrderRepository.getAllByOrderId(unfinishedOrder.getId()))
                .thenReturn(List.of(positionInOrder1));
        when(positionInOrderMapper.map(positionInOrder1)).thenReturn(positionInOrderDto1);

        OperationResult<List<PositionInOrderDto>> result =
                orderService.getUnfinishedOrderPositionsByUserId(unfinishedOrder.getUserId());

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getResult()).isEqualTo(List.of(positionInOrderDto1));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(longs = {-1L})
    void getUnfinishedOrderPositionsByInvalidUserIdTest(Long userId) {
        OperationResult<List<PositionInOrderDto>> result =
                orderService.getUnfinishedOrderPositionsByUserId(userId);

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getResult()).isNull();
    }

    @Test
    void getUnfinishedOrderPositionsByUserIdWithUnexistingUnfinishedOrderTest() {
        when(orderRepository.getUnfinishedByUserId(unfinishedOrder.getUserId()))
                .thenReturn(null);
        when(orderMapper.map((Order) any())).thenReturn(null);
        when(orderRepository.add(any())).thenReturn(unfinishedOrder);
        when(orderMapper.map(unfinishedOrder)).thenReturn(unfinishedOrderDto);
        when(positionInOrderRepository.getAllByOrderId(unfinishedOrder.getId()))
                .thenReturn(List.of(positionInOrder1));
        when(positionInOrderMapper.map(positionInOrder1)).thenReturn(positionInOrderDto1);

        OperationResult<List<PositionInOrderDto>> result =
                orderService.getUnfinishedOrderPositionsByUserId(unfinishedOrder.getUserId());

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getResult()).isEqualTo(List.of(positionInOrderDto1));
    }

    @Test
    void getUnfinishedOrderPositionsByUserIdFailTest() {
        when(orderRepository.getUnfinishedByUserId(unfinishedOrder.getUserId()))
                .thenReturn(null);
        when(orderMapper.map((Order) any())).thenReturn(null);
        when(orderRepository.add(any())).thenReturn(null);

        OperationResult<List<PositionInOrderDto>> result =
                orderService.getUnfinishedOrderPositionsByUserId(unfinishedOrder.getUserId());

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getResult()).isNull();
    }

    @Test
    void addNewPositionToOrderTest() {
        when(positionRepository.getById(position1.getId())).thenReturn(position1);
        when(orderRepository.getUnfinishedByUserId(unfinishedOrder.getUserId()))
                .thenReturn(unfinishedOrder);
        when(positionInOrderRepository.getByOrderAndPositionId(unfinishedOrder.getId(),position1.getId()))
                .thenReturn(null);
        when(positionInOrderRepository.add(any())).thenReturn(positionInOrder1);
        when(positionInOrderMapper.map(positionInOrder1)).thenReturn(positionInOrderDto1);

        OperationResult<PositionInOrderDto> result = orderService.addPositionToOrder(1L, position1.getId());

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getResult()).isEqualTo(positionInOrderDto1);
    }

    @Test
    void addExtraPositionToOrderTest() {
        when(positionRepository.getById(position1.getId())).thenReturn(position1);
        when(orderRepository.getUnfinishedByUserId(unfinishedOrder.getUserId()))
                .thenReturn(unfinishedOrder);
        when(positionInOrderRepository.getByOrderAndPositionId(unfinishedOrder.getId(),position1.getId()))
                .thenReturn(positionInOrder1);
        when(positionInOrderRepository.update(positionInOrder1)).thenReturn(positionInOrder1);
        when(positionInOrderMapper.map(positionInOrder1)).thenReturn(positionInOrderDto1);

        OperationResult<PositionInOrderDto> result = orderService.addPositionToOrder(1L, position1.getId());

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getResult()).isEqualTo(positionInOrderDto1);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(longs = {-1L})
    void addPositionToOrderWithInvalidUserIdTest(Long userId) {
        OperationResult<PositionInOrderDto> result = orderService.addPositionToOrder(userId, position1.getId());

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getResult()).isNull();
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(longs = {-1L})
    void addPositionToOrderWithInvalidPositionIdTest(Long positionId) {
        OperationResult<PositionInOrderDto> result = orderService.addPositionToOrder(1L, positionId);

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getResult()).isNull();
    }

    @Test
    void addPositionToOrderWithUnexistingPositionIdTest() {
        when(positionRepository.getById(position1.getId())).thenReturn(null);

        OperationResult<PositionInOrderDto> result = orderService.addPositionToOrder(1L, position1.getId());

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getResult()).isNull();
    }

    @Test
    void addNewPositionToOrderWithUnexistingUnfinishedOrderTest() {
        when(positionRepository.getById(position1.getId())).thenReturn(position1);
        when(orderRepository.getUnfinishedByUserId(unfinishedOrder.getUserId()))
                .thenReturn(null);
        when(orderRepository.add(any())).thenReturn(unfinishedOrder);
        when(positionInOrderRepository.getByOrderAndPositionId(unfinishedOrder.getId(),position1.getId()))
                .thenReturn(null);
        when(positionInOrderRepository.add(any())).thenReturn(positionInOrder1);
        when(positionInOrderMapper.map(positionInOrder1)).thenReturn(positionInOrderDto1);

        OperationResult<PositionInOrderDto> result = orderService.addPositionToOrder(1L, position1.getId());

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getResult()).isEqualTo(positionInOrderDto1);
    }

    @Test
    void addExtraPositionToOrderWithUnexistingUnfinishedOrderTest() {
        when(positionRepository.getById(position1.getId())).thenReturn(position1);
        when(orderRepository.getUnfinishedByUserId(unfinishedOrder.getUserId()))
                .thenReturn(null);
        when(orderRepository.add(any())).thenReturn(unfinishedOrder);
        when(positionInOrderRepository.getByOrderAndPositionId(unfinishedOrder.getId(),position1.getId()))
                .thenReturn(positionInOrder1);
        when(positionInOrderRepository.update(positionInOrder1)).thenReturn(positionInOrder1);
        when(positionInOrderMapper.map(positionInOrder1)).thenReturn(positionInOrderDto1);

        OperationResult<PositionInOrderDto> result = orderService.addPositionToOrder(1L, position1.getId());

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getResult()).isEqualTo(positionInOrderDto1);
    }

    @Test
    void addPositionToOrderWithFailOnCreatingUnfinishedOrderTest() {
        when(positionRepository.getById(position1.getId())).thenReturn(position1);
        when(orderRepository.getUnfinishedByUserId(unfinishedOrder.getUserId()))
                .thenReturn(null);
        when(orderRepository.add(any())).thenReturn(null);

        OperationResult<PositionInOrderDto> result = orderService.addPositionToOrder(1L, position1.getId());

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getResult()).isNull();
    }

    @Test
    void addNewPositionToOrderWithFailOnAddingPositionInOrderTest() {
        when(positionRepository.getById(position1.getId())).thenReturn(position1);
        when(orderRepository.getUnfinishedByUserId(unfinishedOrder.getUserId()))
                .thenReturn(unfinishedOrder);
        when(positionInOrderRepository.getByOrderAndPositionId(unfinishedOrder.getId(),position1.getId()))
                .thenReturn(null);
        when(positionInOrderRepository.add(any())).thenReturn(null);

        OperationResult<PositionInOrderDto> result = orderService.addPositionToOrder(1L, position1.getId());

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getResult()).isNull();
    }

    @Test
    void addExtraPositionToOrderWithFailOnUpdatingPositionInOrderTest() {
        when(positionRepository.getById(position1.getId())).thenReturn(position1);
        when(orderRepository.getUnfinishedByUserId(unfinishedOrder.getUserId()))
                .thenReturn(unfinishedOrder);
        when(positionInOrderRepository.getByOrderAndPositionId(unfinishedOrder.getId(),position1.getId()))
                .thenReturn(positionInOrder1);
        when(positionInOrderRepository.update(positionInOrder1)).thenReturn(null);

        OperationResult<PositionInOrderDto> result = orderService.addPositionToOrder(1L, position1.getId());

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getResult()).isNull();
    }

    @Test
    void removePositionWithMultipleCountFromOrderTest() {
        when(positionRepository.getById(position1.getId())).thenReturn(position1);
        when(orderRepository.getUnfinishedByUserId(unfinishedOrder.getUserId()))
                .thenReturn(unfinishedOrder);
        positionInOrder1.setPositionCount(2);
        when(positionInOrderRepository.getByOrderAndPositionId(unfinishedOrder.getId(),
                position1.getId())).thenReturn(positionInOrder1);
        when(positionInOrderRepository.update(positionInOrder1)).thenReturn(positionInOrder1);

        OperationResult<Boolean> result = orderService.removePositionFromOrder(unfinishedOrder.getUserId(), position1.getId());

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getResult()).isTrue();
    }

    @Test
    void removePositionFromOrderTest() {
        when(positionRepository.getById(position1.getId())).thenReturn(position1);
        when(orderRepository.getUnfinishedByUserId(unfinishedOrder.getUserId()))
                .thenReturn(unfinishedOrder);
        when(positionInOrderRepository.getByOrderAndPositionId(unfinishedOrder.getId(),
                position1.getId())).thenReturn(positionInOrder1);
        when(positionInOrderRepository.delete(positionInOrder1.getId())).thenReturn(true);

        OperationResult<Boolean> result = orderService.removePositionFromOrder(unfinishedOrder.getUserId(), position1.getId());

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getResult()).isTrue();
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(longs = {-1L})
    void removePositionFromOrderWithInvalidUserIdTest(Long userId) {
        OperationResult<Boolean> result = orderService.removePositionFromOrder(userId, position1.getId());

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getResult()).isNull();
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(longs = {-1L})
    void removePositionFromOrderWithInvalidPositionIdTest(Long positionId) {
        OperationResult<Boolean> result = orderService.removePositionFromOrder(unfinishedOrder.getUserId(), positionId);

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getResult()).isNull();
    }

    @Test
    void removePositionFromOrderWithUnexistingPositionTest() {
        when(positionRepository.getById(position1.getId())).thenReturn(null);

        OperationResult<Boolean> result = orderService.removePositionFromOrder(unfinishedOrder.getUserId(), position1.getId());

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getResult()).isNull();
    }

    @Test
    void removePositionFromOrderWithUnexistingUnfinishedOrderTest() {
        when(positionRepository.getById(position1.getId())).thenReturn(position1);
        when(orderRepository.getUnfinishedByUserId(unfinishedOrder.getUserId()))
                .thenReturn(null);

        OperationResult<Boolean> result = orderService.removePositionFromOrder(unfinishedOrder.getUserId(), position1.getId());

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getResult()).isNull();
    }

    @Test
    void removePositionFromOrderWhenPositionNotListedInOrderTest() {
        when(positionRepository.getById(position1.getId())).thenReturn(position1);
        when(orderRepository.getUnfinishedByUserId(unfinishedOrder.getUserId()))
                .thenReturn(unfinishedOrder);
        when(positionInOrderRepository.getByOrderAndPositionId(unfinishedOrder.getId(),
                position1.getId())).thenReturn(null);

        OperationResult<Boolean> result = orderService.removePositionFromOrder(unfinishedOrder.getUserId(), position1.getId());

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getResult()).isNull();
    }

    @Test
    void removePositionWithMultipleCountFromOrderFailTest() {
        when(positionRepository.getById(position1.getId())).thenReturn(position1);
        when(orderRepository.getUnfinishedByUserId(unfinishedOrder.getUserId()))
                .thenReturn(unfinishedOrder);
        positionInOrder1.setPositionCount(2);
        when(positionInOrderRepository.getByOrderAndPositionId(unfinishedOrder.getId(),
                position1.getId())).thenReturn(positionInOrder1);
        when(positionInOrderRepository.update(positionInOrder1)).thenReturn(null);

        OperationResult<Boolean> result = orderService.removePositionFromOrder(unfinishedOrder.getUserId(), position1.getId());

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getResult()).isNull();
    }

    @Test
    void removePositionFromOrderFailTest() {
        when(positionRepository.getById(position1.getId())).thenReturn(position1);
        when(orderRepository.getUnfinishedByUserId(unfinishedOrder.getUserId()))
                .thenReturn(unfinishedOrder);
        when(positionInOrderRepository.getByOrderAndPositionId(unfinishedOrder.getId(),
                position1.getId())).thenReturn(positionInOrder1);
        when(positionInOrderRepository.delete(positionInOrder1.getId())).thenReturn(false);

        OperationResult<Boolean> result = orderService.removePositionFromOrder(unfinishedOrder.getUserId(), position1.getId());

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getResult()).isNull();
    }

    @Test
    void closeOpenedOrderTest() {
        when(orderValidator.isCloseUnfinishedValid(closeUnfinishedOrderDto)).thenReturn(true);
        when(orderMapper.map(closeUnfinishedOrderDto)).thenReturn(orderToFinish);
        when(orderRepository.update(orderToFinish)).thenReturn(orderToFinish);

        OperationResult<Boolean> result = orderService.closeOpenedOrder(closeUnfinishedOrderDto);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getResult()).isTrue();
    }

    @Test
    void closeOpenedOrderWithIncorrectDataTest() {
        when(orderValidator.isCloseUnfinishedValid(closeUnfinishedOrderDto)).thenReturn(false);

        OperationResult<Boolean> result = orderService.closeOpenedOrder(closeUnfinishedOrderDto);

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getResult()).isNull();
    }

    @Test
    void closeOpenedOrderFailTest() {
        when(orderValidator.isCloseUnfinishedValid(closeUnfinishedOrderDto)).thenReturn(true);
        when(orderMapper.map(closeUnfinishedOrderDto)).thenReturn(orderToFinish);
        when(orderRepository.update(orderToFinish)).thenReturn(null);

        OperationResult<Boolean> result = orderService.closeOpenedOrder(closeUnfinishedOrderDto);

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getResult()).isNull();
    }
}
