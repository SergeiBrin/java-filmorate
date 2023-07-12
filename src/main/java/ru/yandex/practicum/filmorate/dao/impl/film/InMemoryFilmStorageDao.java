package ru.yandex.practicum.filmorate.dao.impl.film;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.film.FilmStorageDao;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
@Slf4j
@Getter
public class InMemoryFilmStorageDao implements FilmStorageDao {
    private int id;
    private final Map<Long, Film> films = new HashMap<>();
    private final Set<Film> popularFilms = new TreeSet<>(createComparator());

    // Сортировка наоборот, так как чем больше лайков, тем выше место в множестве.
    private Comparator<Film> createComparator() {
        return (o1, o2) -> {
            // Это условие необходимо, чтобы в множество добавлялись разные фильмы
            // c одинаковым количеством лайков.
            if (o2.getLikes().size() - o1.getLikes().size() == 0) {
                if (o2.getId() != o1.getId()) {
                    return 1;
                }
            }
            
            return o2.getLikes().size() - o1.getLikes().size();
        };
    }

    @Override
    public List<Film> getAllFilms() {
        log.info("Список всех фильмов {} отправлен клиенту", films.values());
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilmById(Long filmId) {
        log.info("Фильм с id {} отправлены клиенту", filmId);
        return films.get(filmId);
    }

    @Override
    public Film createFilm(Film film) {
        film.setId(++id);

        films.put(film.getId(), film);
        log.info("Фильм {} добавлен в общий список", film);

        if (popularFilms.add(film)) {
            log.info("Фильм {} добавлен в популярный список", film);
        }

        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        films.put(film.getId(), film);
        log.info("Фильм {} обновлён в общем списке", film);

        updatePopularFilms(film);

        return film;
    }

    private void updatePopularFilms(Film film) {
        // Сначала удаляю фильм из множества, а потом добавляю его снова - для сортировки.
        if (!popularFilms.isEmpty()) {
            popularFilms.removeIf(checkFilm -> checkFilm.getId() == film.getId());
        }

        if (popularFilms.add(film)) {
            log.info("Фильм {} обновлён в популярном списке", film);
        }
    }
}
