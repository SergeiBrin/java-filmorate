package ru.yandex.practicum.filmorate.controllers.film;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.validation.validator.Validator;

import java.util.List;

@RestController
public class GenreController {
    private final Validator validator;
    private final FilmService filmService;

    public GenreController(Validator validator, FilmService filmService) {
        this.validator = validator;
        this.filmService = filmService;
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
}
