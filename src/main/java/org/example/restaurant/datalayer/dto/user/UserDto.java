package org.example.restaurant.datalayer.dto.user;

import java.util.Objects;

public class UserDto {
    private Long id;
    private String login;
    private String phoneNumber;
    private String role;

    public UserDto() {
    }

    public UserDto(Long id, String login, String phoneNumber, String role) {
        this.id = id;
        this.login = login;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto userDto = (UserDto) o;
        return Objects.equals(id, userDto.id) &&
                Objects.equals(login, userDto.login) &&
                Objects.equals(phoneNumber, userDto.phoneNumber) &&
                Objects.equals(role, userDto.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, phoneNumber, role);
    }
}
