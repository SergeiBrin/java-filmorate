package ru.yandex.practicum.filmorate.dao.impl.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.film.FilmStorageDao;
import ru.yandex.practicum.filmorate.dao.film.GenresDao;
import ru.yandex.practicum.filmorate.dao.film.LikesDao;
import ru.yandex.practicum.filmorate.dao.film.MpaDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Likes;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Slf4j
@Primary
public class FilmDbStorageDao implements FilmStorageDao {
    private final JdbcTemplate jdbcTemplate;
    private final MpaDao mpaDao;
    private final GenresDao genresDao;
    private final LikesDao likesDao;

    public FilmDbStorageDao(JdbcTemplate jdbcTemplate,
                            MpaDao mpaDao,
                            GenresDao genresDao,
                            LikesDao likesDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaDao = mpaDao;
        this.genresDao = genresDao;
        this.likesDao = likesDao;
    }

    @Override
    public List<Film> getAllFilms() {
        String sqlQuery = "select film_id, name, description, release_date, duration, mpa_id " +
                          "from films";

        List<Film> allFilms = jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
        log.info("Список всех фильмов {} отправлен клиенту", allFilms);

        return allFilms;
    }

    @Override
    public Film getFilmById(Long filmId) {
        String sqlQuery = "select film_id, name, description, release_date, duration, mpa_id " +
                          "from films " +
                          "where film_id = ?";

        Film filmById = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, filmId);
        log.info("Фильм {} с id: {} отправлен клиенту", filmById, filmId);

        return filmById;
    }

    @Override
    public Set<Film> getPopularFilms() {
        // Альтернативный способ поиска популярных фильмов.
        // Сортировка идет с помощью таблиц films и likes ↓ ↓ ↓


        String sqlQuery = "select f.film_id, f.name, f.description, f.release_date, f.duration, f.mpa_id " +
                          "from films as f " +
                          "left outer join likes as l on f.film_id = l.film_id " +
                          "group by f.film_id " +
                          "order by count(user_id) desc";

        Set<Film> popularFilms = new HashSet<>(jdbcTemplate.query(sqlQuery, this::mapRowToFilm));
        log.info("Cписок популярных фильмов {} отправлен клиенту", popularFilms);

        return popularFilms;
    }

    @Override
    public Film createFilm(Film film) {
        String sqlQuery = "insert into films (name, description, release_date, duration, mpa_id) values (?, ?, ?, ?, ?)";

        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId());

        // В таблице popular_films хранится film_id с количеством лайков.
        Film newFilm = findLastFilm();
        log.info("Фильм {} добавлен в таблицу films", newFilm);

        return newFilm;
    }

    @Override
    public Film updateFilm(Film film) {
        String sqlQuery = "update films set name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ? " +
                          "where film_id = ?";

        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());

        Film updateFilm = getFilmById(film.getId());
        log.info("Фильм {} обновлен в таблице films", updateFilm);

        return updateFilm;
    }

    private Film findLastFilm() {
        String sqlQuery = "select film_id, name, description, release_date, duration, mpa_id " +
                "from films " +
                "order by film_id desc " +
                "limit 1";

        Film lastFilm = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm);
        log.info("Из таблицы films взят последний добавленный фильм {}", lastFilm);

        return lastFilm;
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = Film.builder()
                .id(resultSet.getLong("film_id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(resultSet.getLong("duration"))
                .mpa(mpaDao.getMpaById(resultSet.getInt("mpa_id"))) // Вместо Null можно подставить метод, который вернет имя mpa
                .genres(genresDao.getGenresForFilm(resultSet.getLong("film_id")))
                .build();

        // Добавляю id пользователей в фильм, чтобы клиент увидел тех,
        // кто поставил лайки.
        List<Likes> likes = likesDao.getLikesForFilm(film.getId());
        likes.forEach(like -> film.addLike(like.getUserId()));

        return film;
    }
}
