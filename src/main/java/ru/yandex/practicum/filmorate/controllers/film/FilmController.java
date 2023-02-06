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
        validator.checkFilmByPathVariableId(filmId);
        return filmService.getFilmById(filmId);
    }

    @GetMapping("/popular") // Тут нужно разобраться, что писать
    public List<Film> getMostPopularFilm(@RequestParam(defaultValue = "10") Long count) {
        return filmService.getMostPopularFilm(count);
    }

    // добавление фильма
    @PostMapping
    public Film postFilm(@Valid @RequestBody Film film) {
        return filmService.postFilm(film);
    }

    // обновление фильма
    @PutMapping
    public Film putFilm(@Valid @RequestBody Film film) {
        validator.checkIfFilmExistsById(film);
        return filmService.putFilm(film);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public Film likeTheFilm(@PathVariable Long filmId,
                            @PathVariable Long userId) {
        validator.checkFilmByPathVariableId(filmId);
        validator.checkUserByPathVariableId(userId);

        return filmService.likeTheFilm(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public Film deleteLikeForFilm(@PathVariable Long filmId,
                                  @PathVariable Long userId) {
        validator.checkFilmByPathVariableId(filmId);
        validator.checkUserByPathVariableId(userId);

        return filmService.deleteLikeForFilm(filmId, userId);
    }
}
