package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface FilmService {
    List<Film> getAllFilms();
    Film getFilmById(Long filmId);
    List<Film> getMostPopularFilm(Long count);
    List<Genre> getAllGenres();
    Genre getGenreById(Integer genreId);
    Film createFilm(Film film);
    Film updateFilm(Film film);
    Film likeTheFilm(Long filmId, Long userId);
    Film deleteLikeForFilm(Long filmId, Long userId);
    List<Mpa> getAllMpa();
    Mpa getMpaById(Integer mpaId);
}
