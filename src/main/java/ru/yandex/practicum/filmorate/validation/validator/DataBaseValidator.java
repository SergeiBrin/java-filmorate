package ru.yandex.practicum.filmorate.validation.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.film.FilmDbService;
import ru.yandex.practicum.filmorate.service.user.UserDbService;

import javax.validation.ValidationException;

@Service
@Primary
public class DataBaseValidator implements Validator {
    private final UserDbService userService;
    private final FilmDbService filmService;

    @Autowired
    public DataBaseValidator(UserDbService userService, FilmDbService filmService) {
        this.userService = userService;
        this.filmService = filmService;
    }

    public void checkIfUserExists(User user) {
        if (user.getId() == 0) {
            throw new ValidationException("user_id не передан на сервер");
        }

        // Если метод jdbcTemplate.queryForObject() ничего по id не найдет,
        // то будет словлен Exception, который будет перехвачен.
        try {
            userService.getUserById(user.getId());
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException(String.format("Пользователя с таким id: %d нет", user.getId()));
        }
    }

    public void checkIfUserExistById(Long userId) {
        if (userId == null) {
            throw new ValidationException("user_id и/или friend_id не передан в PathVariable");
        }

        // Если метод jdbcTemplate.queryForObject() ничего по id не найдет,
        // то будет выброшен Exception, который будет перехвачен.
        try {
            userService.getUserById(userId);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException(String.format("Пользователя с таким id: %d нет", userId));
        }
    }

    public void checkIfFilmExists(Film film) {
        if (film.getId() == 0) {
            throw new ValidationException("film_id не передан на сервер");
        }

        try {
            filmService.getFilmById(film.getId());
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException(String.format("Фильма с таким id: %d нет", film.getId()));
        }
    }

    public void checkIfFilmExistById(Long filmId) {
        try {
            filmService.getFilmById(filmId);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException(String.format("Фильма с таким id: %d нет", filmId));
        }
    }

    @Override
    public void checkIfMpaExistById(Integer mpaId) {
        try {
            filmService.getMpaById(mpaId);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException(String.format("MPA с таким id: %d нет", mpaId));
        }
    }

    public void checkIfGenreExistById(Integer mpaid) {
        try {
            filmService.getMpaById(mpaid);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException(String.format("Жанра с таким id: %d нет", mpaid));
        }
    }

    // Валидация имени. Если имя пустое, то login становится именем.
    public User checkForName(User user) {
        boolean isNameCorrect = (user.getName() != null) && (!user.getName().isEmpty());

        if (!isNameCorrect) {
            user.setName(user.getLogin());
        }

        return user;
    }
}
