package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.dao.film.*;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.dao.impl.film.FilmDbStorageDao;
import ru.yandex.practicum.filmorate.dao.impl.user.UserDbStorageDao;
import ru.yandex.practicum.filmorate.validation.validator.DataBaseValidator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class FilmorateApplicationTests {
	private final DataBaseValidator validator;
	private final UserDbStorageDao userStorage;
	private final FriendshipDao friendshipDao;
	private final FilmDbStorageDao filmStorage;
	private final GenresDao genresDao;
	private final FilmGenreDao filmGenreDao;
	private final MpaDao mpaDao;
	private final LikesDao likesDao;
	private User user;
	private Film film;

	@BeforeEach
	public void create() {
        user = new User(
				0,
				"Name",
				"Login",
				LocalDate.of(1987, 2, 10),
				"mail@gmail.com");

		film = new Film(
				0,
				"Film",
				"Description",
				LocalDate.of(2000, 1, 1),
				100,
				new Mpa(1, "G"),
				Set.of(new Genre(1, "Комедия"), new Genre(2, "Драма")));
	}

	@Test
	public void exceptionShouldBeThrownIfThereIsNoUserWithThisId() {
		final EntityNotFoundException exception = assertThrows(
				EntityNotFoundException.class,
				() -> validator.checkIfUserExistById(1L));

		assertEquals("Пользователя с таким id: 1 нет", exception.getMessage());
	}

	@Test
	public void userMustBeAddedToDatabase() {
		User postUser = userStorage.createUser(user);
		user.setId(1);

		assertEquals(user, postUser);
	}

	@Test
	public void userMustBeUpdateToDatabase() {
		userStorage.createUser(user);

		user.setId(1);
		user.setLogin("New Login");
		user.setEmail("newmail@gmail.com");

		User updateUser = userStorage.updateUser(user);

		assertEquals(user, updateUser);
	}

	@Test
	public void shouldReturnListOfUsers() {
		User userOne = userStorage.createUser(user);
		User userTwo = userStorage.createUser(user);
		User userThree = userStorage.createUser(user);

		List<User> postUsers = List.of(userOne, userTwo, userThree);
		List<User> getAllUsers = userStorage.getAllUsers();

		assertEquals(postUsers, getAllUsers);
	}

	@Test
	public void userMustReturnById() {
		User postUser = userStorage.createUser(user);
		user.setId(1);

		User getUserById = userStorage.getUserById(postUser.getId());

		assertEquals(user, getUserById);
	}

	@Test
	public void userMustNotHaveFriends() {
		userStorage.createUser(user);
		List<User> friends = userStorage.getUserFriendsList(1L);

		assertTrue(friends.isEmpty());
	}

    @Test
	public void userMustHaveTwoFriends() {
		User userOne = userStorage.createUser(user);
		User userTwo = userStorage.createUser(user);
		User userThree = userStorage.createUser(user);

		// При добавлении юзеру друга (true), самому другу присваивается
		// как бы предложение дружбы от юзера.
		// Без подтверждения дружбы другом, статус дружбы у друга будет false.
		friendshipDao.addFriendToUser(userOne.getId(), userTwo.getId());
		friendshipDao.addFriendToUser(userOne.getId(), userThree.getId());

		User getFriendOne = userStorage.getUserById(userTwo.getId());
		User getFriendTwo = userStorage.getUserById(userThree.getId());

		List<User> friendsToUser = userStorage.getUserFriendsList(userOne.getId());

		assertEquals(List.of(getFriendOne, getFriendTwo), friendsToUser);
	}

	@Test
	public void twoUsersMustNotHaveCommonFriends() {
         User userOne = userStorage.createUser(user);
		 User userTwo = userStorage.createUser(user);
		 User userThree = userStorage.createUser(user);

		 friendshipDao.addFriendToUser(userOne.getId(), userTwo.getId());
		 friendshipDao.addFriendToUser(userTwo.getId(), userThree.getId());

		 List<User> friends = userStorage.getListOfCommonFriends(userOne.getId(), userTwo.getId());

		 assertTrue(friends.isEmpty());
	}

	@Test
	public void twoUsersMustNotHaveCommonFriendIfStatusIsFalse() {
		User userOne = userStorage.createUser(user);
		User userTwo = userStorage.createUser(user);
		User userThree = userStorage.createUser(user);

		friendshipDao.addFriendToUser(userOne.getId(), userThree.getId());
		friendshipDao.addFriendToUser(userTwo.getId(), userThree.getId());

		friendshipDao.updateFriendToUser(userTwo.getId(), userThree.getId(), false);

		List<User> commonFriends = userStorage.getListOfCommonFriends(userOne.getId(), userTwo.getId());

		assertTrue(commonFriends.isEmpty());
	}

	@Test
	public void twoUsersMustHaveCommonFriend() {
		User userOne = userStorage.createUser(user);
		User userTwo = userStorage.createUser(user);
		User userThree = userStorage.createUser(user);

		friendshipDao.addFriendToUser(userOne.getId(), userThree.getId());
		friendshipDao.addFriendToUser(userTwo.getId(), userThree.getId());

		List<User> actual = userStorage.getListOfCommonFriends(userOne.getId(), userTwo.getId());
		List<User> expected = List.of(userStorage.getUserById(userThree.getId()));

		assertEquals(expected, actual);
	}

	@Test
	public void userMustNotHaveFriendIfDeleted() {
		User userOne = userStorage.createUser(user);
		User userTwo = userStorage.createUser(user);

		friendshipDao.addFriendToUser(userOne.getId(), userTwo.getId());
		friendshipDao.deleteFriendFromUser(userOne.getId(), userTwo.getId());

		List<User> friends = userStorage.getUserFriendsList(userOne.getId());

		assertTrue(friends.isEmpty());
	}

	@Test
	public void exceptionShouldBeThrownIfThereIsNoFilmWithThisId() {
		final EntityNotFoundException exception = assertThrows(
				EntityNotFoundException.class,
				() -> validator.checkIfFilmExistById(1L));

		assertEquals("Фильма с таким id: 1 нет", exception.getMessage());
	}

	@Test
	public void filmMustBeAddedToDatabase() {
		Film postFilm = filmStorage.createFilm(film);
		filmGenreDao.addFilmGenre(1, postFilm.getId());
		filmGenreDao.addFilmGenre(2, postFilm.getId());

		film.setId(1);
		Film getPostFilm = filmStorage.getFilmById(postFilm.getId());

		assertEquals(film, getPostFilm);
	}
	@Test
	public void filmMustBeUpdateToDatabase() {
		Film postFilm = filmStorage.createFilm(film);
		filmGenreDao.addFilmGenre(1, postFilm.getId());
		filmGenreDao.addFilmGenre(2, postFilm.getId());

		film.setId(1);
		film.setMpa(new Mpa(2, "PG"));
		film.addGenre(Set.of(new Genre(1, "Комедия"), new Genre(2, "Драма"), new Genre(3, "Мультфильм")));

		filmStorage.updateFilm(film);
		filmGenreDao.addFilmGenre(3, postFilm.getId());

		Film updateFilm = filmStorage.getFilmById(postFilm.getId());

		assertEquals(film, updateFilm);
	}

	@Test
	public void shouldReturnListOfFilms() {
		Film filmOne = filmStorage.createFilm(film);
		Film filmTwo = filmStorage.createFilm(film);

		filmGenreDao.addFilmGenre(1, filmOne.getId());
		filmGenreDao.addFilmGenre(2, filmOne.getId());
		filmGenreDao.addFilmGenre(1, filmTwo.getId());
		filmGenreDao.addFilmGenre(2, filmTwo.getId());

		filmOne.addGenre(Set.of(new Genre(1, "Комедия"), new Genre(2, "Драма")));
		filmTwo.addGenre(Set.of(new Genre(1, "Комедия"), new Genre(2, "Драма")));

		List<Film> postFilms = List.of(filmOne, filmTwo);
		List<Film> getAllFilms = filmStorage.getAllFilms();

		assertEquals(postFilms, getAllFilms);
	}

	@Test
	public void filmMustReturnById() {
		Film filmOne = filmStorage.createFilm(film);
		filmGenreDao.addFilmGenre(1, filmOne.getId());
		filmGenreDao.addFilmGenre(2, filmOne.getId());

		film.setId(1);

		Film getFilmById = filmStorage.getFilmById(filmOne.getId());

		assertEquals(film, getFilmById);
	}

	@Test
	public void listOfPopularFilmsShouldReturn() {
		Film filmOne = filmStorage.createFilm(film);
		Film filmTwo = filmStorage.createFilm(film);

		filmGenreDao.addFilmGenre(1, filmOne.getId());
		filmGenreDao.addFilmGenre(2, filmOne.getId());
		filmGenreDao.addFilmGenre(1, filmTwo.getId());
		filmGenreDao.addFilmGenre(2, filmTwo.getId());

		filmOne.addGenre(Set.of(new Genre(1, "Комедия"), new Genre(2, "Драма")));
		filmTwo.addGenre(Set.of(new Genre(1, "Комедия"), new Genre(2, "Драма")));

		// Добавляю наоборот, так как в методе сортировка по лайкам DESC.
		List<Film> postFilms = List.of(filmTwo, filmOne);
		List<Film> getPopularFilms = new ArrayList<>(filmStorage.getPopularFilms());

		assertEquals(postFilms, getPopularFilms);
	}

	@Test
	public void numberOfLikesForFilmNeedsToBeUpdated() {
		Film filmOne = filmStorage.createFilm(film);
		User userOne = userStorage.createUser(user);

		likesDao.addLikeToFilm(filmOne.getId(), userOne.getId());

		Film updateFilm = filmStorage.getFilmById(filmOne.getId());

		assertEquals(1, updateFilm.getLikes().size());
	}

	@Test
	public void filmShouldGetLike() {
		Film filmOne = filmStorage.createFilm(film);
		User userOne = userStorage.createUser(user);

		likesDao.addLikeToFilm(filmOne.getId(), userOne.getId());

		List<Likes> likes = likesDao.getLikesForFilm(filmOne.getId());
        Likes expectedLikes = new Likes(filmOne.getId(), userOne.getId());

		assertEquals(expectedLikes, likes.get(0));
	}

	@Test
	public void filmShouldHaveLike() {
		Film filmOne = filmStorage.createFilm(film);
		User userOne = userStorage.createUser(user);

		likesDao.addLikeToFilm(filmOne.getId(), userOne.getId());

		List<Likes> likes = likesDao.getLikesForFilm(filmOne.getId());
		Likes expectedLikes = new Likes(filmOne.getId(), userOne.getId());

		assertEquals(expectedLikes, likes.get(0));

		likesDao.deleteLikeForFilm(filmOne.getId(), userOne.getId());
		List<Likes> deleteLikes = likesDao.getLikesForFilm(filmOne.getId());

		assertTrue(deleteLikes.isEmpty());
	}

	@Test
	public void filmShouldBeAddedToThePopularList() {
		Film filmOne = filmStorage.createFilm(film);
		Set<Film> popularFilms = filmStorage.getPopularFilms();

		assertEquals(1, popularFilms.size());
	}

	@Test
	public void shouldReturnMpaList() {
		List<Mpa> expected = List.of(new Mpa(1, "G"),
		                             new Mpa(2, "PG"),
		                             new Mpa(3, "PG-13"),
		                             new Mpa(4, "R"),
		                             new Mpa(5, "NC-17"));

		List<Mpa> allMpa = mpaDao.getAllMpa();

		assertEquals(expected, allMpa);
	}

	@Test
	public void shouldReturnMpaById() {
		Mpa expectedMpa = new Mpa(2, "PG");
		Mpa actualMpa = mpaDao.getMpaById(2);

		assertEquals(expectedMpa, actualMpa);
	}

	@Test
	public void shouldReturnListOfAllGenres() {
		List<Genre> expectedGenres = List.of(new Genre(1, "Комедия"),
		                                     new Genre(2, "Драма"),
		                                     new Genre(3, "Мультфильм"),
		                                     new Genre(4, "Триллер"),
		                                     new Genre(5, "Документальный"),
		                                     new Genre(6, "Боевик"));

		List<Genre> allGenres = genresDao.getAllGenres();

		assertEquals(expectedGenres, allGenres);
	}

	@Test
	public void shouldReturnGenreById() {
		Genre expectedGenre = new Genre(3, "Мультфильм");
		Genre actualGenre = genresDao.getGenreById(3);

		assertEquals(expectedGenre, actualGenre);
	}

	@Test
	public void filmGenreShouldComeBack() {
		Film filmOne = filmStorage.createFilm(film);
		Set<Genre> expectedGenres = Set.of(new Genre(1, "Комедия"), new Genre(2, "Драма"));

		filmGenreDao.addFilmGenre(1, filmOne.getId());
		filmGenreDao.addFilmGenre(2, filmOne.getId());

		Set<Genre> actualGenres = genresDao.getGenresForFilm(filmOne.getId());

		assertEquals(expectedGenres, actualGenres);
	}

	@Test
	public void filmGenresShouldBeDeleted() {
		Film filmOne = filmStorage.createFilm(film);
		filmGenreDao.addFilmGenre(1, filmOne.getId());
		filmGenreDao.addFilmGenre(2, filmOne.getId());

		filmGenreDao.deleteFilmGenres(filmOne.getId());
		Set<Genre> actualGenres = genresDao.getGenresForFilm(filmOne.getId());

		assertTrue(actualGenres.isEmpty());
	}

	@Test
	void contextLoads() {
	}
}
