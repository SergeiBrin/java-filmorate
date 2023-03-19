package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Likes;

import java.util.List;

public interface LikesDao {
    Boolean addLikeToFilm(Long filmId, Long userId);
    Boolean deleteLikeForFilm(Long filmId, Long userId);
    List<Likes> getLikesForFilm(Long filmId);
}
