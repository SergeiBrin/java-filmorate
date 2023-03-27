package ru.yandex.practicum.filmorate.validation.annotation.mpa;

import ru.yandex.practicum.filmorate.model.Mpa;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FilmMpaValidator implements ConstraintValidator<FilmIdMpaConstraint, Mpa> {
    @Override
    public boolean isValid(Mpa mpa, ConstraintValidatorContext constraintValidatorContext) {
        return (mpa.getId() > 0) && (mpa.getId() < 6);
    }
}
