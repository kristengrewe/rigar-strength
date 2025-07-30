package com.techelevator.custom.controller;

import com.techelevator.custom.dao.PersonDao;
import com.techelevator.custom.dao.UserDao;
import com.techelevator.custom.exception.DaoException;
import com.techelevator.custom.model.Person;
import com.techelevator.custom.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin
@PreAuthorize("isAuthenticated()")
@RequestMapping(path="/api/members")
public class MemberController {

    private final PersonDao personDao;
    private final UserDao userDao;


    public MemberController(PersonDao personDao, UserDao userDao) {
        this.personDao = personDao;
        this.userDao = userDao;
    }
    @GetMapping
    public List<Person> getAllMembers(Principal principal){
        try {
            User user = userDao.getUserByUsername(principal.getName());

            if(user.getRole().equals("ROLE_ADMIN")){
                return personDao.getAllPersons();
            }
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only admins have access to the members list");
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Can't get members list: " + e.getMessage());
        }
    }
}
