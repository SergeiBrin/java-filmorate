package ru.yandex.practicum.filmorate.validation.validator;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

@Service
public interface Validator {
    void checkIfUserExists(User user);
    void checkIfUserExistById(Long userId);
    void checkIfFilmExists(Film film);
    void checkIfFilmExistById(Long filmId);
    User checkForName(User user);
    void checkIfMpaExistById(Integer mpaId);
    void checkIfGenreExistById(Integer genreId);

}
