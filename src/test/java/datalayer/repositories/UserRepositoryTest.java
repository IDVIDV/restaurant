package datalayer.repositories;

import datalayer.TestConnectionProvider;
import org.example.restaurant.datalayer.ConnectionProvider;
import org.example.restaurant.datalayer.entities.User;
import org.example.restaurant.datalayer.exceptions.DataBaseException;
import org.example.restaurant.datalayer.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UserRepositoryTest {
    final String RESET_TABLE_QUERY = "TRUNCATE \"user\" RESTART IDENTITY CASCADE";
    final ConnectionProvider connectionProvider = TestConnectionProvider.getInstance();
    final UserRepository userRepository = new UserRepository(connectionProvider);

    @AfterEach
    @BeforeEach
    public void clearUserTable() {
        try (Connection connection = connectionProvider.getConnection()) {
            connection.prepareStatement(RESET_TABLE_QUERY).executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static Stream<List<User>> genUserLists() {
        User user1 = new User(null, "user1",
                "pass1", "1234567890", "user");

        User user2 = new User(null, "user2",
                "pass2", "1234567891", "admin");


        return Stream.of(
                new ArrayList<>(),
                List.of(
                        user1
                ),
                List.of(
                        user1,
                        user2
                )
        );
    }

    private static Stream<User> genInvalidUsers() {
        return Stream.of(
                new User(null, null,
                        "pass1", "1234567890", "user"),
                new User(null, "user2",
                        null, "1234567892", "")
        );
    }

    @ParameterizedTest
    @MethodSource("genUserLists")
    void getAllUsersTest(List<User> users) {
        for (User user : users) {
            user.setId(userRepository.add(user).getId());
        }

        List<User> gotUsers = userRepository.getAll();
        assertThat(gotUsers).isEqualTo(users);
    }

    @Test
    void getUserByIdTest() {
            User user = new User(null, "user1",
                    "pass1", "1234567890", "user");

        user.setId(userRepository.add(user).getId());

        User actualUser = userRepository.getById(user.getId());

        assertThat(actualUser).isEqualTo(user);
    }

    @Test
    void getUnexistingUserByIdTest() {
        User user = userRepository.getById(-1L);
        assertThat(user).isNull();
    }

    @Test
    void getUserByLoginTest() {
        User user = new User(null, "user1",
                "pass1", "1234567890", "user");

        user.setId(userRepository.add(user).getId());

        User actualUser = userRepository.getByLogin(user.getLogin());

        assertThat(actualUser).isEqualTo(user);
    }

    @Test
    void getUnexistingUserByLoginTest() {
        User user = userRepository.getByLogin("");
        assertThat(user).isNull();
    }

    @Test
    void addUserTest() {
        User user = new User(null, "user1",
                "pass1", "1234567890", "user");

        user.setId(userRepository.add(user).getId());

        User actualUser = userRepository.getById(user.getId());

        assertThat(actualUser).isEqualTo(user);
    }

    @Test
    void addSameUserTest() {
        User user = new User(null, "user1",
                "pass1", "1234567890", "user");

        user.setId(userRepository.add(user).getId());

        assertThatThrownBy(() -> {
            userRepository.add(user);
        }).isInstanceOf(DataBaseException.class);
    }

    @ParameterizedTest
    @MethodSource("genInvalidUsers")
    void addInvalidUserTest(User user) {
        assertThatThrownBy(() -> {
            userRepository.add(user);
        }).isInstanceOf(DataBaseException.class);
    }

    @Test
    void updateUnexistingUserTest() {
        User user = new User(1L, "user1",
                "pass1", "1234567890", "user");

        userRepository.update(user);

        User savedUser = userRepository.getById(user.getId());

        assertThat(savedUser).isNull();
    }

    @Test
    void updateUserTest() {
        User user = new User(null, "user1",
                "pass1", "1234567890", "user");

        user = userRepository.add(user);
        user.setLogin("NewName");

        userRepository.update(user);
        User savedUser = userRepository.getById(user.getId());

        assertThat(savedUser).isEqualTo(user);
    }

    @Test
    void deleteUnexistingUserTest() {
        assertThat(userRepository.delete(-1L)).isFalse();
    }

    @Test
    void deleteUserTest() {
        User user = new User(null, "user1",
                "pass1", "1234567890", "user");

        user = userRepository.add(user);

        assertThat(userRepository.delete(user.getId())).isTrue();
    }
}
