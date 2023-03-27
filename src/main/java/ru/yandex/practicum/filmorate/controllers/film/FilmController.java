package ru.yandex.practicum.filmorate.controllers.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.validation.validator.Validator;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final FilmService filmService;
    private final Validator validator;

    @Autowired
    public FilmController(FilmService filmService, Validator validator) {
        this.filmService = filmService;
        this.validator = validator;
    }

    // получение всех фильмов
    @GetMapping
    public List<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    @GetMapping("/{filmId}")
    public Film getFilmById(@PathVariable Long filmId) {
        validator.checkIfFilmExistById(filmId);
        return filmService.getFilmById(filmId);
    }

    @GetMapping("/popular")
    public List<Film> getMostPopularFilm(@RequestParam(defaultValue = "10") Long count) {
        return filmService.getMostPopularFilm(count);
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        return filmService.createFilm(film);
    }

    // обновление фильма
    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        validator.checkIfFilmExists(film);
        return filmService.updateFilm(film);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public Film likeTheFilm(@PathVariable Long filmId,
                            @PathVariable Long userId) {
        validator.checkIfFilmExistById(filmId);
        validator.checkIfUserExistById(userId);

        return filmService.likeTheFilm(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public Film deleteLikeForFilm(@PathVariable Long filmId,
                                  @PathVariable Long userId) {
        validator.checkIfFilmExistById(filmId);
        validator.checkIfUserExistById(userId);

        return filmService.deleteLikeForFilm(filmId, userId);
    }
}
