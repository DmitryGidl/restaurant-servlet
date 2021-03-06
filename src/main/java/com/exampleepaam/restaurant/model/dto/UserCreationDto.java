package com.exampleepaam.restaurant.model.dto;

import java.util.Objects;

/**
 * Creation DTO class for User
 */
public class UserCreationDto {
    private String name;
    private String email;
    private String password;
    private String matchingPassword;


    public UserCreationDto(String name, String email, String password, String matchingPassword) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.matchingPassword = matchingPassword;
    }

    public UserCreationDto() {
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "username='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", matchingPassword='" + matchingPassword + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMatchingPassword() {
        return matchingPassword;
    }

    public void setMatchingPassword(String matchingPassword) {
        this.matchingPassword = matchingPassword;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserCreationDto that = (UserCreationDto) o;
        return Objects.equals(name, that.name) && Objects.equals(email, that.email) &&
                Objects.equals(password, that.password) && Objects.equals(matchingPassword, that.matchingPassword);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email, password, matchingPassword);
    }
}

