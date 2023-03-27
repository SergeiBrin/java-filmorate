package ru.yandex.practicum.filmorate.dao.film;

public interface FilmGenreDao {
    Boolean addFilmGenre(Integer genreId, Long filmId);
    Boolean deleteFilmGenres(Long filmId);
}
