package ru.yandex.practicum.filmorate.controllers.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final FilmService filmService;

    @Autowired
    public FilmController(InMemoryFilmStorage filmStorage, InMemoryUserStorage userStorage, FilmService filmService) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.filmService = filmService;
    }

    // получение всех фильмов
    @GetMapping
    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    @GetMapping("/{filmId}")
    public Film getFilmById(@PathVariable Long filmId) {
        if (filmStorage.getFilmById(filmId) == null) {
            throw new FilmNotFoundException(String.format("Фильма с таким %d нет", filmId));
        }

        return filmStorage.getFilmById(filmId);
    }

    @GetMapping("/popular") // Тут нужно разобраться, что писать
    public List<Film> getMostPopularFilm(@RequestParam(defaultValue = "10") Long count) {
        return filmService.getMostPopularFilm(count);
    }

    // добавление фильма
    @PostMapping
    public Film postFilm(@Valid @RequestBody Film film) {
        return filmStorage.postFilm(film);
    }

    // обновление фильма
    @PutMapping
    public Film putFilm(@Valid @RequestBody Film film) {
        if (filmStorage.getFilmById(film.getId()) == null) {
            throw new FilmNotFoundException(String.format("Фильма с таким %d нет", film.getId()));
        }

        return filmStorage.putFilm(film);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public Film likeTheFilm(@PathVariable Long filmId,
                            @PathVariable Long userId) {
        if (filmId == null || userId == null) {
            throw new ValidationException("Id фильма и/или пользователя не передан в PathVariable");
        }

        if (filmStorage.getFilmById(filmId) == null) {
            throw new FilmNotFoundException(String.format("Фильма с таким %d нет", filmId));
        }

        if (userStorage.getUserById(userId) == null) {
            throw new UserNotFoundException(String.format("Пользователя с таким %d нет", userId));
        }

        return filmService.likeTheFilm(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public Film deleteLikeForFilm(@PathVariable Long filmId,
                                  @PathVariable Long userId) {
        if (filmId == null || userId == null) {
            throw new ValidationException("Id фильма и/или пользователя не передан в PathVariable");
        }

        if (filmStorage.getFilmById(filmId) == null) {
            throw new FilmNotFoundException(String.format("Фильма с таким %d нет", filmId));
        }

        if (userStorage.getUserById(userId) == null) {
            throw new UserNotFoundException(String.format("Пользователя с таким %d нет", userId));
        }

        return filmService.deleteLikeForFilm(filmId, userId);
    }
}
