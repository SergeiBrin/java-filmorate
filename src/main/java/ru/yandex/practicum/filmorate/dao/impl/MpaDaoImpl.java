package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@Slf4j
public class MpaDaoImpl implements MpaDao {
    JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Mpa> getAllMpa() {
        String sqlQuery = "select mpa_id, mpa_name " +
                          "from mpa";

        List<Mpa> allMpa = jdbcTemplate.query(sqlQuery, this::mapRowToMpa);
        log.info("Список всех mpa {} отправлен клиенту", allMpa);

        return allMpa;
    }

    @Override
    public Mpa getMpaById(Integer mpaId) {
        String sqlQuery = "select mpa_id, mpa_name " +
                          "from mpa " +
                          "where mpa_id = ?";

        Mpa mpaById = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToMpa, mpaId);
        log.info("Mpa {} с id: {} отправлен клиенту", mpaById, mpaById);

        return mpaById;
    }

    private Mpa mapRowToMpa(ResultSet resultSet, int rowNum) throws SQLException {
        return Mpa.builder()
                .id(resultSet.getInt("mpa_id"))
                .name(resultSet.getString("mpa_name"))
                .build();
    }
}
