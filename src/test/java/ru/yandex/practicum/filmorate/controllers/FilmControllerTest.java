package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FilmControllerTest {
    private Validator validator;
    private Set<ConstraintViolation<Film>> violations;
    private Film film;
    private final String description = "В футуристическом парке развлечений «Мир Дикого Запада» специально сконструированные " +
            "андроиды выполняют любые прихоти посетителей, чтобы те чувствовали безнаказанность и полную свободу " +
            "действий. Если робота убили — не беда, техники его починят, сотрут память и снова поставят в строй, " +
            "навстречу новому дню и новым людским прихотям. Но оказывается, что далеко не все роботы теряют " +
            "воспоминания.";

    @BeforeEach
    public void create() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        film = new Film(0,
                "F",
                description.substring(0, 200),
                LocalDate.of(1895, 12, 28),
                1);
    }

    // Проверка на граничные c валидацией значения.
    @Test
    public void filmMustBeValidatedWithTheCorrectParameters() {
        violations = validator.validate(film);

        assertTrue(violations.isEmpty());
    }

    @Test
    public void filmMustNotPassValidationWithAnEmptyName() {
        film.setName("");
        violations = validator.validate(film);

        assertEquals(1, violations.size());
    }

    @Test
    public void filmMustNotPassValidationWith201CharacterDescription() {
        film.setDescription(description.substring(0, 201));
        violations = validator.validate(film);

        assertEquals(1, violations.size());
    }

    @Test
    public void filmMustNotBeValidatedWithReleaseDateBefore12December1895() {
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        violations = validator.validate(film);

        assertEquals(1, violations.size());
    }

    @Test
    public void filmShouldNotPassValidationWithNegativeDurationOrZero() {
        film.setDuration(-1);
        violations = validator.validate(film);

        assertEquals(1, violations.size());

        film.setDuration(0);
        violations = validator.validate(film);

        assertEquals(1, violations.size());
    }
}
