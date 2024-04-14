package org.example.restaurant.servicelayer.validators;

import org.example.restaurant.datalayer.dto.user.AddUserDto;
import org.example.restaurant.datalayer.dto.user.UpdateUserDto;

public class UserValidator {
    private static final String PHONE_NUMBER_REGEX = ".*";   //TODO
    private static final UserValidator INSTANCE = new UserValidator();

    public static UserValidator getInstance() {
        return INSTANCE;
    }

    private UserValidator() {
    }

    public boolean isAddValid(AddUserDto addUserDto) {
        return !addUserDto.getLogin().isEmpty() &&
                !addUserDto.getPassword().isEmpty() &&
                addUserDto.getPhoneNumber().matches(PHONE_NUMBER_REGEX);
    }

    public boolean isUpdateValid(UpdateUserDto updateUserDto) {
        return updateUserDto.getId() > 0 &&
                !updateUserDto.getLogin().isEmpty() &&
                updateUserDto.getPhoneNumber().matches(PHONE_NUMBER_REGEX);
    }
}
