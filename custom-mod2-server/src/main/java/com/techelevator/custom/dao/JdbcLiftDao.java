package com.techelevator.custom.dao;

import com.techelevator.custom.exception.DaoException;
import com.techelevator.custom.model.Lift;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class JdbcLiftDao implements LiftDao{

    private static final String LIFT_SELECT = "SELECT lift.lift_id, lift.lift_name" +
            " FROM lift ";

    private final JdbcTemplate jdbcTemplate;

    public JdbcLiftDao(DataSource datasource){
        this.jdbcTemplate = new JdbcTemplate(datasource);
    }

    private final static RowMapper<Lift> LIFT_ROW_MAPPER = new RowMapper<Lift>() {
        @Override
        public Lift mapRow(ResultSet results, int rowNum) throws SQLException {
            Lift lift = new Lift(results.getInt("lift_id"), results.getString("lift_name"));
            return lift;
        }
    };

    @Override
    public List<Lift> getAllLifts() {
        String sql = LIFT_SELECT + "ORDER BY lift_name";
        try {
            return jdbcTemplate.query(sql, LIFT_ROW_MAPPER);
        } catch (DaoException e) {
            throw new DaoException("Error fetching all lifts: " + e.getMessage(), e);
        }
    }
}
