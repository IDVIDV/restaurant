package org.example.restaurant.datalayer.dto.user;

import java.util.Objects;

public class AddUserDto {
    private String login;
    private String password;
    private String phoneNumber;

    public AddUserDto() {
    }

    public AddUserDto(String login, String password, String phoneNumber) {
        this.login = login;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddUserDto that = (AddUserDto) o;
        return Objects.equals(login, that.login) &&
                Objects.equals(password, that.password) &&
                Objects.equals(phoneNumber, that.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, password, phoneNumber);
    }
}
