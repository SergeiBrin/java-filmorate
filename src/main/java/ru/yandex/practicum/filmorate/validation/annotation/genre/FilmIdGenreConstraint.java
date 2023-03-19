package ru.yandex.practicum.filmorate.validation.annotation.genre;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FilmIdGenreValidator.class)
public @interface FilmIdGenreConstraint {
    String message() default "genre_id must be 1 to 6";

    Class <?>[] groups() default {};

    Class <? extends Payload>[] payload() default {};
}
