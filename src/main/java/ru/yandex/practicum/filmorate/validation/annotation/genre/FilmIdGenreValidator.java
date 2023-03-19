package ru.yandex.practicum.filmorate.validation.annotation.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Set;

public class FilmIdGenreValidator implements ConstraintValidator<FilmIdGenreConstraint, Set<Genre>> {
    @Override
    public boolean isValid(Set<Genre> genres, ConstraintValidatorContext constraintValidatorContext) {
        if (genres == null) {
            return true;
        }

        for (Genre genre : genres) {
            if (genre.getId() < 1 || genre.getId() > 6) {
                return false;
            }
        }
        return true;
    }
}
