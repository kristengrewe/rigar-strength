package com.techelevator.custom.controller;

import com.techelevator.custom.dao.UserDao;
import com.techelevator.custom.dao.WorkoutDao;
import com.techelevator.custom.dto.Workout;
import com.techelevator.custom.exception.DaoException;
import com.techelevator.custom.model.LoginDto;
import com.techelevator.custom.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@RestController
@CrossOrigin
@PreAuthorize("isAuthenticated()")
@RequestMapping(path="/api/workouts")
public class WorkoutController {
    private final WorkoutDao workoutsDao;
    private final UserDao userDao;


    public WorkoutController(WorkoutDao workoutsDao, UserDao userDao) {
        this.workoutsDao = workoutsDao;
        this.userDao = userDao;
    }
    @GetMapping
    public List<Workout> getAllWorkouts(
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) LocalDate date,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) String liftName,
            Principal principal) {

        try {
            User user = userDao.getUserByUsername(principal.getName());

            if (user.getRole().equals("ROLE_ADMIN")) {
                if (lastName != null) {
                    return workoutsDao.getWorkoutsByPersonLastName(lastName);
                } else if (date != null) {
                    return workoutsDao.getWorkoutsByDate(date);
                } else if (startDate != null && endDate != null) {
                    return workoutsDao.getWorkoutsByDateRange(startDate, endDate);
                } else if (liftName != null) {
                    return workoutsDao.getWorkoutsByLiftName(liftName);
                } else {
                    return workoutsDao.getAllWorkouts();
                }
            } else {
                int userId = user.getId();

                if (lastName != null){
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can not access workouts for other members");
                } else if (date != null) {
                    return workoutsDao.getWorkoutsByDateAndPersonId(userId, date);
                } else if (startDate != null && endDate != null) {
                    return workoutsDao.getWorkoutsByDateRangeAndPersonId(userId, startDate, endDate);
                } else if (liftName != null) {
                    return workoutsDao.getWorkoutsByLiftNameAndPersonId(userId, liftName);
                } else {
                    return workoutsDao.getWorkoutsByPersonId(userId);
                }
            }
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Can't get workouts: " + e.getMessage());
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Workout createWorkout(@RequestBody Workout workout, Principal principal){

        User user = userDao.getUserByUsername(principal.getName());
        if(user == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if (workout.getPersonId() != user.getId()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Access denied");
        }

        Workout newWorkout = workoutsDao.createWorkout(workout);

        return newWorkout;
    }
    @PutMapping("/{id}")
    public Workout updateWorkout(@PathVariable("id") int workoutId, @RequestBody Workout workout, Principal principal) {

        User user = userDao.getUserByUsername(principal.getName());

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
        }
        if (workout.getPersonId() != user.getId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        workout.setWorkoutId(workoutId);
        Workout updatedWorkout = workoutsDao.updateWorkout(workout, workoutId);

        if (updatedWorkout == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Workout not found or update failed");
        }

        return updatedWorkout;
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteWorkout(@PathVariable("id") int workoutId, Principal principal) {

        User user = userDao.getUserByUsername(principal.getName());

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        Workout workout = workoutsDao.getWorkoutById(workoutId);

        if (workout == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Workout not found");
        }
        if (workout.getPersonId() != user.getId()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Access denied");
        }
        boolean isDeleted = workoutsDao.deleteWorkout(workoutId);
    }

}
