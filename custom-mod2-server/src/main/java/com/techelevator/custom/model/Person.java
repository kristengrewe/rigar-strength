package com.techelevator.custom.model;

import java.time.LocalDate;
import java.util.Objects;

public class Person {

    private int personId;
    private String firstName;
    private String lastName;
    private LocalDate birthdate;
    private String email;

    public Person(int personId, String firstName, String lastName, LocalDate birthdate, String email) {
        this.personId = personId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthdate = birthdate;
        this.email = email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPersonId() {
        return personId;
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

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return personId == person.personId && Objects.equals(firstName, person.firstName) && Objects.equals(lastName, person.lastName) && Objects.equals(birthdate, person.birthdate) && Objects.equals(email, person.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(personId, firstName, lastName, birthdate, email);
    }

    @Override
    public String toString() {
        return " {" + firstName + " " + lastName +
                " | Id: " + personId +
                " | Birthdate: " + birthdate +
                " | Email: " + email +
                '}';
    }
}
