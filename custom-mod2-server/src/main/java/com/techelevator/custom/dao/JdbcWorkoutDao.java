package com.techelevator.custom.dao;

import com.techelevator.custom.dto.Workout;
import com.techelevator.custom.exception.DaoException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcWorkoutDao implements WorkoutDao {

    private static final String WORKOUT_SELECT = "SELECT * from person_lift as pl " +
            "JOIN person as p ON pl.person_id = p.person_id " +
            "JOIN lift as l ON pl.lift_id = l.lift_id";
    private final JdbcTemplate template;

    public JdbcWorkoutDao(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Workout> getAllWorkouts() {

        List<Workout> workouts = new ArrayList<>();

        SqlRowSet rowset = template.queryForRowSet(WORKOUT_SELECT);

        while (rowset.next()) {
            Workout workout = mapRowToWorkouts(rowset);
            workouts.add(workout);

        }

        return workouts;
    }

    @Override
    public List<Workout> getWorkoutsByPersonId(int id) {
        String sql = WORKOUT_SELECT + " WHERE p.person_id = ? ORDER BY date DESC";
        try {
            return template.query(sql, WORKOUTS_ROW_MAPPER, id);
        } catch (DaoException e) {
            throw new DaoException("Error fetching lift by lift name: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Workout> getWorkoutsByDate(LocalDate date) {

        String sql = WORKOUT_SELECT + " WHERE date = ?";
        try {
            return template.query(sql, WORKOUTS_ROW_MAPPER, date);
        } catch (DaoException e) {
            throw new DaoException("Error fetching workouts by date: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Workout> getWorkoutsByDateAndPersonId(int id, LocalDate date) {

        String sql = WORKOUT_SELECT + " WHERE p.person_id = ? AND date = ? ORDER BY date DESC";
        try {
            return template.query(sql, WORKOUTS_ROW_MAPPER, id, date);
        } catch (DaoException e) {
            throw new DaoException("Error fetching workouts by date: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Workout> getWorkoutsByDateRange(LocalDate startDate, LocalDate endDate) {
        String sql = WORKOUT_SELECT + " WHERE date BETWEEN ? AND ?";
        try {
            sql += " ORDER BY date ASC";
            return template.query(sql, WORKOUTS_ROW_MAPPER, startDate, endDate);
        } catch (DaoException e) {
            throw new DaoException("Error fetching workouts by date range: " + e.getMessage(), e);
        }

    }
    @Override
    public  List<Workout> getWorkoutsByDateRangeAndPersonId(int id, LocalDate startDate, LocalDate endDate){
        String sql = WORKOUT_SELECT + " WHERE p.person_id = ? AND date BETWEEN ? AND ?";
            try {
                sql += " ORDER BY date ASC";
                return template.query(sql, WORKOUTS_ROW_MAPPER, id, startDate, endDate);
            } catch (DaoException e) {
                throw new DaoException("Error fetching workouts by date range: " + e.getMessage(), e);
            }

    }
    @Override
    public List<Workout> getWorkoutsByLiftName(String liftName) {
        liftName = "%" + liftName + "%";
        String sql = WORKOUT_SELECT + " WHERE l.lift_name ILIKE ? ORDER BY date DESC";
        try {
            return template.query(sql, WORKOUTS_ROW_MAPPER, liftName);
        } catch (DaoException e) {
            throw new DaoException("Error fetching lift by lift name: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Workout> getWorkoutsByLiftNameAndPersonId(int id, String liftName) {
        liftName = "%" + liftName + "%";
        String sql = WORKOUT_SELECT + " WHERE p.person_id = ? AND l.lift_name ILIKE ? ORDER BY date DESC";
        try {
            return template.query(sql, WORKOUTS_ROW_MAPPER, id, liftName);
        } catch (DaoException e) {
            throw new DaoException("Error fetching lift by lift name: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Workout> getWorkoutsByPersonLastName(String lastName) {
        lastName = "%" + lastName + "%";
        String sql = WORKOUT_SELECT + " WHERE last_name ILIKE ? ORDER BY date DESC";
        try {
            return template.query(sql, WORKOUTS_ROW_MAPPER, lastName);
        } catch (DaoException e) {
            throw new DaoException("Error fetching workouts by member's last name: " + e.getMessage(), e);
        }
    }

    @Override
    public Workout getWorkoutById(int workoutId){
        String sql = WORKOUT_SELECT + " WHERE pl.workout_id = ?";
        try {
            SqlRowSet rowSet = template.queryForRowSet(sql, workoutId);
            if(rowSet.next()){
                return mapRowToWorkouts(rowSet);
            }
            return null;
        } catch (Exception e) {
            throw new DaoException("Error fetching workout by ID: " + e.getMessage(), e);
        }

    }

    @Override
    public Workout createWorkout(Workout workout) {
        String sql = "INSERT INTO person_lift (person_id, lift_id, date, sets, reps, max_weight, notes) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING workout_id";

        try {
            return workoutBuildMapping(workout, sql);
        } catch (Exception e) {
            throw new DaoException("Error creating workout: " + e.getMessage(), e);
        }
    }

    @Override
    public Workout updateWorkout(Workout workout, int workoutId) {

            String sql = "UPDATE person_lift " +
                    "SET person_id = ?, lift_id = ?, date = ?, sets = ?, reps = ?, max_weight = ?, notes = ? " +
                    "WHERE workout_id = ?";
            try {
                template.update(sql,
                        workout.getPersonId(),
                        workout.getLiftId(),
                        workout.getDate(),
                        workout.getSets(),
                        workout.getReps(),
                        workout.getMaxWeight(),
                        workout.getNotes(),
                        workoutId
                );

                return getWorkoutById(workoutId);
            } catch (Exception e) {
                throw new DaoException("Error updating workout: " + e.getMessage(), e);
            }
    }

    @Override
    public boolean deleteWorkout(int workoutId) {
        String sql = "DELETE FROM person_lift WHERE workout_id = ?";
        try {
            int rowsAffected = template.update(sql, workoutId);
            return rowsAffected > 0;
        } catch (Exception e) {
            throw new DaoException("Error deleting person lift: " + e.getMessage(), e);
        }
    }

    private static Workout mapRowToWorkouts(SqlRowSet rowset) {
        Integer workoutId = rowset.getInt("workout_id");
        Integer personId = rowset.getInt("person_id");
        Integer liftId = rowset.getInt("lift_id");
        String personFirstName = rowset.getString("first_name");
        String personLastName = rowset.getString("last_name");
        String liftName = rowset.getString("lift_name");
        LocalDate date = rowset.getDate("date").toLocalDate();
        Integer sets = rowset.getInt("sets");
        Integer reps = rowset.getInt("reps");
        Integer maxWeight = rowset.getInt("max_weight");
        String notes = rowset.getString("notes");

        return new Workout(workoutId, personId, liftId, personFirstName, personLastName, liftName, date, sets, reps, maxWeight, notes);
    }

    private static final RowMapper<Workout> WORKOUTS_ROW_MAPPER = (results, rowNum) -> new Workout(
            results.getInt("workout_id"),
            results.getInt("person_id"),
            results.getInt("lift_id"),
            results.getString("first_name"),
            results.getString("last_name"),
            results.getString("lift_name"),
            results.getObject("date", LocalDate.class),
            results.getInt("sets"),
            results.getInt("reps"),
            results.getInt("max_weight"),
            results.getString("notes")
    );

    private Workout workoutBuildMapping(Workout workout, String sql) {
        Integer queryId = template.queryForObject(sql, int.class,
                workout.getPersonId(),
                workout.getLiftId(),
                workout.getDate(),
                workout.getSets(),
                workout.getReps(),
                workout.getMaxWeight(),
                workout.getNotes());

        return getWorkoutById(queryId);
    }
}
