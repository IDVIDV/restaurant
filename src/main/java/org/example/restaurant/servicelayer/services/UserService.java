package org.example.restaurant.servicelayer.services;

import org.example.restaurant.datalayer.dto.user.AddUserDto;
import org.example.restaurant.datalayer.dto.user.UpdateUserDto;
import org.example.restaurant.datalayer.dto.user.UserDto;
import org.example.restaurant.datalayer.entities.User;
import org.example.restaurant.datalayer.mappers.UserMapper;
import org.example.restaurant.datalayer.repositories.UserRepository;
import org.example.restaurant.servicelayer.OperationResult;
import org.example.restaurant.servicelayer.PasswordHasher;
import org.example.restaurant.servicelayer.PasswordHasherImpl;
import org.example.restaurant.servicelayer.validators.UserValidator;

import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public class UserService {
    private final PasswordHasher passwordHasher;
    private final UserValidator userValidator;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final Properties properties;

    public UserService(PasswordHasherImpl passwordHasher, UserValidator userValidator,
                       UserMapper userMapper, UserRepository userRepository) {
        this.passwordHasher = passwordHasher;
        this.userValidator = userValidator;
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.properties = new Properties();
        try {
            this.properties.load(Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream("app.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public OperationResult<UserDto> login(String login, String password) {
        User loggingUser = userRepository.getByLogin(login);

        if (Objects.isNull(loggingUser) || !checkPassword(password, loggingUser.getPassword())) {
            return new OperationResult<>("Invalid login or password");
        }

        return new OperationResult<>(userMapper.map(loggingUser));
    }

    public OperationResult<UserDto> register(AddUserDto addUserDto) {
        if (Objects.isNull(addUserDto) || !userValidator.isAddValid(addUserDto)) {
            return new OperationResult<>("Invalid input data");
        }

        User existingUser = userRepository.getByLogin(addUserDto.getLogin());

        if (!Objects.isNull(existingUser)) {
            return new OperationResult<>("Login is hold by another user");
        }

        User registeredUser = userMapper.map(addUserDto);
        registeredUser.setRole("user");
        registeredUser.setPassword(getPasswordHash(registeredUser.getPassword()));

        registeredUser = userRepository.add(registeredUser);

        return new OperationResult<>(userMapper.map(registeredUser));
    }

    public OperationResult<UserDto> getById(Long id) {
        if (Objects.isNull(id) || id < 1) {
            return new OperationResult<>("Invalid id");
        }

        User user = userRepository.getById(id);

        if (Objects.isNull(user)) {
            return new OperationResult<>("No user with given id");
        }

        return new OperationResult<>(userMapper.map(user));
    }

    public OperationResult<UserDto> update(UpdateUserDto updateUserDto) {
        if (Objects.isNull(updateUserDto) || !userValidator.isUpdateValid(updateUserDto)) {
            return new OperationResult<>("Invalid input data");
        }

        User userToUpdate = userRepository.getById(updateUserDto.getId());

        if (Objects.isNull(userToUpdate)) {
            return new OperationResult<>("User not found");
        }

        String password = userToUpdate.getPassword();

        userToUpdate = userMapper.map(updateUserDto);
        userToUpdate.setPassword(password);

        User updatedUser = userRepository.update(userToUpdate);

        return new OperationResult<>(userMapper.map(updatedUser));
    }

    private String getPasswordHash(String password) {
        return passwordHasher.hash(password, properties.getProperty("salt"));
    }

    private Boolean checkPassword(String candidate, String hashed) {
        return passwordHasher.check(candidate, properties.getProperty("salt"), hashed);
    }
}
