package org.example.restaurant.servicelayer.services;

import org.example.restaurant.datalayer.entities.User;
import org.example.restaurant.datalayer.repositories.UserRepository;

import java.util.Objects;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean login(String login, String password) {
        User user = userRepository.getByLogin(login);

        if (Objects.isNull(user)) {
            return false;
        }

        return user.getPassword().equals(getPasswordHash(password));
    }

    private String getPasswordHash(String password) {
        return password;
    }

    //TODO
    public User getByLogin(String login) {
        return userRepository.getByLogin(login);
    }
}
