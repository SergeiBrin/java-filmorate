package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage filmStorage) {
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

    public Film postFilm(Film film) {
        return filmStorage.postFilm(film);
    }

    public Film putFilm(Film film) {
        return filmStorage.putFilm(film);
    }

    public Film deleteLikeForFilm(Long filmId, Long userId) {
        Film film = filmStorage.getFilmById(filmId);
        film.deleteLike(userId);
        filmStorage.updatePopularFilms(film);

        return film;
    }

    public Film likeTheFilm(Long filmId, Long userId) {
        Film film = filmStorage.getFilmById(filmId);
        film.addLike(userId);
        filmStorage.updatePopularFilms(film);

        return film;
    }
}
