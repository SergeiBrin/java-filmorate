package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
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
    public Film postFilm(@RequestBody Film film) {
        validateFilm(film, "POST");

        film.setId(++id);

        films.put(film.getId(), film);
        log.info("Фильм добавлен {}", film);

        return film;
    }

    // обновление фильма
    @PutMapping
    public Film putFilm(@RequestBody Film film) {
        validateFilm(film, "PUT");

        films.put(film.getId(), film);
        log.info("Фильм обновлен {}", film);

        return film;
    }

    // Проверка тела запроса на несоответствия правилам API.
    private void validateFilm(Film film, String methodName) {
        List<String> exceptionMessage = new ArrayList<>();

        if (film == null) {
            throw new ValidationException(methodName + ": [Запрос пустой]");
        }

        boolean isThereAnId = films.containsKey(film.getId());
        boolean isNameCorrect = checkName(film.getName());
        boolean isDescriptionCorrect = checkDescription(film.getDescription());
        boolean isReleaseDateCorrect = checkReleaseDate(film.getReleaseDate());
        boolean isDurationCorrect = checkDuration(film.getDuration());

        switch (methodName) {
            case "POST" -> {
                if (film.getId() != 0) {
                    exceptionMessage.add("В метод POST нельзя передавать id фильма");
                }
            }
            case "PUT" -> {
                if (!isThereAnId) {
                    exceptionMessage.add("Фильма с таким id нет");
                }
            }
        }

        if (!isNameCorrect) {
            exceptionMessage.add("Название фильма пустое");
        }

        if (!isDescriptionCorrect) {
            exceptionMessage.add("Описание фильма больше 200 символов");
        }

        if (!isReleaseDateCorrect) {
            exceptionMessage.add("Дата фильма раньше 28.12.1895");
        }

        if (!isDurationCorrect) {
            exceptionMessage.add("Продолжительность фильма отрицательная");
        }

        // Проверка на то, что лист сообщений об ошибках пустой.
        // Если лист не пустой, то выбрасываю исключение.
        if (!exceptionMessage.isEmpty()) {
            String logMessage = methodName + ": " + exceptionMessage;
            log.warn(logMessage);
            throw new ValidationException(logMessage);
        }
    }

    private boolean checkName(String name) {
        return (name != null) && (!name.isEmpty());
    }

    private boolean checkDescription(String description) {
        return (description != null) && (description.length() <= 200);
    }

    private boolean checkReleaseDate(LocalDate releaseDate) {
        LocalDate checkDate = LocalDate.of(1895, 12, 28);
        return releaseDate.isAfter(checkDate) || releaseDate.equals(checkDate);
    }

    private boolean checkDuration(Long duration) {
        return duration >= 0;
    }
}
