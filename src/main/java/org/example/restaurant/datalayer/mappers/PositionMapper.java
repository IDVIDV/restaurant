package org.example.restaurant.datalayer.mappers;

import org.example.restaurant.datalayer.dto.position.AddPositionDto;
import org.example.restaurant.datalayer.dto.position.PositionDto;
import org.example.restaurant.datalayer.dto.position.UpdatePositionDto;
import org.example.restaurant.datalayer.entities.Position;

import java.util.Objects;

public class PositionMapper {

    public Position map(AddPositionDto addPositionDto) {
        if (Objects.isNull(addPositionDto)) {
            return null;
        }

        Position position = new Position();
        position.setPositionName(addPositionDto.getPositionName());
        position.setPrice(addPositionDto.getPrice());
        position.setWeight(addPositionDto.getWeight());
        position.setProtein(addPositionDto.getProtein());
        position.setFat(addPositionDto.getFat());
        position.setCarbohydrate(addPositionDto.getCarbohydrate());
        position.setVegan(addPositionDto.getVegan());
        position.setIngredients(addPositionDto.getIngredients());
        return position;
    }

    public Position map(UpdatePositionDto updatePositionDto) {
        if (Objects.isNull(updatePositionDto)) {
            return null;
        }

        Position position = new Position();
        position.setId(updatePositionDto.getId());
        position.setPositionName(updatePositionDto.getPositionName());
        position.setPrice(updatePositionDto.getPrice());
        position.setWeight(updatePositionDto.getWeight());
        position.setProtein(updatePositionDto.getProtein());
        position.setFat(updatePositionDto.getFat());
        position.setCarbohydrate(updatePositionDto.getCarbohydrate());
        position.setVegan(updatePositionDto.getVegan());
        position.setIngredients(updatePositionDto.getIngredients());
        return position;
    }

    public PositionDto map(Position position) {
        if (Objects.isNull(position)) {
            return null;
        }

        PositionDto positionDto = new PositionDto();
        positionDto.setId(position.getId());
        positionDto.setPositionName(position.getPositionName());
        positionDto.setPrice(position.getPrice());
        positionDto.setWeight(position.getWeight());
        positionDto.setProtein(position.getProtein());
        positionDto.setFat(position.getFat());
        positionDto.setCarbohydrate(position.getCarbohydrate());
        positionDto.setVegan(position.isVegan());
        positionDto.setIngredients(position.getIngredients());
        return positionDto;
    }
}
