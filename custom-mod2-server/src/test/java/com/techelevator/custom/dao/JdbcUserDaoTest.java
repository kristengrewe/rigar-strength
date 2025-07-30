package com.techelevator.custom.dao;

import com.techelevator.custom.exception.DaoException;
import com.techelevator.custom.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JdbcUserDaoTest extends BaseDaoTest {
    private JdbcUserDao dao;
    private static final User USER_KRISTEN = new User(1, "kristen", "$2a$10$tmxuYYg1f5T0eXsTPlq/V.DJUKmRHyFbJ.o.liI1T35TFbjs2xiem", "ROLE_ADMIN");
    private static final User USER_JESS = new User(2, "jess", "$2a$10$tmxuYYg1f5T0eXsTPlq/V.DJUKmRHyFbJ.o.liI1T35TFbjs2xiem", "ROLE_USER");
    private static final User USER_MIKEY = new User(3, "mikey", "$2a$10$tmxuYYg1f5T0eXsTPlq/V.DJUKmRHyFbJ.o.liI1T35TFbjs2xiem", "ROLE_USER");

    @BeforeEach
    public void setup() {
        dao = new JdbcUserDao(new JdbcTemplate(dataSource));
    }

    @Test
    public void getUserByUsername_with_invalid_returns_null() {
        User user = dao.getUserByUsername("notarealuser");
        assertNull(user);
    }

    @Test
    public void getUserByUsername_with_valid_username_returns_user() {
        User user = dao.getUserByUsername("kristen");
        assertNotNull(user);
        assertEquals(USER_KRISTEN.getUsername(), user.getUsername());
        assertEquals(USER_KRISTEN.getRole(), user.getRole());

        User userTwo = dao.getUserByUsername("mikey");
        assertNotNull(userTwo);
        assertEquals(USER_MIKEY.getUsername(), userTwo.getUsername());
        assertEquals(USER_MIKEY.getRole(), userTwo.getRole());
    }

    @Test
    public void getUserById_with_invalid_id_returns_null() {
        User user = dao.getUserById(-99);
        assertNull(user);
    }

    @Test
    public void getUserById_with_valid_id_returns_user() {
        User user = dao.getUserById(2);
        assertNotNull(user);
        assertEquals(USER_JESS.getUsername(), user.getUsername());
    }

    @Test
    public void getUsers_returns_all_users() {
        List<User> users = dao.getUsers();

        assertNotNull(users);
        assertEquals(4, users.size());

        assertEquals("colin", users.get(0).getUsername());
        assertEquals("jess", users.get(1).getUsername());
        assertEquals("kristen", users.get(2).getUsername());
        assertEquals("mikey", users.get(3).getUsername());
    }

    @Test
    public void createUser_with_null_username_throws_exception() {
        User badUser = new User(null, "password", "ROLE_USER");
        assertThrows(DaoException.class, () -> dao.createUser(badUser));
    }

    @Test
    public void createUser_with_existing_username_throws_exception() {
        User duplicateUser = new User("jess", "password", "ROLE_USER");
        assertThrows(DaoException.class, () -> dao.createUser(duplicateUser));
    }

    @Test
    public void createUser_with_null_password_throws_exception() {
        User badUser = new User("uniqueUser", null, "ROLE_USER");
        assertThrows(DaoException.class, () -> dao.createUser(badUser));
    }

    @Test
    public void createUser_creates_and_returns_user() {
        String rawPassword = "newpass123";
        User userToCreate = new User("new_user", rawPassword, "ROLE_USER");

        User createdUser = dao.createUser(userToCreate);

        assertNotNull(createdUser);
        assertNotEquals(0, createdUser.getId());
        assertEquals("new_user", createdUser.getUsername());
        assertEquals("ROLE_USER", createdUser.getRole());

        assertNotEquals(rawPassword, createdUser.getHashedPassword());
        assertTrue(createdUser.getHashedPassword().startsWith("$2a$"));
    }
}
