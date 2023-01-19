package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private int id;
    private final Map<Integer, Film> films = new HashMap<>();

    // получение всех фильмов
    @GetMapping
    public List<Film> getAllFilms() {
        log.info("Список всех фильмов {} отправлен клиенту", films.values());
        return new ArrayList<>(films.values());
    }

    // добавление фильма
    @PostMapping
    public Film postFilm(@Valid @RequestBody Film film) {
        if (film.getId() != 0) {
            log.warn("В метод POST передан id фильма");
            throw new ValidationException("В метод POST нельзя передавать id фильма");
        }

        film.setId(++id);

        films.put(film.getId(), film);
        log.info("Фильм добавлен {}", film);

        return film;
    }

    // обновление фильма
    @PutMapping
    public Film putFilm(@Valid @RequestBody Film film) {
        boolean isThereAnId = films.containsKey(film.getId());
        if (!isThereAnId) {
            log.warn("В метод PUT передан фильм с несуществующим id");
            throw new ValidationException("Фильма с таким id нет");
        }

        films.put(film.getId(), film);
        log.info("Фильм обновлен {}", film);

        return film;
    }
}
