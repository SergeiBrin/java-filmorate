package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.PopularFilmsDao;

@Component
@Slf4j
public class PopularFilmsDaoImpl implements PopularFilmsDao {
    JdbcTemplate jdbcTemplate;

    @Autowired
    public PopularFilmsDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Boolean addFilmInPopularList(Long filmId) {
        String sqlQuery = "insert into popular_films (film_id, likes_count) values (?, ?)";

        Boolean isUpdate = jdbcTemplate.update(sqlQuery, filmId, 0) > 0;

        if (isUpdate) {
            log.info("Фильм с id {} добавлен в таблицу popular_films", filmId);
        }

        return isUpdate;
    }

    @Override
    public Boolean updateFilmInPopularList(Long filmId, Long likesCount) {
        String sqlQuery = "update popular_films set likes_count = ? " +
                          "where film_id = ?";

        Boolean isUpdate = jdbcTemplate.update(sqlQuery, likesCount, filmId) > 0;

        if (isUpdate) {
            log.info("Фильм с id {} обновлен в таблице popular_films", filmId);
        }

        return isUpdate;
    }

    public Long getLikesCountForFilm(Long filmId) {
        String sqlQuery = "select likes_count " +
                          "from popular_films " +
                          "where film_id = ?";

        Long likesCount = jdbcTemplate.queryForObject(sqlQuery, Long.class, filmId);
        log.info("Количество лайков {} для фильма с id {} отправлено клиенту", likesCount, filmId);

        return likesCount;
    }
}
