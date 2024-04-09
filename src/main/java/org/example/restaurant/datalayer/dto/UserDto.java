package org.example.restaurant.datalayer.dto;

public class UserDto {
    private String login;
    private String phoneNumber;
    private String role;

    public UserDto(String login, String phoneNumber, String role) {
        this.login = login;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }

    public UserDto() {
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
}
