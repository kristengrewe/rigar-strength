package com.techelevator.custom.dao;

import com.techelevator.custom.dto.Workout;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JdbcWorkoutDaoTest extends BaseDaoTest {
    private JdbcWorkoutDao workoutDao;

    @BeforeEach
    public void setup() {
        workoutDao = new JdbcWorkoutDao(dataSource);
    }

    @Test
    public void getAllWorkouts_returns_non_empty_list() {
        List<Workout> workouts = workoutDao.getAllWorkouts();

        assertNotNull(workouts);
        assertTrue(workouts.size() >= 1, "Expected at least one workout from seed data");
    }

    @Test
    public void getWorkoutsByPersonId_returns_workouts_for_person() {
        List<Workout> workouts = workoutDao.getWorkoutsByPersonId(1);

        assertNotNull(workouts);
        assertEquals(1, workouts.get(0).getPersonId());
    }

    @Test
    public void getWorkoutsByDate_returns_workouts_on_date() {
        LocalDate date = LocalDate.of(2024, 10, 29);
        List<Workout> workouts = workoutDao.getWorkoutsByDate(date);

        assertNotNull(workouts);
        assertEquals(date, workouts.get(0).getDate());
    }

    @Test
    public void getWorkoutsByDateAndPersonId_returns_filtered_workouts() {
        LocalDate date = LocalDate.of(2024, 10, 29);
        List<Workout> workouts = workoutDao.getWorkoutsByDateAndPersonId(2, date);

        assertNotNull(workouts);
        assertEquals(2, workouts.get(0).getPersonId());
        assertEquals(date, workouts.get(0).getDate());
    }

    @Test
    public void getWorkoutsByDateRange_returns_all_in_range() {
        LocalDate start = LocalDate.of(2024, 10, 1);
        LocalDate end = LocalDate.of(2024, 12, 31);

        List<Workout> workouts = workoutDao.getWorkoutsByDateRange(start, end);

        assertNotNull(workouts);

        if(workouts.size()>0) {
            LocalDate firstDate = workouts.get(0).getDate();
            assertTrue(!firstDate.isBefore(start) && !firstDate.isAfter(end),
                    "Date range filter is not working correctly");
        }
    }

    @Test
    public void getWorkoutsByDateRangeAndPersonId_returns_correct_subset() {
        LocalDate start = LocalDate.of(2024, 10, 1);
        LocalDate end = LocalDate.of(2024, 12, 31);

        List<Workout> workouts = workoutDao.getWorkoutsByDateRangeAndPersonId(1, start, end);

        assertNotNull(workouts);

        if (workouts.size()>0) {
            for (Workout w : workouts) {
                assertEquals(1, w.getPersonId(), "Workout has incorrect personId");
                assertTrue(!w.getDate().isBefore(start) && !w.getDate().isAfter(end),
                        "Workout date is outside the specified range");
            }
        }
    }

    @Test
    public void getWorkoutsByLiftName_returns_results() {
        List<Workout> workouts = workoutDao.getWorkoutsByLiftName("clean");

        assertNotNull(workouts);
        assertFalse(workouts.isEmpty());

        boolean found = false;
        for (Workout w : workouts) {
            if (w.getLiftName().toLowerCase().contains("clean")) {
                found = true;
                break;
            }
        }
        assertTrue(found, "Expected at least one workout with a lift name containing 'clean'");
    }

    @Test
    public void getWorkoutsByLiftNameAndPersonId_returns_filtered_results() {
        List<Workout> workouts = workoutDao.getWorkoutsByLiftNameAndPersonId(1, "clean");

        assertNotNull(workouts);
        if(workouts.size()>0) {
            boolean allMatch = true;
            for (Workout w : workouts) {
                if (w.getPersonId() != 1 || !w.getLiftName().toLowerCase().contains("clean")) {
                    allMatch = false;
                    break;
                }
            }
            assertTrue(allMatch, "Not all workouts matched person ID 1 and lift name containing 'clean'");
        }
    }

    @Test
    public void getWorkoutsByPersonLastName_returns_expected_results() {
        List<Workout> workouts = workoutDao.getWorkoutsByPersonLastName("grewe");

        assertNotNull(workouts);
        assertFalse(workouts.isEmpty());
    }

    @Test
    public void getWorkoutById_returns_expected_workout() {
        Workout workout = workoutDao.getWorkoutById(1);

        assertNotNull(workout);
        assertEquals(1, workout.getWorkoutId());
    }

    @Test
    public void createWorkout_creates_and_returns_workout() {
        Workout newWorkout = new Workout(
                0,
                1,  // Kristen
                1,  // 3-Position Clean
                "Kristen", "Grewe", "3-Position Clean",
                LocalDate.of(2025, 5, 10),
                4, 3, 100,
                "JUnit test insert"
        );

        Workout created = workoutDao.createWorkout(newWorkout);

        assertNotNull(created);
        assertEquals(newWorkout.getDate(), created.getDate());
        assertEquals(1, created.getPersonId());
        assertEquals(1, created.getLiftId());
        assertEquals("JUnit test insert", created.getNotes());
    }

    @Test
    public void updateWorkout_modifies_workout_successfully() {
        Workout existing = workoutDao.getWorkoutById(1);
        assertNotNull(existing);

        existing.setNotes("Updated by test");
        existing.setReps(10);

        Workout updated = workoutDao.updateWorkout(existing, existing.getWorkoutId());

        assertNotNull(updated);
        assertEquals("Updated by test", updated.getNotes());
        assertEquals(10, updated.getReps());
    }

    @Test
    public void deleteWorkout_removes_workout() {
        Workout newWorkout = new Workout(
                0, 1, 1, "Kristen", "Grewe", "3-Position Clean",
                LocalDate.of(2025, 5, 10), 2, 2, 100, "Delete me");

        Workout created = workoutDao.createWorkout(newWorkout);
        assertNotNull(created);

        boolean deleted = workoutDao.deleteWorkout(created.getWorkoutId());

        assertTrue(deleted);
        assertNull(workoutDao.getWorkoutById(created.getWorkoutId()));
    }
}
