package com.techelevator.custom.dao;

import com.techelevator.custom.dto.Workout;

import java.time.LocalDate;
import java.util.List;

public interface WorkoutDao {

    List<Workout> getAllWorkouts();
    List<Workout> getWorkoutsByPersonId(int id);


    List<Workout> getWorkoutsByDate(LocalDate date);
    List<Workout> getWorkoutsByDateAndPersonId(int id, LocalDate date);


    List<Workout> getWorkoutsByDateRange(LocalDate startDate, LocalDate endDate);
    List<Workout> getWorkoutsByDateRangeAndPersonId(int id, LocalDate startDate, LocalDate endDate);


    List<Workout> getWorkoutsByLiftName(String liftName);
    List<Workout> getWorkoutsByLiftNameAndPersonId(int id, String liftName);


    List<Workout> getWorkoutsByPersonLastName(String lastName);


    Workout getWorkoutById(int workoutId);

    Workout createWorkout(Workout workout);
    Workout updateWorkout(Workout workout, int workoutId);
    boolean deleteWorkout(int workoutId);
}
