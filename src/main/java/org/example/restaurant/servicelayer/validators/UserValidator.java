package org.example.restaurant.servicelayer.validators;

import org.example.restaurant.datalayer.dto.user.AddUserDto;
import org.example.restaurant.datalayer.dto.user.UpdateUserDto;

import java.util.Objects;

public class UserValidator {
    private static final String PHONE_NUMBER_REGEX =
            "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$";
    private static final UserValidator INSTANCE = new UserValidator();

    public static UserValidator getInstance() {
        return INSTANCE;
    }

    private UserValidator() {
    }

    public boolean isAddValid(AddUserDto addUserDto) {
        return !addUserDto.getLogin().isEmpty() &&
                !addUserDto.getPassword().isEmpty() &&
                (addUserDto.getPhoneNumber().isEmpty() || Objects.isNull(addUserDto.getPhoneNumber()) ||
                        addUserDto.getPhoneNumber().matches(PHONE_NUMBER_REGEX));
    }

    public boolean isUpdateValid(UpdateUserDto updateUserDto) {
        return updateUserDto.getId() > 0 &&
                !updateUserDto.getLogin().isEmpty() &&
                (updateUserDto.getPhoneNumber().isEmpty() || Objects.isNull(updateUserDto.getPhoneNumber()) ||
                        updateUserDto.getPhoneNumber().matches(PHONE_NUMBER_REGEX));
    }
}
