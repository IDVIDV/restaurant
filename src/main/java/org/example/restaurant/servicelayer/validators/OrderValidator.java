package org.example.restaurant.servicelayer.validators;

import org.example.restaurant.datalayer.dto.order.CloseUnfinishedOrderDto;

import java.time.LocalDate;
import java.util.Objects;

public class OrderValidator {

    public boolean isCloseUnfinishedValid(CloseUnfinishedOrderDto closeUnfinishedOrderDto) {
        return closeUnfinishedOrderDto.getId() > 0 &&
                closeUnfinishedOrderDto.getUserId() > 0 &&
                closeUnfinishedOrderDto.getOrderDate().toLocalDate().compareTo(LocalDate.now()) > 0 &&
                Objects.nonNull(closeUnfinishedOrderDto.getTableDto());
    }
}
