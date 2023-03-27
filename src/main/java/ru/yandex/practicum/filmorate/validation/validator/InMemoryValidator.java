package ru.yandex.practicum.filmorate.validation.validator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.film.FilmStorageDao;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserDbService;

import javax.validation.ValidationException;

@Service
@Slf4j
public class InMemoryValidator implements Validator {
    private final UserDbService userService;
    private final FilmStorageDao filmService;

    @Autowired
    public InMemoryValidator(UserDbService userService, FilmStorageDao filmService) {
        this.userService = userService;
        this.filmService = filmService;
    }

    public void checkIfUserExists(User user) {
        if (user.getId() == 0) {
            throw new ValidationException("Id пользователя не передан на сервер");
        }

        if (userService.getUserById(user.getId()) == null) {
            throw new EntityNotFoundException(String.format("Пользователя с таким %d нет", user.getId()));
        }
    }

    public void checkIfUserExistById(Long userId) {
        if (userService.getUserById(userId) == null) {
            throw new EntityNotFoundException(String.format("Пользователя с таким %d нет", userId));
        }
    }

    public void checkIfFilmExists(Film film) {
        if (film.getId() == 0) {
            throw new ValidationException("Id фильма не передан на сервер");
        }

        if (filmService.getFilmById(film.getId()) == null) {
            throw new EntityNotFoundException(String.format("Фильма с таким %d нет", film.getId()));
        }
    }

    public void checkIfFilmExistById(Long filmId) {
        if (filmService.getFilmById(filmId) == null) {
            throw new EntityNotFoundException(String.format("Фильма с таким %d нет", filmId));
        }
    }

    @Override
    public void checkIfMpaExistById(Integer mpaId) {

    }

    @Override
    public void checkIfGenreExistById(Integer genreId) {

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
