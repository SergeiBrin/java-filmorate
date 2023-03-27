package ru.yandex.practicum.filmorate.dao.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Set;

public interface FilmStorageDao {
    List<Film> getAllFilms();
    Film getFilmById(Long filmId);
    Set<Film> getPopularFilms();
    Film createFilm(Film film);
    Film updateFilm(Film film);
}
