package ru.yandex.practicum.filmorate.validation.validator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserDbService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import javax.validation.ValidationException;

@Service
@Slf4j
public class RAMValidator implements Validator {
    private final UserDbService userService;
    private final FilmStorage filmService;

    @Autowired
    public RAMValidator(UserDbService userService, FilmStorage filmService) {
        this.userService = userService;
        this.filmService = filmService;
    }

    public void checkIfUserExistsById(User user) {
        if (user.getId() == 0) {
            throw new ValidationException("Id пользователя не передан на сервер");
        }

        if (userService.getUserById(user.getId()) == null) {
            throw new UserNotFoundException(String.format("Пользователя с таким %d нет", user.getId()));
        }
    }

    public void checkUserByPathVariableId(Long userId) {
        if (userId == null) {
            throw new ValidationException("Id пользователя и/или друга не передан в PathVariable");
        }

        if (userService.getUserById(userId) == null) {
            throw new UserNotFoundException(String.format("Пользователя с таким %d нет", userId));
        }
    }

    public void checkIfFilmExistsById(Film film) {
        if (film.getId() == 0) {
            throw new ValidationException("Id фильма не передан на сервер");
        }

        if (filmService.getFilmById(film.getId()) == null) {
            throw new FilmNotFoundException(String.format("Фильма с таким %d нет", film.getId()));
        }
    }

    public void checkFilmByPathVariableId(Long filmId) {
        if (filmId == null) {
            throw new ValidationException("Id фильма не передан в PathVariable");
        }

        if (filmService.getFilmById(filmId) == null) {
            throw new FilmNotFoundException(String.format("Фильма с таким %d нет", filmId));
        }
    }

    @Override
    public void checkMpaByPathVariableId(Integer mpaId) {

    }

    @Override
    public void checkGenreByPathVariableId(Integer genreId) {

    }

    // Валидация имени. Если имя пустое, то login становится именем.
    @Override
    public User checkForName(User user) {
        boolean isNameCorrect = (user.getName() != null) && (!user.getName().isEmpty());

        if (!isNameCorrect) {
            user.setName(user.getLogin());
        }

        return user;
    }
}
