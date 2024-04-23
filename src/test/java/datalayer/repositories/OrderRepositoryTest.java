package datalayer.repositories;

import datalayer.TestConnectionProvider;
import org.example.restaurant.datalayer.ConnectionProvider;
import org.example.restaurant.datalayer.entities.Order;
import org.example.restaurant.datalayer.entities.Table;
import org.example.restaurant.datalayer.entities.User;
import org.example.restaurant.datalayer.exceptions.DataBaseException;
import org.example.restaurant.datalayer.repositories.OrderRepository;
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

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OrderRepositoryTest {
    final String RESET_TABLE_QUERY = "TRUNCATE \"order\" RESTART IDENTITY CASCADE";
    final ConnectionProvider connectionProvider = new TestConnectionProvider();

    final UserRepository userRepository = new UserRepository(connectionProvider);
    final TableRepository tableRepository = new TableRepository(connectionProvider);
    final OrderRepository orderRepository = new OrderRepository(connectionProvider, tableRepository);

    User user;
    Table table1;
    Table table2;

    @BeforeAll
    public void fillConnectedTable() {
        table1 = tableRepository.add(new Table(null, 1, 10));
        table2 = tableRepository.add(new Table(null, 2, 5));
        user = userRepository.add(new User(null, "user", "user", "user", "user"));
    }

    @AfterAll
    public void clearConnectedTables() {
        tableRepository.delete(table1.getId());
        tableRepository.delete(table2.getId());
        userRepository.delete(user.getId());
    }

    @AfterEach
    @BeforeEach
    public void clearOrderTable() {
        try (Connection connection = connectionProvider.getConnection()) {
            connection.prepareStatement(RESET_TABLE_QUERY).executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Stream<List<Order>> genOrderLists() {
        Order order1 = new Order(null, user.getId(), table1,
                Date.valueOf(LocalDate.now().plusWeeks(2)),
                true);

        Order order2 = new Order(null, user.getId(), table2,
                Date.valueOf(LocalDate.now().plusWeeks(2)),
                true);

        Order order3 = new Order(null, user.getId(), table2,
                Date.valueOf(LocalDate.now().plusWeeks(3)),
                true);

        Order order4 = new Order(null, user.getId(), null,
                null, false);

        return Stream.of(
                new ArrayList<>(),
                List.of(
                        order1
                ),
                List.of(
                        order1,
                        order2,
                        order3,
                        order4
                ),
                List.of(
                        order4
                )
        );
    }

    private Stream<Order> genInvalidOrders() {
        return Stream.of(
                new Order(null, null, table1,
                        Date.valueOf(LocalDate.now().plusWeeks(2)),
                        true),
                new Order(null, null, table1,
                        Date.valueOf(LocalDate.now().plusWeeks(2)),
                        null)
        );
    }

    @ParameterizedTest
    @MethodSource("genOrderLists")
    void getAllOrdersTest(List<Order> orders) {
        for (Order order : orders) {
            order.setId(orderRepository.add(order).getId());
        }

        List<Order> gotOrders = orderRepository.getAll();
        assertThat(gotOrders).isEqualTo(orders);
    }

    @ParameterizedTest
    @MethodSource("genOrderLists")
    void getFinishedOrdersByUserId(List<Order> orders) {
        for (Order order : orders) {
            order.setId(orderRepository.add(order).getId());
        }

        List<Order> gotOrders = orderRepository.getFinishedByUserId(user.getId());
        assertThat(gotOrders).isEqualTo(orders.stream().filter(Order::isFinished).toList());
    }

    @ParameterizedTest
    @MethodSource("genOrderLists")
    void getUnFinishedOrdersByUserId(List<Order> orders) {
        for (Order order : orders) {
            order.setId(orderRepository.add(order).getId());
        }

        Order gotOrder = orderRepository.getUnfinishedByUserId(user.getId());
        List<Order> gotOrders = new ArrayList<>();
        if (Objects.nonNull(gotOrder)) {
            gotOrders.add(gotOrder);
        }

        assertThat(gotOrders)
                .isEqualTo(orders.stream().filter(order -> !order.isFinished()).toList());
    }

    @Test
    void getOrderByIdTest() {
        Order order = new Order(null, user.getId(), table1,
                Date.valueOf(LocalDate.now().plusWeeks(2)),
                true);

        order.setId(orderRepository.add(order).getId());

        Order actualOrder = orderRepository.getById(order.getId());

        assertThat(actualOrder).isEqualTo(order);
    }

    @Test
    void getUnexistingOrderByIdTest() {
        Order order = orderRepository.getById(1L);
        assertThat(order).isNull();
    }

    @Test
    void addOrderTest() {
        Order order = new Order(null, user.getId(), table1,
                Date.valueOf(LocalDate.now().plusWeeks(2)),
                true);

        order.setId(orderRepository.add(order).getId());

        Order actualOrder = orderRepository.getById(order.getId());

        assertThat(actualOrder).isEqualTo(order);
    }

    @Test
    void addOrderWithSameTableAndDateTest() {
        Order order = new Order(null, user.getId(), table1,
                Date.valueOf(LocalDate.now().plusWeeks(2)),
                true);

        order.setId(orderRepository.add(order).getId());

        assertThatThrownBy(() -> orderRepository.add(order)).isInstanceOf(DataBaseException.class);
    }

    @ParameterizedTest
    @MethodSource("genInvalidOrders")
    void addInvalidOrderTest(Order order) {
        assertThatThrownBy(() -> orderRepository.add(order)).isInstanceOf(DataBaseException.class);
    }

    @Test
    void updateUnexistingOrderTest() {
        Order order = new Order(-1L, user.getId(), table1,
                Date.valueOf(LocalDate.now().plusWeeks(2)),
                true);

        orderRepository.update(order);

        Order savedOrder = orderRepository.getById(order.getId());

        assertThat(savedOrder).isNull();
    }

    @Test
    void updateOrderTest() {
        Order order = new Order(1L, user.getId(), table1,
                Date.valueOf(LocalDate.now().plusWeeks(2)),
                true);

        order = orderRepository.add(order);
        order.setTable(table2);
        order.setOrderDate(Date.valueOf(LocalDate.now().plusWeeks(3)));

        orderRepository.update(order);
        Order savedOrder = orderRepository.getById(order.getId());

        assertThat(savedOrder).isEqualTo(order);
    }

    @Test
    void deleteUnexistingOrderTest() {
        assertThat(orderRepository.delete(-1L)).isFalse();
    }

    @Test
    void deleteOrderTest() {
        Order order = new Order(null, user.getId(), table1,
                Date.valueOf(LocalDate.now().plusWeeks(2)),
                true);

        order = orderRepository.add(order);

        assertThat(orderRepository.delete(order.getId())).isTrue();
    }
}
