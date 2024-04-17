package org.example.restaurant.servicelayer.services;

import org.example.restaurant.datalayer.dto.position.AddPositionDto;
import org.example.restaurant.datalayer.dto.position.PositionDto;
import org.example.restaurant.datalayer.dto.position.UpdatePositionDto;
import org.example.restaurant.datalayer.entities.Position;
import org.example.restaurant.datalayer.mappers.PositionMapper;
import org.example.restaurant.datalayer.repositories.PositionRepository;
import org.example.restaurant.servicelayer.OperationResult;
import org.example.restaurant.servicelayer.validators.PositionValidator;

import java.util.List;
import java.util.Objects;

public class PositionService {
    private final PositionValidator positionValidator;
    private final PositionMapper positionMapper;
    private final PositionRepository positionRepository;

    public PositionService(PositionValidator positionValidator, PositionMapper positionMapper,
                           PositionRepository positionRepository) {
        this.positionValidator = positionValidator;
        this.positionMapper = positionMapper;
        this.positionRepository = positionRepository;
    }

    public List<PositionDto> getAll() {
        return positionRepository.getAll().stream().map(positionMapper::map).toList();
    }

    public OperationResult<PositionDto> getById(Long id) {
        if (Objects.isNull(id) || id <= 0) {
            return new OperationResult<>("Invalid position id");
        }

        Position position = positionRepository.getById(id);

        if (Objects.isNull(position)) {
            return new OperationResult<>("No position with given id");
        }

        return new OperationResult<>(positionMapper.map(position));
    }

    public List<PositionDto> getByName(String positionName) {
        if (Objects.isNull(positionName) || positionName.isEmpty()) {
            return getAll();
        }

        return positionRepository.getByName(positionName).stream().map(positionMapper::map).toList();
    }

    public OperationResult<PositionDto> add(AddPositionDto addPositionDto) {
        if (Objects.isNull(addPositionDto) || !positionValidator.isAddValid(addPositionDto)) {
            return new OperationResult<>("Invalid input data");
        }

        Position position = positionRepository.add(positionMapper.map(addPositionDto));

        if (Objects.isNull(position)) {
            return new OperationResult<>("Couldn't add position");
        }

        return new OperationResult<>(positionMapper.map(position));
    }

    public OperationResult<PositionDto> update(UpdatePositionDto updatePositionDto) {
        if (Objects.isNull(updatePositionDto) || !positionValidator.isUpdateValid(updatePositionDto)) {
            return new OperationResult<>("Invalid input data");
        }

        Position position = positionRepository.update(positionMapper.map(updatePositionDto));

        if (Objects.isNull(position)) {
            return new OperationResult<>("Couldn't update position");
        }

        return new OperationResult<>(positionMapper.map(position));
    }

    public OperationResult<Boolean> delete(Long id) {
        if (Objects.isNull(id) || id <= 0) {
            return new OperationResult<>("Invalid position id");
        }

        Boolean result = positionRepository.delete(id);

        if (!result) {
            return new OperationResult<>("Couldn't delete position");
        }

        return new OperationResult<>(result);
    }
}
