package org.example.restaurant.servicelayer.services;

import org.example.restaurant.datalayer.entities.Position;
import org.example.restaurant.datalayer.repositories.PositionRepository;
import org.example.restaurant.servicelayer.exceptions.InvalidEntityException;
import org.example.restaurant.servicelayer.exceptions.InvalidIdException;
import org.example.restaurant.servicelayer.validators.PositionValidator;

import java.util.List;
import java.util.Objects;

public class PositionService {
    private final PositionRepository positionRepository;
    private final PositionValidator positionValidator;

    public PositionService(PositionRepository positionRepository, PositionValidator positionValidator) {
        this.positionRepository = positionRepository;
        this.positionValidator = positionValidator;
    }

    public List<Position> getAll() {
        return positionRepository.getAll();
    }

    public Position getById(Long id) {
        if (Objects.isNull(id) || id <= 0) {
            throw new InvalidIdException("Provided id is invalid");
        }

        return positionRepository.getById(id);
    }

    public List<Position> getByName(String positionName) {
        if (Objects.isNull(positionName) || positionName.isEmpty()) {
            return getAll();
        }

        return positionRepository.getByName(positionName);
    }

    public Position add(Position position) {
        if (Objects.isNull(position) || !positionValidator.isAddValid(position)) {
            throw new InvalidEntityException("Provided position is invalid");
        }

        return positionRepository.add(position);
    }

    public Position update(Position position) {
        if (Objects.isNull(position) || !positionValidator.isCreateValid(position)) {
            throw new InvalidEntityException("Provided position is invalid");
        }

        return positionRepository.update(position);
    }

    public boolean delete(Long id) {
        if (Objects.isNull(id) || id <= 0) {
            throw new InvalidIdException("Provided id is invalid");
        }

        return positionRepository.delete(id);
    }
}
