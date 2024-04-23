package org.example.restaurant.servicelayer.validators;

import org.example.restaurant.datalayer.dto.position.AddPositionDto;
import org.example.restaurant.datalayer.dto.position.UpdatePositionDto;
import org.example.restaurant.datalayer.entities.Position;

import java.math.BigDecimal;

public class PositionValidator {

    public boolean isAddValid(AddPositionDto addPositionDto) {
        return !addPositionDto.getPositionName().isEmpty() &&
                addPositionDto.getPrice().compareTo(BigDecimal.ZERO) > 0 &&
                addPositionDto.getWeight() > 0 &&
                addPositionDto.getProtein() > 0 &&
                addPositionDto.getFat() > 0 &&
                addPositionDto.getCarbohydrate() > 0;
    }

    public boolean isUpdateValid(UpdatePositionDto updatePositionDto) {
        return updatePositionDto.getId() > 0 &&
                !updatePositionDto.getPositionName().isEmpty() &&
                updatePositionDto.getPrice().compareTo(BigDecimal.ZERO) > 0 &&
                updatePositionDto.getWeight() > 0 &&
                updatePositionDto.getProtein() > 0 &&
                updatePositionDto.getFat() > 0 &&
                updatePositionDto.getCarbohydrate() > 0;
    }
}
