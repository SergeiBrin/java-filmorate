package ru.yandex.practicum.filmorate.validation.annotation.mpa;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FilmMpaValidator.class)
public @interface FilmIdMpaConstraint {
    String message() default "mpa_id must be 1 to 5";

    Class <?>[] groups() default {};

    Class <? extends Payload>[] payload() default {};
}
