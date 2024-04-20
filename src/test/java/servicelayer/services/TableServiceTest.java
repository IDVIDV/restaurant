package servicelayer.services;

import org.example.restaurant.datalayer.dto.table.TableDto;
import org.example.restaurant.datalayer.entities.Table;
import org.example.restaurant.datalayer.mappers.TableMapper;
import org.example.restaurant.datalayer.repositories.TableRepository;
import org.example.restaurant.servicelayer.OperationResult;
import org.example.restaurant.servicelayer.services.TableService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TableServiceTest {
    @Mock
    TableMapper tableMapper;
    @Mock
    TableRepository tableRepository;
    @InjectMocks
    TableService tableService;

    Table table1;
    Table table2;
    TableDto tableDto1;
    TableDto tableDto2;

    @BeforeEach
    void prepareEntities() {
        table1 = new Table(1L, 1, 1);
        table2 = new Table(2L, 2, 2);
        tableDto1 = new TableDto(1L, 1, 1);
        tableDto2 = new TableDto(2L, 2, 2);
    }

    @Test
    void getAllTablesTest() {
        when(tableMapper.map(table1)).thenReturn(tableDto1);
        when(tableMapper.map(table2)).thenReturn(tableDto2);

        when(tableRepository.getAll()).thenReturn(List.of(table1, table2));

        assertThat(tableService.getAll()).isEqualTo(List.of(tableDto1, tableDto2));
    }

    @Test
    void getAllTablesFromEmptyTableTest() {
        when(tableRepository.getAll()).thenReturn(new ArrayList<>());

        assertThat(tableService.getAll()).isEmpty();
    }

    @Test
    void getTableByIdTest() {
        Long tableId = table1.getId();
        when(tableRepository.getById(tableId)).thenReturn(table1);
        when(tableMapper.map(table1)).thenReturn(tableDto1);

        OperationResult<TableDto> result = tableService.getById(tableId);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getResult()).isEqualTo(tableDto1);
    }

    @Test
    void getUnexistingTableByIdTest() {
        when(tableRepository.getById(anyLong())).thenReturn(null);

        OperationResult<TableDto> result = tableService.getById(1L);

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getResult()).isNull();
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(longs = {-1L})
    void getTableByInvaliIdTest(Long tableId) {
        OperationResult<TableDto> result = tableService.getById(tableId);

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getResult()).isNull();
    }
}
