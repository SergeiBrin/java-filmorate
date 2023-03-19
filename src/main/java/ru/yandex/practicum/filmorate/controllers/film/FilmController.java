package ru.yandex.practicum.filmorate.controllers.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.film.FilmDbService;
import ru.yandex.practicum.filmorate.validation.validator.Validator;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
public class FilmController {
    private final FilmDbService filmService;
    private final Validator validator;

    @Autowired
    public FilmController(FilmDbService filmService, Validator validator) {
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
        validator.checkFilmByPathVariableId(filmId);
        return filmService.getFilmById(filmId);
    }

    @GetMapping("/films/popular") // Тут нужно разобраться, что писать
    public List<Film> getMostPopularFilm(@RequestParam(defaultValue = "10") Long count) {
        return filmService.getMostPopularFilm(count);
    }

    @GetMapping("/mpa")
    public List<Mpa> getAllMpa() {
        return filmService.getAllMpa();
    }
    @GetMapping("/mpa/{mpaId}")
    public Mpa getMpaById(@PathVariable Integer mpaId) {
        validator.checkMpaByPathVariableId(mpaId);
        return filmService.getMpaById(mpaId);
    }

    @GetMapping("/genres")
    public List<Genre> getAllGenres() {
        return filmService.getAllGenres();
    }

    @GetMapping("/genres/{genreId}")
    public Genre getGenreById(@PathVariable Integer genreId) {
        validator.checkGenreByPathVariableId(genreId);
        return filmService.getGenreById(genreId);
    }

    @PostMapping("/films")
    public Film postFilm(@Valid @RequestBody Film film) {
        return filmService.postFilm(film);
    }

    // обновление фильма
    @PutMapping("/films")
    public Film putFilm(@Valid @RequestBody Film film) {
        validator.checkIfFilmExistsById(film);
        return filmService.putFilm(film);
    }

    @PutMapping("/films/{filmId}/like/{userId}")
    public Film likeTheFilm(@PathVariable Long filmId,
                            @PathVariable Long userId) {
        validator.checkFilmByPathVariableId(filmId);
        validator.checkUserByPathVariableId(userId);

        return filmService.likeTheFilm(filmId, userId);
    }

    @DeleteMapping("/films/{filmId}/like/{userId}")
    public Film deleteLikeForFilm(@PathVariable Long filmId,
                                  @PathVariable Long userId) {
        validator.checkFilmByPathVariableId(filmId);
        validator.checkUserByPathVariableId(userId);

        return filmService.deleteLikeForFilm(filmId, userId);
    }
}
