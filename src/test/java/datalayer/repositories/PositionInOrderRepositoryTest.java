package datalayer.repositories;

import datalayer.TestConnectionProvider;
import org.example.restaurant.datalayer.ConnectionProvider;
import org.example.restaurant.datalayer.entities.Order;
import org.example.restaurant.datalayer.entities.Position;
import org.example.restaurant.datalayer.entities.PositionInOrder;
import org.example.restaurant.datalayer.entities.User;
import org.example.restaurant.datalayer.repositories.OrderRepository;
import org.example.restaurant.datalayer.repositories.PositionInOrderRepository;
import org.example.restaurant.datalayer.repositories.PositionRepository;
import org.example.restaurant.datalayer.repositories.TableRepository;
import org.example.restaurant.datalayer.repositories.UserRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PositionInOrderRepositoryTest {
    final String RESET_TABLE_QUERY = "TRUNCATE position_in_order RESTART IDENTITY CASCADE";
    final ConnectionProvider connectionProvider = TestConnectionProvider.getInstance();
    final TableRepository tableRepository = new TableRepository(connectionProvider);
    final UserRepository userRepository = new UserRepository(connectionProvider);
    final OrderRepository orderRepository = new OrderRepository(connectionProvider, tableRepository);
    final PositionRepository positionRepository = new PositionRepository(connectionProvider);
    final PositionInOrderRepository positionInOrderRepository = new PositionInOrderRepository(connectionProvider,
            positionRepository, orderRepository);
    private User user;
    private Order order1;
    private Order order2;
    private Position position1;
    private Position position2;

    @BeforeAll
    public void fillConnectedTables() {
        user = userRepository.add(new User(null, "user", "pass", "phone", "user"));
        order1 = orderRepository.add(new Order(null, user.getId(), null, null, false));
        order2 = orderRepository.add(new Order(null, user.getId(), null, null, false));
        position1 = positionRepository.add(new Position(null, "pos1",
                BigDecimal.valueOf(1),
                1., 1., 1.,
                1., false, "Pos1Ingr"));
        position2 = positionRepository.add(new Position(null, "pos2",
                BigDecimal.valueOf(1),
                2., 2., 2.,
                2., true, "Pos2Ingr"));
    }

    @AfterAll
    public void clearConnectedTables() {
        positionRepository.delete(position2.getId());
        positionRepository.delete(position1.getId());
        orderRepository.delete(order2.getId());
        orderRepository.delete(order1.getId());
        userRepository.delete(user.getId());
    }

    @AfterEach
    @BeforeEach
    public void clearPositionInOrderTable() {
        try (Connection connection = connectionProvider.getConnection()) {
            connection.prepareStatement(RESET_TABLE_QUERY).executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Stream<List<PositionInOrder>> genPositionInOrderLists() {
        PositionInOrder positionInOrder1 = new PositionInOrder(null, 1,
                position1, order1);
        PositionInOrder positionInOrder2 = new PositionInOrder(null, 2,
                position2, order1);
        PositionInOrder positionInOrder3 = new PositionInOrder(null, 3,
                position1, order2);
        PositionInOrder positionInOrder4 = new PositionInOrder(null, 3,
                position2, order2);

        return Stream.of(
                new ArrayList<>(),
                List.of(
                        positionInOrder1
                ),
                List.of(
                        positionInOrder1,
                        positionInOrder2,
                        positionInOrder3,
                        positionInOrder4
                )
        );
    }

    @ParameterizedTest
    @MethodSource("genPositionInOrderLists")
    void getAllPositionInOrdersTest(List<PositionInOrder> positionInOrders) {
        for (PositionInOrder positionInOrder : positionInOrders) {
            positionInOrder.setId(positionInOrderRepository.add(positionInOrder).getId());
        }

        List<PositionInOrder> gotPositionInOrders = positionInOrderRepository.getAll();
        assertThat(gotPositionInOrders).isEqualTo(positionInOrders);
    }

    @ParameterizedTest
    @MethodSource("genPositionInOrderLists")
    void getAllPositionInOrdersByOrderIdTest(List<PositionInOrder> positionInOrders) {
        Long orderId = order1.getId();

        for (PositionInOrder positionInOrder : positionInOrders) {
            positionInOrder.setId(positionInOrderRepository.add(positionInOrder).getId());
        }

        List<PositionInOrder> gotPositionInOrders = positionInOrderRepository.getAllByOrderId(orderId);
        assertThat(gotPositionInOrders).isEqualTo(positionInOrders.stream()
                .filter(positionInOrder -> positionInOrder.getOrderId().equals(orderId))
                .toList());
    }

    @ParameterizedTest
    @MethodSource("genPositionInOrderLists")
    void getAllPositionInOrdersByUnexisitngOrderIdTest(List<PositionInOrder> positionInOrders) {
        Long orderId = -1L;

        for (PositionInOrder positionInOrder : positionInOrders) {
            positionInOrder.setId(positionInOrderRepository.add(positionInOrder).getId());
        }

        List<PositionInOrder> gotPositionInOrders = positionInOrderRepository.getAllByOrderId(orderId);
        assertThat(gotPositionInOrders).isEmpty();
    }

    @Test
    void getPositionInOrderByIdTest() {
        PositionInOrder positionInOrder = new PositionInOrder(null, 1, position1, order1);

        positionInOrder.setId(positionInOrderRepository.add(positionInOrder).getId());

        PositionInOrder actualPositionInOrder = positionInOrderRepository.getById(positionInOrder.getId());

        assertThat(actualPositionInOrder).isEqualTo(positionInOrder);
    }

    @Test
    void getUnexistingPositionInOrderByIdTest() {
        PositionInOrder positionInOrder = positionInOrderRepository.getById(-1L);
        assertThat(positionInOrder).isNull();
    }

    @Test
    void getPositionInOrderByOrderAndPositionIdTest() {
        PositionInOrder positionInOrder = new PositionInOrder(null, 1, position1, order1);

        positionInOrder.setId(positionInOrderRepository.add(positionInOrder).getId());

        PositionInOrder actualPositionInOrder = positionInOrderRepository
                .getByOrderAndPositionId(order1.getId(), position1.getId());

        assertThat(actualPositionInOrder).isEqualTo(positionInOrder);
    }

    @Test
    void getPositionInOrderByUnexistingOrderAndPositionIdTest() {
        PositionInOrder positionInOrder = new PositionInOrder(null, 1, position1, order1);

        positionInOrder.setId(positionInOrderRepository.add(positionInOrder).getId());

        PositionInOrder actualPositionInOrder = positionInOrderRepository
                .getByOrderAndPositionId(-1L, position1.getId());

        assertThat(actualPositionInOrder).isNull();
    }

    @Test
    void getPositionInOrderByOrderAndUnexistingPositionIdTest() {
        PositionInOrder positionInOrder = new PositionInOrder(null, 1, position1, order1);

        positionInOrder.setId(positionInOrderRepository.add(positionInOrder).getId());

        PositionInOrder actualPositionInOrder = positionInOrderRepository
                .getByOrderAndPositionId(order1.getId(), -1L);

        assertThat(actualPositionInOrder).isNull();
    }

    @Test
    void addPositionInOrderTest() {
        PositionInOrder positionInOrder = new PositionInOrder(null, 1, position1, order1);

        positionInOrder.setId(positionInOrderRepository.add(positionInOrder).getId());

        PositionInOrder actualPositionInOrder = positionInOrderRepository.getById(positionInOrder.getId());

        assertThat(actualPositionInOrder).isEqualTo(positionInOrder);
    }

    @Test
    void updateUnexistingPositionInOrderTest() {
        PositionInOrder positionInOrder = new PositionInOrder(-1L, 1, position1, order1);

        positionInOrderRepository.update(positionInOrder);

        PositionInOrder savedPositionInOrder = positionInOrderRepository.getById(positionInOrder.getId());

        assertThat(savedPositionInOrder).isNull();
    }

    @Test
    void updatePositionInOrderTest() {
        PositionInOrder positionInOrder = new PositionInOrder(1L, 1, position1, order1);

        positionInOrder = positionInOrderRepository.add(positionInOrder);
        positionInOrder.setPositionCount(positionInOrder.getPositionCount() + 2);

        positionInOrderRepository.update(positionInOrder);
        PositionInOrder savedPositionInOrder = positionInOrderRepository.getById(positionInOrder.getId());

        assertThat(savedPositionInOrder).isEqualTo(positionInOrder);
    }

    @Test
    void deleteUnexistingPositionInOrderTest() {
        assertThat(orderRepository.delete(-1L)).isFalse();
    }

    @Test
    void deletePositionInOrderTest() {
        PositionInOrder positionInOrder = new PositionInOrder(null, 1, position1, order1);

        positionInOrder = positionInOrderRepository.add(positionInOrder);

        assertThat(positionInOrderRepository.delete(positionInOrder.getId())).isTrue();
    }
}
