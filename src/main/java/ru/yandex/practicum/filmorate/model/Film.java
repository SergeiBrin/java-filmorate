package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.validation.annotation.date.FilmReleaseDateConstraint;
import ru.yandex.practicum.filmorate.validation.annotation.genre.FilmIdGenreConstraint;
import ru.yandex.practicum.filmorate.validation.annotation.mpa.FilmIdMpaConstraint;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@Builder
public class Film {
    private long id;
    @NotBlank
    private String name;
    @Size(max=200)
    private String description;
    @FilmReleaseDateConstraint
    private LocalDate releaseDate;
    @Positive
    private long duration;
    @NotNull // Под вопросом.
    @FilmIdMpaConstraint
    private Mpa mpa;
    @FilmIdGenreConstraint
    private Set<Genre> genres;
    @NotNull
    private final Set<Long> likes = new HashSet<>();

    public boolean addLike(Long userId) {
        return likes.add(userId);
    }

    public boolean deleteLike(Long userId) {
        return likes.remove(userId);
    }
}
