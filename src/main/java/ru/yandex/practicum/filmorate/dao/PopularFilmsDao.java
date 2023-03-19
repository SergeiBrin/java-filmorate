package ru.yandex.practicum.filmorate.dao;

public interface PopularFilmsDao {
    Boolean addFilmInPopularList(Long filmId);
    Boolean updateFilmInPopularList(Long filmId, Long likesCount);
    Long getLikesCountForFilm(Long filmId);
}
