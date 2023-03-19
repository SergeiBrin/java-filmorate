package ru.yandex.practicum.filmorate.validation.validator;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

@Service
public interface Validator {
    void checkIfUserExistsById(User user);
    void checkUserByPathVariableId(Long userId);
    void checkIfFilmExistsById(Film film);
    void checkFilmByPathVariableId(Long filmId);
    User checkForName(User user);
    void checkMpaByPathVariableId(Integer mpaId);
    void checkGenreByPathVariableId(Integer genreId);

}
