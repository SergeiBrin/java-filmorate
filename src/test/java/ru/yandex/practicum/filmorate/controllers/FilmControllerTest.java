package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    private FilmController filmController;
    private Film film1;
    private Film film2;
    private String description;

    @BeforeEach
    public void createFilms() {
        filmController = new FilmController();

        film1 = new Film(0,
                "Film1 name",
                "Film1 description",
                LocalDate.of(1895, 12, 28),
                125);

        film2 = new Film(0,
                "Film2 name",
                "Film2 description",
                LocalDate.of(2010, 1, 10),
                90);

        description = "В футуристическом парке развлечений «Мир Дикого Запада» специально сконструированные " +
                "андроиды выполняют любые прихоти посетителей, чтобы те чувствовали безнаказанность и полную свободу " +
                "действий. Если робота убили — не беда, техники его починят, сотрут память и снова поставят в строй, " +
                "навстречу новому дню и новым людским прихотям. Но оказывается, что далеко не все роботы теряют " +
                "воспоминания.";
    }

    // GET проверки
    @Test
    public void getMethodShouldReturnAnEmptyList() {
        final List<Film> getFilms = filmController.getAllFilms();

        assertTrue(getFilms.isEmpty());
    }

    @Test
    public void getMethodShouldReturnListWithObjects() {
        filmController.postFilm(film1);
        filmController.postFilm(film2);

        film1 = new Film(1,
                "Film1 name",
                "Film1 description",
                LocalDate.of(1895, 12, 28),
                125);

        film2 = new Film(2,
                "Film2 name",
                "Film2 description",
                LocalDate.of(2010, 1, 10),
                90);

        final List<Film> postFilms = List.of(film1, film2);
        final List<Film> getFilms = filmController.getAllFilms();

        assertEquals(postFilms, getFilms);
    }

    // POST проверки
    @Test
    public void emptyRequestWillNotBeAddedViaPostMethod() {
        final ValidationException exception = assertThrows(ValidationException.class,
                () -> filmController.postFilm(null));

        final List<Film> getFilms = filmController.getAllFilms();

        assertEquals("POST: [Запрос пустой]", exception.getMessage());
        assertTrue(getFilms.isEmpty());
    }

    @Test
    public void postMethodWithCorrectParametersShouldWork() {
        filmController.postFilm(film1);

        final List<Film> getFilms = filmController.getAllFilms();

        assertEquals(film1, getFilms.get(0));
    }

    @Test
    public void filmWithAnEmptyNameWillNotBeAddedViaPostMethod() {
        film1.setName("");

        final ValidationException exception = assertThrows(ValidationException.class,
                () -> filmController.postFilm(film1));

        final List<Film> getFilms = filmController.getAllFilms();

        assertEquals("POST: [Название фильма пустое]", exception.getMessage());
        assertTrue(getFilms.isEmpty());
    }

    @Test
    public void filmWithDescriptionOf200CharactersWillBeAddedViaPostMethod() {
        final String descriptionLength200 = description.substring(0, 200);
        film1.setDescription(descriptionLength200);

        filmController.postFilm(film1);
        final List<Film> getFilms = filmController.getAllFilms();

        assertEquals(film1, getFilms.get(0));
    }

    @Test
    public void filmWithDescriptionOf201CharactersWillBeAddedViaPostMethod() {
        final String descriptionLength201 = description.substring(0, 201);
        film1.setDescription(descriptionLength201);

        final ValidationException exception = assertThrows(ValidationException.class,
                () -> filmController.postFilm(film1));
        final List<Film> getFilms = filmController.getAllFilms();

        assertEquals("POST: [Описание фильма больше 200 символов]", exception.getMessage());
        assertTrue(getFilms.isEmpty());
    }

    @Test
    public void filmBefore02281985ShouldNotBeAddedViaPostMethod() {
        final LocalDate wrongDate = LocalDate.of(1895, 2, 27);
        film1.setReleaseDate(wrongDate);

        final ValidationException exception = assertThrows(ValidationException.class,
                () -> filmController.postFilm(film1));
        final List<Film> getFilms = filmController.getAllFilms();

        assertEquals("POST: [Дата фильма раньше 28.12.1895]", exception.getMessage());
        assertTrue(getFilms.isEmpty());
    }

    @Test
    public void filmLater02281985MustBeAddedViaPostMethod() {
        filmController.postFilm(film1);
        final List<Film> getFilms = filmController.getAllFilms();

        assertEquals(film1, getFilms.get(0));
    }

    @Test
    public void filmWithNegativeDurationShouldNotBeAddedViaPostMethod() {
        film1.setDuration(-1);

        final ValidationException exception = assertThrows(ValidationException.class,
                () -> filmController.postFilm(film1));
        final List<Film> getFilms = filmController.getAllFilms();

        assertEquals("POST: [Продолжительность фильма отрицательная]", exception.getMessage());
        assertTrue(getFilms.isEmpty());
    }

    @Test
    public void filmWithPositiveDurationMustBeBeAddedViaPostMethod() {
        film1.setDuration(0);
        film2.setDuration(1);

        filmController.postFilm(film1);
        filmController.postFilm(film2);

        final List<Film> postFilms = List.of(film1, film2);
        final List<Film> getFilms = filmController.getAllFilms();

        assertEquals(postFilms, getFilms);
    }

    // PUT проверки
    @Test
    public void emptyRequestWillNotBeAddedViaPutMethod() {
        filmController.postFilm(film1);

        final ValidationException exception = assertThrows(ValidationException.class,
                () -> filmController.putFilm(null));

        final List<Film> getFilms = filmController.getAllFilms();

        assertEquals("PUT: [Запрос пустой]", exception.getMessage());
        assertEquals(film1, getFilms.get(0));
    }

    @Test
    public void putMethodWithCorrectParametersShouldWork() {
        filmController.postFilm(film1);

        film2.setId(1);
        filmController.putFilm(film2);

        final List<Film> getFilms = filmController.getAllFilms();

        assertEquals(film2, getFilms.get(0));
    }

    @Test
    public void filmWithAnEmptyNameWillNotBeAddedViaPutMethod() {
        filmController.postFilm(film1);

        film2.setId(1);
        film2.setName("");

        final ValidationException exception = assertThrows(ValidationException.class,
                () -> filmController.putFilm(film2));

        final List<Film> getFilms = filmController.getAllFilms();

        assertEquals("PUT: [Название фильма пустое]", exception.getMessage());
        assertEquals(film1, getFilms.get(0));
    }

    @Test
    public void filmWithDescriptionOf200CharactersWillBeAddedViaPutMethod() {
        filmController.postFilm(film1);

        final String descriptionLength200 = description.substring(0, 200);
        film2.setId(1);
        film2.setDescription(descriptionLength200);

        filmController.putFilm(film2);
        final List<Film> getFilms = filmController.getAllFilms();

        assertEquals(film2, getFilms.get(0));
    }

    @Test
    public void filmWithDescriptionOf201CharactersWillBeAddedViaPutMethod() {
        filmController.postFilm(film1);

        final String descriptionLength201 = description.substring(0, 201);
        film2.setId(1);
        film2.setDescription(descriptionLength201);

        final ValidationException exception = assertThrows(ValidationException.class,
                () -> filmController.putFilm(film2));
        final List<Film> getFilms = filmController.getAllFilms();

        assertEquals("PUT: [Описание фильма больше 200 символов]", exception.getMessage());
        assertEquals(film1, getFilms.get(0));
    }

    @Test
    public void filmBefore02281985ShouldNotBeAddedViaPutMethod() {
        filmController.postFilm(film1);

        final LocalDate wrongDate = LocalDate.of(1895, 2, 27);
        film2.setId(1);
        film2.setReleaseDate(wrongDate);

        final ValidationException exception = assertThrows(ValidationException.class,
                () -> filmController.putFilm(film2));
        final List<Film> getFilms = filmController.getAllFilms();

        assertEquals("PUT: [Дата фильма раньше 28.12.1895]", exception.getMessage());
        assertEquals(film1, getFilms.get(0));
    }

    @Test
    public void filmLater02281985MustBeAddedViaPutMethod() {
        filmController.postFilm(film1);

        film2.setId(1);
        film2.setReleaseDate(LocalDate.of(1895, 12, 29));
        filmController.putFilm(film2);

        final List<Film> getFilms = filmController.getAllFilms();

        assertEquals(film2, getFilms.get(0));
    }

    @Test
    public void filmWithNegativeDurationShouldNotBeAddedViaPutMethod() {
        filmController.postFilm(film1);

        film2.setId(1);
        film2.setDuration(-1);

        final ValidationException exception = assertThrows(ValidationException.class,
                () -> filmController.putFilm(film2));
        final List<Film> getFilms = filmController.getAllFilms();

        assertEquals("PUT: [Продолжительность фильма отрицательная]", exception.getMessage());
        assertEquals(film1, getFilms.get(0));
    }

    @Test
    public void filmWithPositiveDurationMustBeBeAddedViaPutMethod() {
        filmController.postFilm(film1);

        film2.setId(1);
        film2.setDuration(0);
        filmController.putFilm(film2);

        final List<Film> getFilms = filmController.getAllFilms();

        assertEquals(film2, getFilms.get(0));
    }
}