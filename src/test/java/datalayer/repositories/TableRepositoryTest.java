package datalayer.repositories;

import datalayer.TestConnectionProvider;
import org.example.restaurant.datalayer.ConnectionProvider;
import org.example.restaurant.datalayer.entities.Table;
import org.example.restaurant.datalayer.exceptions.DataBaseException;
import org.example.restaurant.datalayer.repositories.TableRepository;
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

public class TableRepositoryTest {
    final String RESET_TABLE_QUERY = "TRUNCATE \"table\" RESTART IDENTITY CASCADE";
    final ConnectionProvider connectionProvider = TestConnectionProvider.getInstance();
    final TableRepository tableRepository = new TableRepository(connectionProvider);

    @AfterEach
    @BeforeEach
    public void clearTableTable() {
        try (Connection connection = connectionProvider.getConnection()) {
            connection.prepareStatement(RESET_TABLE_QUERY).executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static Stream<List<Table>> genTableLists() {
        Table table1 = new Table(null, 1, 10);
        Table table2 = new Table(null, 2, 5);


        return Stream.of(
                new ArrayList<>(),
                List.of(
                        table1
                ),
                List.of(
                        table1,
                        table2
                )
        );
    }

    private static Stream<Table> genInvalidTables() {
        return Stream.of(
                new Table(null, null, 10),
                new Table(null, 1, null)
        );
    }

    @ParameterizedTest
    @MethodSource("genTableLists")
    void getAllTablesTest(List<Table> tables) {
        for (Table table : tables) {
            table.setId(tableRepository.add(table).getId());
        }

        List<Table> gotTables = tableRepository.getAll();
        assertThat(gotTables).isEqualTo(tables);
    }

    @Test
    void getTableByIdTest() {
        Table table = new Table(null, 1, 10);

        table.setId(tableRepository.add(table).getId());

        Table actualTable = tableRepository.getById(table.getId());

        assertThat(actualTable).isEqualTo(table);
    }

    @Test
    void getUnexistingTableByIdTest() {
        Table table = tableRepository.getById(-1L);
        assertThat(table).isNull();
    }

    @Test
    void addTableTest() {
        Table table = new Table(null, 1, 10);

        table.setId(tableRepository.add(table).getId());

        Table actualTable = tableRepository.getById(table.getId());

        assertThat(actualTable).isEqualTo(table);
    }

    @Test
    void addSameTableTest() {
        Table table = new Table(null, 1, 10);

        table.setId(tableRepository.add(table).getId());

        assertThatThrownBy(() -> {
            tableRepository.add(table);
        }).isInstanceOf(DataBaseException.class);
    }

    @ParameterizedTest
    @MethodSource("genInvalidTables")
    void addInvalidTableTest(Table table) {
        assertThatThrownBy(() -> {
            tableRepository.add(table);
        }).isInstanceOf(DataBaseException.class);
    }

    @Test
    void updateUnexistingTableTest() {
        Table table = new Table(-1L, 1, 10);

        tableRepository.update(table);

        Table savedTable = tableRepository.getById(table.getId());

        assertThat(savedTable).isNull();
    }

    @Test
    void updateTableTest() {
        Table table = new Table(null, 1, 10);

        table = tableRepository.add(table);
        table.setCapacity(15);

        tableRepository.update(table);
        Table savedTable = tableRepository.getById(table.getId());

        assertThat(savedTable).isEqualTo(table);
    }

    @Test
    void deleteUnexistingTableTest() {
        assertThat(tableRepository.delete(-1L)).isFalse();
    }

    @Test
    void deleteTableTest() {
        Table table = new Table(null, 1, 10);

        table = tableRepository.add(table);

        assertThat(tableRepository.delete(table.getId())).isTrue();
    }
}
