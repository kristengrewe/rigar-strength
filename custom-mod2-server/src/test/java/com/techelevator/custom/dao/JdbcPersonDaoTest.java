package com.techelevator.custom.dao;

import com.techelevator.custom.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JdbcPersonDaoTest extends BaseDaoTest{
    private JdbcPersonDao dao;

    private static final Person KRISTEN = new Person(1, "Kristen", "Grewe", LocalDate.of(1990, 11, 1), "kgrewe@gmail.com");
    private static final Person JESS = new Person(2, "Jess", "Christin", LocalDate.of(1984, 2, 5), "jess@jcwellness.com");
    private static final Person MIKEY = new Person(3, "Mikey", "Garrity", LocalDate.of(1985, 6, 10), "mikey@rigarperformance.com");
    private static final Person COLIN = new Person(4, "Colin", "Van Ert", LocalDate.of(1990, 6, 1), "colinv@swole.com");

    @BeforeEach
    public void setup() {
        dao = new JdbcPersonDao(dataSource);
    }

    @Test
    public void getAllPersons_returns_all_people_correctly() {
        List<Person> persons = dao.getAllPersons();

        assertNotNull(persons, "Expected non-null list of persons");
        assertEquals(4, persons.size(), "Expected 4 persons from seed data");

        assertEquals(KRISTEN.getPersonId(), persons.get(0).getPersonId());
        assertEquals(JESS.getPersonId(), persons.get(1).getPersonId());
        assertEquals(MIKEY.getPersonId(), persons.get(2).getPersonId());
        assertEquals(COLIN.getPersonId(), persons.get(3).getPersonId());
    }
}
