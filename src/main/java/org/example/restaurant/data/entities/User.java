package org.example.restaurant.data.entities;

import java.util.Objects;

public class User extends Entity {
    private String login;
    private String password;
    private String phoneNumber;
    private String role;

    public User() {}

    public User(long id, String login, String password, String phoneNumber, String role) {
        super(id);
        this.login = login;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.role = role;
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
        if (!super.equals(o)) return false;
        User user = (User) o;
        return Objects.equals(login, user.login) &&
                Objects.equals(password, user.password) &&
                Objects.equals(phoneNumber, user.phoneNumber) &&
                Objects.equals(role, user.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), login, password, phoneNumber, role);
    }
}
