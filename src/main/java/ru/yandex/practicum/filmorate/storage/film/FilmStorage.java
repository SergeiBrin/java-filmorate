package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Set;

public interface FilmStorage {
    List<Film> getAllFilms();
    Film getFilmById(Long filmId);
    Set<Film> getPopularFilms();
    Film postFilm(Film film);
    Film putFilm(Film film);
    void updatePopularFilms(Film film);
}
