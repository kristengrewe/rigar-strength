package com.techelevator.custom.dto;

import java.time.LocalDate;
import java.util.Objects;

public class Workout {

    private int workoutId;
    private int personId;
    private int liftId;
    private String personFirstName;
    private String personLastName;
    private String liftName;
    private LocalDate date;
    private int sets;
    private int reps;
    private int maxWeight;
    private String notes;

    public Workout(int workoutId, int personId, int liftId, String personFirstName, String personLastName, String liftName, LocalDate date, int sets, int reps, int maxWeight, String notes) {
        this.workoutId = workoutId;
        this.personId = personId;
        this.liftId = liftId;
        this.personFirstName = personFirstName;
        this.personLastName = personLastName;
        this.liftName = liftName;
        this.date = date;
        this.sets = sets;
        this.reps = reps;
        this.maxWeight = maxWeight;
        this.notes = notes;
    }

    public int getWorkoutId() {
        return workoutId;
    }

    public void setWorkoutId(int workoutId) {
        this.workoutId = workoutId;
    }

    public String getPersonFirstName() {
        return personFirstName;
    }

    public void setPersonFirstName(String personFirstName) {
        this.personFirstName = personFirstName;
    }

    public String getPersonLastName() {
        return personLastName;
    }

    public void setPersonLastName(String personLastName) {
        this.personLastName = personLastName;
    }

    public String getLiftName() {
        return liftName;
    }

    public void setLiftName(String liftName) {
        this.liftName = liftName;
    }

    public int getLiftId() {
        return liftId;
    }

    public void setLiftId(int liftId) {
        this.liftId = liftId;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public int getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(int maxWeight) {
        this.maxWeight = maxWeight;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Workout workouts = (Workout) o;
        return liftId == workouts.liftId && personId == workouts.personId && sets == workouts.sets && reps == workouts.reps && maxWeight == workouts.maxWeight && Objects.equals(personFirstName, workouts.personFirstName) && Objects.equals(personLastName, workouts.personLastName) && Objects.equals(liftName, workouts.liftName) && Objects.equals(date, workouts.date) && Objects.equals(notes, workouts.notes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(personFirstName, personLastName, liftName, liftId, personId, date, sets, reps, maxWeight, notes);
    }

    @Override
    public String toString() {
        return "Workout {" +
                "Name: " + personFirstName + " " + personLastName +
                " | Lift: " + liftName +
                " | Date: " + date +
                " | Sets: " + sets +
                " | Reps: " + reps +
                " | Max Weight: " + maxWeight +
                " | Notes: " + notes +
                '}';
    }
}
