package ru.yandex.practicum.filmorate.dao.impl.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.film.LikesDao;
import ru.yandex.practicum.filmorate.model.Likes;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@Slf4j
public class LikesDaoImpl implements LikesDao {
    JdbcTemplate jdbcTemplate;

    @Autowired
    public LikesDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Boolean addLikeToFilm(Long filmId, Long userId) {
        String sqlQuery = "insert into likes (user_id, film_id) values (?, ?)";

        Boolean isAddLike = jdbcTemplate.update(sqlQuery, userId, filmId) > 0;

        if (isAddLike) {
            log.info("В таблицу likes добавлена связь между фильмом с id {} и пользователем с id {}", filmId, userId);
        }

        return isAddLike;
    }

    @Override
    public Boolean deleteLikeForFilm(Long filmId, Long userId) {
        String sqlQuery = "delete " +
                "from likes " +
                "where user_id = ? and film_id = ?";

        Boolean isDeleteLike = jdbcTemplate.update(sqlQuery, userId, filmId) > 0;

        if (isDeleteLike) {
            log.info("В таблице likes удалена связь между фильмом с id {} и пользователем с id {}", filmId, userId);
        }

        return isDeleteLike;
    }

    @Override
    public List<Likes> getLikesForFilm(Long filmId) {
        String sqlQuery = "select * " +
                          "from likes " +
                          "where film_id = ?";

        List<Likes> likesForFilm = jdbcTemplate.query(sqlQuery, this::mapRowToLikes, filmId);
        log.info("Из таблицы likes возвращен список id пользователей, поставивших лайк фильму с id {}", filmId);

        return likesForFilm;
    }

    private Likes mapRowToLikes(ResultSet resultSet, int rowNum) throws SQLException {
        return Likes.builder()
                .userId(resultSet.getLong("user_id"))
                .filmId(resultSet.getLong("film_id"))
                .build();
    }
}
