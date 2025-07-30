package com.techelevator.custom.dao;

import com.techelevator.custom.model.Person;

import java.util.List;

public interface PersonDao {
    List<Person> getAllPersons();
    Person createPerson(Person person);
}
