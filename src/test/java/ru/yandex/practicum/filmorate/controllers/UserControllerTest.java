package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    private Validator validator;
    private Set<ConstraintViolation<User>> violations;
    private User user;

    @BeforeEach
    public void create() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        user = new User(0,
                "name",
                "login",
                LocalDate.now(),
                "user@gmail.com");
    }

    @Test
    public void userWithCorrectParametersShouldBeValidated() {
        violations = validator.validate(user);

        assertTrue(violations.isEmpty());
    }

    // Проверка на граничные с валидацией значения
    @Test
    public void userMustNotBeValidatedWithAnEmptyEmailOrNotContainingSignAt() {
        user.setEmail(null);
        violations = validator.validate(user);

        assertEquals(1, violations.size());

        user.setEmail("usergmail.com");
        violations = validator.validate(user);

        assertEquals(1, violations.size());
    }

    @Test
    public void userMustNotValidateWithAnEmptyOrContainingSpacesLogin() {
        user.setLogin("");
        violations = validator.validate(user);

        // Тут 2, потому что отрабатывает сразу 2 аннотации.
        assertEquals(2, violations.size());

        user.setLogin("login name");
        violations = validator.validate(user);

        assertEquals(1, violations.size());
    }

    @Test
    public void userMustBeValidatedIfUsernameIsEmpty() {
        user.setName("");
        violations = validator.validate(user);

        assertTrue(violations.isEmpty());
    }

    @Test
    public void userShouldNotBeValidatedIfHisDateOfBirthIsInTheFuture() {
        user.setBirthday(LocalDate.now().plusDays(1));
        violations = validator.validate(user);

        assertEquals(1, violations.size());
    }
}