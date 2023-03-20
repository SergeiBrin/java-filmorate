package ru.yandex.practicum.filmorate.controllers.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.validation.validator.Validator;

import javax.validation.Valid;
import java.util.List;

@RestController
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
    @GetMapping("/films")
    public List<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    @GetMapping("/films/{filmId}")
    public Film getFilmById(@PathVariable Long filmId) {
        validator.checkIfFilmExistById(filmId);
        return filmService.getFilmById(filmId);
    }

    @GetMapping("/films/popular")
    public List<Film> getMostPopularFilm(@RequestParam(defaultValue = "10") Long count) {
        return filmService.getMostPopularFilm(count);
    }

    @GetMapping("/mpa")
    public List<Mpa> getAllMpa() {
        return filmService.getAllMpa();
    }

    @GetMapping("/mpa/{mpaId}")
    public Mpa getMpaById(@PathVariable Integer mpaId) {
        validator.checkIfMpaExistById(mpaId);
        return filmService.getMpaById(mpaId);
    }

    @GetMapping("/genres")
    public List<Genre> getAllGenres() {
        return filmService.getAllGenres();
    }

    @GetMapping("/genres/{genreId}")
    public Genre getGenreById(@PathVariable Integer genreId) {
        validator.checkIfGenreExistById(genreId);
        return filmService.getGenreById(genreId);
    }

    @PostMapping("/films")
    public Film createFilm(@Valid @RequestBody Film film) {
        return filmService.createFilm(film);
    }

    // обновление фильма
    @PutMapping("/films")
    public Film updateFilm(@Valid @RequestBody Film film) {
        validator.checkIfFilmExists(film);
        return filmService.updateFilm(film);
    }

    @PutMapping("/films/{filmId}/like/{userId}")
    public Film likeTheFilm(@PathVariable Long filmId,
                            @PathVariable Long userId) {
        validator.checkIfFilmExistById(filmId);
        validator.checkIfUserExistById(userId);

        return filmService.likeTheFilm(filmId, userId);
    }

    @DeleteMapping("/films/{filmId}/like/{userId}")
    public Film deleteLikeForFilm(@PathVariable Long filmId,
                                  @PathVariable Long userId) {
        validator.checkIfFilmExistById(filmId);
        validator.checkIfUserExistById(userId);

        return filmService.deleteLikeForFilm(filmId, userId);
    }
}
