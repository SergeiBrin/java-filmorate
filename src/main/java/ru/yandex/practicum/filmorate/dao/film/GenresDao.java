package ru.yandex.practicum.filmorate.dao.film;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Set;

public interface GenresDao {
    List<Genre> getAllGenres();
    Genre getGenreById(Integer genreId);
    Set<Genre> getGenresForFilm(Long filmId);
}
