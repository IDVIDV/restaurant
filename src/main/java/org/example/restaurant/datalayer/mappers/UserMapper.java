package org.example.restaurant.datalayer.mappers;

import org.example.restaurant.datalayer.dto.user.AddUserDto;
import org.example.restaurant.datalayer.dto.user.UpdateUserDto;
import org.example.restaurant.datalayer.dto.user.UserDto;
import org.example.restaurant.datalayer.entities.User;

public class UserMapper {
    private static final UserMapper INSTANCE = new UserMapper();

    public static UserMapper getInstance() {
        return INSTANCE;
    }

    private UserMapper() {
    }

    public User map(AddUserDto addUserDto) {
        User user = new User();
        user.setLogin(addUserDto.getLogin());
        user.setPassword(addUserDto.getPassword());
        user.setPhoneNumber(addUserDto.getPhoneNumber());
        return user;
    }

    public User map(UpdateUserDto updateUserDto) {
        User user = new User();
        user.setId(updateUserDto.getId());
        user.setLogin(updateUserDto.getLogin());
        user.setPhoneNumber(updateUserDto.getPhoneNumber());
        return user;
    }

    public UserDto map(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setLogin(user.getLogin());
        userDto.setPhoneNumber(user.getPhoneNumber());
        userDto.setRole(user.getRole());
        return userDto;
    }
}
