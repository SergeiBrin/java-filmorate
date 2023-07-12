package ru.yandex.practicum.filmorate.dao.impl.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.film.GenresDao;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Slf4j
public class GenresDaoImpl implements GenresDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenresDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> getAllGenres() {
        String sqlQuery = "select genre_id, genre_name " +
                          "from genres";

        List<Genre> allGenres = jdbcTemplate.query(sqlQuery, this::mapRowToGenres);
        log.info("Список всех жанров отправлен клиенту {}", allGenres);

        return allGenres;
    }

    @Override
    public Genre getGenreById(Integer genreId) {
        String sqlQuery = "select genre_id, genre_name " +
                          "from genres " +
                          "where genre_id = ?";

        Genre genreById = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToGenres, genreId);
        log.info("Жанр с id {} отправлен клиенту", genreById);

        return genreById;
    }

    @Override
    public Set<Genre> getGenresForFilm(Long filmId) {
        String sqlQuery = "select genre_id, genre_name " +
                          "from genres " +
                          "where genre_id in (select genre_id " +
                                             "from film_genre " +
                                             "where film_id = ?)";

        Set<Genre> genresForFilm = new HashSet<>(jdbcTemplate.query(sqlQuery, this::mapRowToGenres, filmId));
        log.info("Список жанров для фильма с id {} отправлен клиенту {}", filmId, genresForFilm);

        return genresForFilm;
    }

    private Genre mapRowToGenres(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getInt("genre_id"))
                .name(resultSet.getString("genre_name"))
                .build();
    }
}
