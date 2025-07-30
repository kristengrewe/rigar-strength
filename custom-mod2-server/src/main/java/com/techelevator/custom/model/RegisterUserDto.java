package com.techelevator.custom.model;

import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDate;
public class RegisterUserDto {

    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
    @NotEmpty
    private String confirmPassword;
    @NotEmpty(message = "Please select a role for this user.")
    private String role;
    private String firstName;
    private String lastName;
    private LocalDate birthdate;
    private String email;

    public RegisterUserDto(String username, String password, String confirmPassword, String role, String firstName, String lastName, LocalDate birthdate, String email) {
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthdate = birthdate;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public String getRole() {
        return role;
    }

    public boolean isPasswordsMatch(){
        return password.equals(confirmPassword);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public String getEmail() {
        return email;
    }
}
