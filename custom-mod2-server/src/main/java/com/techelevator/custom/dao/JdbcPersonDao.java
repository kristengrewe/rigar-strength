package com.techelevator.custom.dao;

import com.techelevator.custom.exception.DaoException;
import com.techelevator.custom.model.Person;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Repository
public class JdbcPersonDao implements PersonDao {

    private static final String PERSON_SELECT = "SELECT person_id, first_name, last_name, birthdate" +
            ", email FROM person ";

    private final JdbcTemplate jdbcTemplate;

    public JdbcPersonDao(DataSource datasource){
        this.jdbcTemplate = new JdbcTemplate(datasource);
    }

    private final static RowMapper<Person> PERSON_ROW_MAPPER = new RowMapper<Person>() {
        @Override
        public Person mapRow(ResultSet results, int rowNum) throws SQLException {
            Person person = new Person(results.getInt("person_id"), results.getString("first_name"), results.getString("last_name"), results.getObject("birthdate", LocalDate.class), results.getString("email"));
            return person;
        }
    };

    @Override
    public List<Person> getAllPersons() {
        String sql = PERSON_SELECT + "ORDER BY person_id";

        try {
            return jdbcTemplate.query(sql, PERSON_ROW_MAPPER);
        } catch (DaoException e) {
            throw new DaoException("Error fetching all persons: " + e);
        }
    }
    @Override
    public Person createPerson(Person person) {
        try {
            String sql = "INSERT INTO person (person_id, first_name, last_name, birthdate, email) VALUES (?, ?, ?, ?, ?)";
            jdbcTemplate.update(sql,
                    person.getPersonId(),
                    person.getFirstName(),
                    person.getLastName(),
                    person.getBirthdate(),
                    person.getEmail()

            );
            return person;

        } catch (Exception e) {
            throw new DaoException("Error creating person: " + e.getMessage(), e);
        }
    }
}
