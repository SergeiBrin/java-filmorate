package ru.yandex.practicum.filmorate.service.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.film.FilmStorageDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InMemoryFilmService implements FilmService{
    private final FilmStorageDao filmStorage;

    @Autowired
    public InMemoryFilmService(FilmStorageDao filmStorage) {
        this.filmStorage = filmStorage;
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(Long filmId)  {
        return filmStorage.getFilmById(filmId);
    }

    public List<Film> getMostPopularFilm(Long count) {
        return filmStorage.getPopularFilms()
                .stream()
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public Film deleteLikeForFilm(Long filmId, Long userId) {
        Film film = filmStorage.getFilmById(filmId);
        film.deleteLike(userId);
        filmStorage.updateFilm(film);

        return film;
    }

    public Film likeTheFilm(Long filmId, Long userId) {
        Film film = filmStorage.getFilmById(filmId);
        film.addLike(userId);
        filmStorage.updateFilm(film);

        return film;
    }

    // Если разработка вернется в Ram хранение,
    // то эти методы нужно будет доработать.
    @Override
    public List<Mpa> getAllMpa() {
        return null;
    }

    @Override
    public Mpa getMpaById(Integer mpaId) {
        return null;
    }

    @Override
    public List<Genre> getAllGenres() {
        return null;
    }

    @Override
    public Genre getGenreById(Integer genreId) {
        return null;
    }
}

