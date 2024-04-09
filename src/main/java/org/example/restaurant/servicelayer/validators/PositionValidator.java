package org.example.restaurant.servicelayer.validators;

import org.example.restaurant.datalayer.entities.Position;

import java.math.BigDecimal;

public class PositionValidator {
    private static final PositionValidator INSTANCE = new PositionValidator();

    public static PositionValidator getInstance() {
        return INSTANCE;
    }

    private PositionValidator() {
    }

    private boolean isValid(Position position) {
        return !position.getPositionName().isEmpty() &&
                position.getPrice().compareTo(BigDecimal.ZERO) > 0 &&
                position.getWeight() > 0 &&
                position.getProtein() > 0 &&
                position.getFat() > 0 &&
                position.getCarbohydrate() > 0;

    }

    public boolean isAddValid(Position position) {
        return isValid(position);
    }

    public boolean isCreateValid(Position position) {
        return position.getId() > 0 &&
                isValid(position);
    }
}
