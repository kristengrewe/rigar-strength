package com.techelevator.custom.controller;

import com.techelevator.custom.dao.LiftDao;
import com.techelevator.custom.exception.DaoException;
import com.techelevator.custom.model.Lift;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(path="api/lifts")
public class LiftController {

    private final LiftDao liftDao;

    public LiftController(LiftDao liftDao) {
        this.liftDao = liftDao;
    }

    @GetMapping
    public List<Lift> getAllLifts(){
        try {
            return liftDao.getAllLifts();
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not get lifts" + e.getMessage());
        }
    }

}
