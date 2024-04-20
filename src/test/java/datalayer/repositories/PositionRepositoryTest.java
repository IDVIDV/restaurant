package datalayer.repositories;

import datalayer.TestConnectionProvider;
import org.example.restaurant.datalayer.ConnectionProvider;
import org.example.restaurant.datalayer.entities.Position;
import org.example.restaurant.datalayer.exceptions.DataBaseException;
import org.example.restaurant.datalayer.repositories.PositionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PositionRepositoryTest {
    final String RESET_TABLE_QUERY = "TRUNCATE position RESTART IDENTITY CASCADE";
    final ConnectionProvider connectionProvider = TestConnectionProvider.getInstance();
    final PositionRepository positionRepository = new PositionRepository(connectionProvider);

    @AfterEach
    @BeforeEach
    public void clearPositionTable() {
        try (Connection connection = connectionProvider.getConnection()) {
            connection.prepareStatement(RESET_TABLE_QUERY).executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static Stream<List<Position>> genPositionLists() {
        Position pos1 = new Position(null, "pos1",
                BigDecimal.valueOf(1),
                1., 1., 1.,
                1., false, "Pos1Ingr");

        Position pos2 = new Position(null, "NotPos2",
                BigDecimal.valueOf(2),
                2., 2., 2.,
                2., true, "");

        return Stream.of(
                new ArrayList<>(),
                List.of(
                        pos1
                ),
                List.of(
                        pos1,
                        pos2
                ),
                List.of(
                        pos2,
                        pos2
                )
        );
    }

    private static Stream<Position> genInvalidPositions() {
        return Stream.of(
                new Position(null, null,
                        null,
                        null, null, null,
                        null, null, null),

                new Position(null, "NotPos2",
                        null,
                        2., 2., 2.,
                        2., true, ""),
                new Position(null, "NotPos2",
                        null,
                        2., 2., 2.,
                        2., true, "")
        );
    }

    @ParameterizedTest
    @MethodSource("genPositionLists")
    void getAllPositionsTest(List<Position> positions) {
        for (Position pos : positions) {
            pos.setId(positionRepository.add(pos).getId());
        }

        List<Position> gotPositions = positionRepository.getAll();
        assertThat(gotPositions).isEqualTo(positions);
    }

    @ParameterizedTest
    @MethodSource("genPositionLists")
    void getPositionsByNameTest(List<Position> positions) {
        for (Position pos : positions) {
            pos.setId(positionRepository.add(pos).getId());
        }
        String name = "pos";

        List<Position> gotPositions = positionRepository.getByName("pos");
        assertThat(gotPositions).allSatisfy(elem -> elem.getPositionName().startsWith(name));
    }

    @Test
    void getPositionByIdTest() {
        Position pos = new Position(null, "pos1",
                BigDecimal.valueOf(1),
                1., 1., 1.,
                1., false, "Pos1Ingr");

        pos.setId(positionRepository.add(pos).getId());

        Position actualPos = positionRepository.getById(pos.getId());

        assertThat(actualPos).isEqualTo(pos);
    }

    @Test
    void getUnexistingPositionByIdTest() {
        Position pos = positionRepository.getById(1L);
        assertThat(pos).isNull();
    }

    @Test
    void addPositionTest() {
        Position pos = new Position(null, "pos1",
                BigDecimal.valueOf(1),
                1., 1., 1.,
                1., false, "Pos1Ingr");

        pos.setId(positionRepository.add(pos).getId());

        Position actualPos = positionRepository.getById(pos.getId());

        assertThat(actualPos).isEqualTo(pos);
    }

    @ParameterizedTest
    @MethodSource("genInvalidPositions")
    void addInvalidPositionTest(Position pos) {
        assertThatThrownBy(() -> positionRepository.add(pos)).isInstanceOf(DataBaseException.class);
    }

    @Test
    void updateUnexistingPositionTest() {
        Position pos = new Position(-1L, "pos1",
                BigDecimal.valueOf(1),
                1., 1., 1.,
                1., false, "Pos1Ingr");

        positionRepository.update(pos);

        Position savedPos = positionRepository.getById(pos.getId());

        assertThat(savedPos).isNull();
    }

    @Test
    void updatePositionTest() {
        Position pos = new Position(null, "pos1",
                BigDecimal.valueOf(1),
                1., 1., 1.,
                1., false, "Pos1Ingr");
        pos = positionRepository.add(pos);
        pos.setPositionName("NewName");
        pos.setIngredients("NewIngr");

        positionRepository.update(pos);
        Position savedPos = positionRepository.getById(pos.getId());

        assertThat(savedPos).isEqualTo(pos);
    }

    @Test
    void deleteUnexistingPositionTest() {
        assertThat(positionRepository.delete(-1L)).isFalse();
    }

    @Test
    void deletePositionTest() {
        Position pos = new Position(null, "pos1",
                BigDecimal.valueOf(1),
                1., 1., 1.,
                1., false, "Pos1Ingr");

        pos = positionRepository.add(pos);

        assertThat(positionRepository.delete(pos.getId())).isTrue();
    }
}
