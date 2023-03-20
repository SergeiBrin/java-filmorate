package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.film.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Primary
@Slf4j
public class FilmDbService implements FilmService {
    private final FilmStorageDao filmStorage;
    private final MpaDao mpaDao;
    private final FilmGenreDao filmGenreDao;
    private final GenresDao genresDao;
    private final LikesDao likesDao;

    public FilmDbService(FilmStorageDao filmStorage,
                         MpaDao mpaDao,
                         FilmGenreDao filmGenreDao,
                         GenresDao genresDao,
                         LikesDao likesDao) {

        this.filmStorage = filmStorage;
        this.mpaDao = mpaDao;
        this.filmGenreDao = filmGenreDao;
        this.genresDao = genresDao;
        this.likesDao = likesDao;
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(Long filmId)  {
        return filmStorage.getFilmById(filmId);
    }

    public List<Film> getMostPopularFilm(Long count) {
        return filmStorage.getPopularFilms()
                .stream()
                .limit(count)
                .collect(Collectors.toList());
    }

    public List<Genre> getAllGenres() {
        return genresDao.getAllGenres();
    }

    public Genre getGenreById(Integer genreId) {
        return genresDao.getGenreById(genreId);
    }

    public Film createFilm(Film film) {
        // В методе post так же происходит отсылка в popular_films
        Film createfilm = filmStorage.createFilm(film);

        Set<Genre> genres = film.getGenres();
        if (genres != null) {
            for (Genre genre : genres) {
                filmGenreDao.addFilmGenre(genre.getId(), createfilm.getId());
            }
        }

        return filmStorage.getFilmById(createfilm.getId());
    }

    public Film updateFilm(Film film) {
        // Метод обнуляет жанры фильма.
        // Если жанров в запросе нет, то сразу ок.
        // Если они есть, то они обновятся в цикле.
        filmGenreDao.deleteFilmGenres(film.getId());

        Set<Genre> genres = film.getGenres();
        if (genres != null) {
            for (Genre genre : genres) {
                filmGenreDao.addFilmGenre(genre.getId(), film.getId());
            }
        }

        return filmStorage.updateFilm(film);
    }

    public Film likeTheFilm(Long filmId, Long userId) {
        likesDao.addLikeToFilm(filmId, userId);
        return filmStorage.getFilmById(filmId);
    }

    public Film deleteLikeForFilm(Long filmId, Long userId) {
        likesDao.deleteLikeForFilm(filmId, userId);
        return filmStorage.getFilmById(filmId);

    }

    public List<Mpa> getAllMpa() {
        return mpaDao.getAllMpa();
    }

    public Mpa getMpaById(Integer mpaId) {
        return mpaDao.getMpaById(mpaId);
    }
}
