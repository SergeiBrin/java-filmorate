package ru.yandex.practicum.filmorate.validation.annotation.date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class ReleaseDateValidator implements ConstraintValidator<FilmReleaseDateConstraint, LocalDate> {
    @Override
    public boolean isValid(LocalDate releaseDate, ConstraintValidatorContext constraintValidatorContext) {
        return releaseDate.equals(LocalDate.of(1895, 12, 28)) ||
                releaseDate.isAfter(LocalDate.of(1895, 12, 28)) ;
    }
}
