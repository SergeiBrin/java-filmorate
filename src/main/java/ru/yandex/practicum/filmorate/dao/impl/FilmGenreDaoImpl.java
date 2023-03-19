package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmGenreDao;

@Component
@Slf4j
public class FilmGenreDaoImpl implements FilmGenreDao {
    JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmGenreDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Boolean addFilmGenre(Integer genreId, Long filmId) {
        String sqlQuery = "insert into film_genre (film_id, genre_id) values (?, ?)";

        Boolean isAddFilm = jdbcTemplate.update(sqlQuery, filmId, genreId) > 0;

        if (isAddFilm) {
            log.info("Фильм с id {} и genreId {} добавлен в таблицу film_genre", filmId, genreId);
        }

        return isAddFilm;
    }

    @Override
    public Boolean deleteFilmGenres(Long filmId) {
        String sqlQuery = "delete " +
                          "from film_genre " +
                          "where film_id = ?";

        Boolean isDeleteFilm = jdbcTemplate.update(sqlQuery, filmId) > 0;

        if (isDeleteFilm) {
            log.info("Фильм с id {} удален из таблицы film_genre", filmId);
        }

        return isDeleteFilm;
    }
}
