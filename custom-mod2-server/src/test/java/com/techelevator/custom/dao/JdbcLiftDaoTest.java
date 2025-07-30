package com.techelevator.custom.dao;

import com.techelevator.custom.model.Lift;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JdbcLiftDaoTest extends BaseDaoTest{
    private JdbcLiftDao dao;

    @BeforeEach
    public void setup() {
        dao = new JdbcLiftDao(dataSource);
    }

    @Test
    public void getAllLifts_returns_all_lifts_sorted_by_name() {
        List<Lift> lifts = dao.getAllLifts();

        assertNotNull(lifts, "Expected non-null list of lifts");
        assertFalse(lifts.isEmpty(), "Expected lifts list to not be empty");
        assertTrue(lifts.size() == 71, "Expected 71 lifts from seed data");

    }
}
