package servicelayer.services;

import org.example.restaurant.datalayer.dto.position.AddPositionDto;
import org.example.restaurant.datalayer.dto.position.PositionDto;
import org.example.restaurant.datalayer.dto.position.UpdatePositionDto;
import org.example.restaurant.datalayer.entities.Position;
import org.example.restaurant.datalayer.mappers.PositionMapper;
import org.example.restaurant.datalayer.repositories.PositionRepository;
import org.example.restaurant.servicelayer.OperationResult;
import org.example.restaurant.servicelayer.services.PositionService;
import org.example.restaurant.servicelayer.validators.PositionValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PositionServiceTest {
    @Mock
    PositionValidator positionValidator;
    @Mock
    PositionMapper positionMapper;
    @Mock
    PositionRepository positionRepository;
    @InjectMocks
    PositionService positionService;
    Position position1;
    Position position2;
    PositionDto positionDto1;
    PositionDto positionDto2;
    AddPositionDto addPositionDto;
    UpdatePositionDto updatePositionDto;

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
        addPositionDto = new AddPositionDto("positionName", BigDecimal.valueOf(1),
                1D, 1D, 1D, 1D,
                false, "Ingr");
        updatePositionDto = new UpdatePositionDto(1L, "positionName",
                BigDecimal.valueOf(1),
                1D, 1D, 1D, 1D,
                false, "Ingr");
    }

    @Test
    void getAllPositionsTest() {
        when(positionRepository.getAll()).thenReturn(List.of(position1, position2));
        when(positionMapper.map(position1)).thenReturn(positionDto1);
        when(positionMapper.map(position2)).thenReturn(positionDto2);

        assertThat(positionService.getAll()).isEqualTo(List.of(positionDto1, positionDto2));
    }

    @Test
    void getAllPositionsFromEmptyTable() {
        when(positionRepository.getAll()).thenReturn(new ArrayList<>());

        assertThat(positionService.getAll()).isEmpty();
    }

    @Test
    void getPositionByIdTest() {
        when(positionRepository.getById(position1.getId())).thenReturn(position1);
        when(positionMapper.map(position1)).thenReturn(positionDto1);

        OperationResult<PositionDto> result = positionService.getById(position1.getId());

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getResult()).isEqualTo(positionDto1);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(longs = {-1L})
    void getPositionByInvalidIdTest(Long positionId) {
        OperationResult<PositionDto> result = positionService.getById(positionId);

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getResult()).isNull();
    }

    @Test
    void getUnexistingPositionByIdTest() {
        when(positionRepository.getById(position1.getId())).thenReturn(null);

        OperationResult<PositionDto> result = positionService.getById(position1.getId());

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getResult()).isNull();
    }

    @Test
    void getPositionsByNameTest() {
        String positionName = position1.getPositionName();
        when(positionRepository.getByName(positionName)).thenReturn(List.of(position1));
        when(positionMapper.map(position1)).thenReturn(positionDto1);

        assertThat(positionService.getByName(positionName)).isEqualTo(List.of(positionDto1));
    }

    @Test
    void getPositionsByNameFromEmptyTableTest() {
        String positionName = position1.getPositionName();
        when(positionRepository.getByName(positionName)).thenReturn(new ArrayList<>());

        assertThat(positionService.getByName(positionName)).isEmpty();
    }

    @ParameterizedTest
    @NullAndEmptySource
    void getPositionsByEmptyNameTest(String positionName) {
        when(positionRepository.getAll()).thenReturn(List.of(position1, position2));
        when(positionMapper.map(position1)).thenReturn(positionDto1);
        when(positionMapper.map(position2)).thenReturn(positionDto2);

        assertThat(positionService.getByName(positionName)).isEqualTo(List.of(positionDto1, positionDto2));
    }

    @Test
    void addPositionTest() {
        when(positionValidator.isAddValid(addPositionDto)).thenReturn(true);
        when(positionMapper.map(addPositionDto)).thenReturn(position1);
        when(positionRepository.add(position1)).thenReturn(position1);
        when(positionMapper.map(position1)).thenReturn(positionDto1);

        OperationResult<PositionDto> result = positionService.add(addPositionDto);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getResult()).isEqualTo(positionDto1);
    }

    @Test
    void addPositionWithIncorrectDataTest() {
        when(positionValidator.isAddValid(addPositionDto)).thenReturn(false);

        OperationResult<PositionDto> result = positionService.add(addPositionDto);

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getResult()).isNull();
    }

    @Test
    void addPositionFailTest() {
        when(positionValidator.isAddValid(addPositionDto)).thenReturn(true);
        when(positionMapper.map(addPositionDto)).thenReturn(position1);
        when(positionRepository.add(position1)).thenReturn(null);

        OperationResult<PositionDto> result = positionService.add(addPositionDto);

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getResult()).isNull();
    }

    @Test
    void updatePositionTest() {
        when(positionValidator.isUpdateValid(updatePositionDto)).thenReturn(true);
        when(positionMapper.map(updatePositionDto)).thenReturn(position1);
        when(positionRepository.update(position1)).thenReturn(position1);
        when(positionMapper.map(position1)).thenReturn(positionDto1);

        OperationResult<PositionDto> result = positionService.update(updatePositionDto);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getResult()).isEqualTo(positionDto1);
    }

    @Test
    void updatePositionWithIncorrectDataTest() {
        when(positionValidator.isUpdateValid(updatePositionDto)).thenReturn(false);

        OperationResult<PositionDto> result = positionService.update(updatePositionDto);

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getResult()).isNull();
    }

    @Test
    void updatePositionFailTest() {
        when(positionValidator.isUpdateValid(updatePositionDto)).thenReturn(true);
        when(positionMapper.map(updatePositionDto)).thenReturn(position1);
        when(positionRepository.update(position1)).thenReturn(null);

        OperationResult<PositionDto> result = positionService.update(updatePositionDto);

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getResult()).isNull();
    }

    @Test
    void deletePositionTest() {
        when(positionRepository.delete(position1.getId())).thenReturn(true);

        OperationResult<Boolean> result = positionService.delete(position1.getId());

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getResult()).isTrue();
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(longs = {-1L})
    void deletePositionByInvalidIdTest(Long positionId) {
        OperationResult<Boolean> result = positionService.delete(positionId);

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getResult()).isNull();
    }

    @Test
    void deletePositionFailTest() {
        when(positionRepository.delete(position1.getId())).thenReturn(false);

        OperationResult<Boolean> result = positionService.delete(position1.getId());

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getResult()).isNull();
    }
}
