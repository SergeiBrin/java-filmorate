package ru.yandex.practicum.filmorate.validation.validator;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.ValidationException;

@Service
@Data
public class Validator {
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;

    @Autowired
    public Validator(UserStorage userStorage, FilmStorage filmStorage) {
        this.userStorage = userStorage;
        this.filmStorage = filmStorage;
    }

    public void checkIfUserExistsById(User user) {
        if (user.getId() == 0) {
            throw new ValidationException("Id пользователя не передан на сервер");
        }

        if (userStorage.getUserById(user.getId()) == null) {
            throw new UserNotFoundException(String.format("Пользователя с таким %d нет", user.getId()));
        }
    }

    public void checkUserByPathVariableId(Long userId) {
        if (userId == null) {
            throw new ValidationException("Id пользователя и/или друга не передан в PathVariable");
        }

        if (userStorage.getUserById(userId) == null) {
            throw new UserNotFoundException(String.format("Пользователя с таким %d нет", userId));
        }
    }

    public void checkIfFilmExistsById(Film film) {
        if (film.getId() == 0) {
            throw new ValidationException("Id фильма не передан на сервер");
        }

        if (filmStorage.getFilmById(film.getId()) == null) {
            throw new FilmNotFoundException(String.format("Фильма с таким %d нет", film.getId()));
        }
    }

    public void checkFilmByPathVariableId(Long filmId) {
        if (filmId == null) {
            throw new ValidationException("Id фильма не передан в PathVariable");
        }

        if (filmStorage.getFilmById(filmId) == null) {
            throw new FilmNotFoundException(String.format("Фильма с таким %d нет", filmId));
        }
    }
}
