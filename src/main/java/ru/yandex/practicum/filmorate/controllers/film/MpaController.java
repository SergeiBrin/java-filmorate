package ru.yandex.practicum.filmorate.controllers.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.validation.validator.Validator;

import java.util.List;

@RestController
public class MpaController {
    private final FilmService filmService;
    private final Validator validator;

    @Autowired
    public MpaController(FilmService filmService, Validator validator) {
        this.filmService = filmService;
        this.validator = validator;
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
}
