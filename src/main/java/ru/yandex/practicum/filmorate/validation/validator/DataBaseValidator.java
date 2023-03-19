package ru.yandex.practicum.filmorate.validation.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.GenreNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.MpaNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
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

    public void checkIfUserExistsById(User user) {
        if (user.getId() == 0) {
            throw new ValidationException("user_id не передан на сервер");
        }

        // Если метод jdbcTemplate.queryForObject() ничего по id не найдет,
        // то будет словлен Exception, который будет перехвачен.
        try {
            userService.getUserById(user.getId());
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotFoundException(String.format("Пользователя с таким id: %d нет", user.getId()));
        }
    }

    public void checkUserByPathVariableId(Long userId) {
        if (userId == null) {
            throw new ValidationException("user_id и/или friend_id не передан в PathVariable");
        }

        // Если метод jdbcTemplate.queryForObject() ничего по id не найдет,
        // то будет выброшен Exception, который будет перехвачен.
        try {
            userService.getUserById(userId);
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotFoundException(String.format("Пользователя с таким id: %d нет", userId));
        }
    }

    public void checkIfFilmExistsById(Film film) {
        if (film.getId() == 0) {
            throw new ValidationException("film_id не передан на сервер");
        }

        try {
            filmService.getFilmById(film.getId());
        } catch (EmptyResultDataAccessException e) {
            throw new FilmNotFoundException(String.format("Фильма с таким id: %d нет", film.getId()));
        }
    }

    public void checkFilmByPathVariableId(Long filmId) {
        if (filmId == null) {
            throw new ValidationException("film_id не передан в PathVariable");
        }

        try {
            filmService.getFilmById(filmId);
        } catch (EmptyResultDataAccessException e) {
            throw new FilmNotFoundException(String.format("Фильма с таким id: %d нет", filmId));
        }
    }

    @Override
    public void checkMpaByPathVariableId(Integer mpaId) {
        if (mpaId == null) {
            throw new ValidationException("mpa_id не передан в PathVariable");
        }

        try {
            filmService.getMpaById(mpaId);
        } catch (EmptyResultDataAccessException e) {
            throw new MpaNotFoundException(String.format("MPA с таким id: %d нет", mpaId));
        }
    }

    public void checkGenreByPathVariableId(Integer mpaid) {
        if (mpaid == null) {
            throw new ValidationException("genre_id не передан в PathVariable");
        }

        try {
            filmService.getMpaById(mpaid);
        } catch (EmptyResultDataAccessException e) {
            throw new GenreNotFoundException(String.format("Жанра с таким id: %d нет", mpaid));
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
