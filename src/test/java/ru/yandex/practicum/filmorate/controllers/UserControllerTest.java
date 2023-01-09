package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    private UserController userController;
    private User user1;
    private User user2;

    @BeforeEach
    public void createObjects() {
        userController = new UserController();

        user1 = new User(0,
                "user1 name",
                "login1",
                LocalDate.now(),
                "user1@gmail.com");

        user2 = new User(0,
                "user2 name",
                "login2",
                LocalDate.of(1980, 1, 20),
                "user2@gmail.com");
    }


    // GET проверки
    @Test
    public void getMethodShouldReturnAnEmptyList() {
        List<User> getUsers = userController.getAllUsers();

        assertTrue(getUsers.isEmpty());
    }

    @Test
    public void getMethodShouldReturnListWithObjects() {
        userController.postUser(user1);
        userController.postUser(user2);

        user1 = new User(1,
                "user1 name",
                "login1",
                LocalDate.now(),
                "user1@gmail.com");

        user2 = new User(2,
                "user2 name",
                "login2",
                LocalDate.of(1980, 1, 20),
                "user2@gmail.com");

        final List<User> postUsers = List.of(user1, user2);
        final List<User> getUsers = userController.getAllUsers();

        assertEquals(postUsers, getUsers);
    }

    // POST проверки
    @Test
    public void emptyRequestWillNotBeAddedViaPostMethod() {
        final ValidationException exception = assertThrows(ValidationException.class,
                () -> userController.postUser(null));

        final List<User> getUsers = userController.getAllUsers();

        assertEquals("POST: [Запрос пустой]", exception.getMessage());
        assertTrue(getUsers.isEmpty());
    }

    @Test
    public void postMethodWithCorrectParametersShouldWork() {
        userController.postUser(user1);
        final List<User> getUsers = userController.getAllUsers();

        assertEquals(user1, getUsers.get(0));
    }

    @Test
    public void userWithEmptyEmailWillNotBeAddedViaPostMethod() {
        user1.setEmail("");

        final ValidationException exception = assertThrows(ValidationException.class,
                () -> userController.postUser(user1));

        final List<User> getUsers = userController.getAllUsers();

        assertEquals("POST: [email пустой или не содержит символ @]", exception.getMessage());
        assertTrue(getUsers.isEmpty());
    }

    @Test
    public void userWithEmailWithoutAtWillNotBeAddedViaPostMethod() {
        user1.setEmail("user1gmail.com");

        final ValidationException exception = assertThrows(ValidationException.class,
                () -> userController.postUser(user1));

        final List<User> getUsers = userController.getAllUsers();

        assertEquals("POST: [email пустой или не содержит символ @]", exception.getMessage());
        assertTrue(getUsers.isEmpty());
    }

    @Test
    public void userWithAnEmptyLoginWillNotBeAddedViaPostMethod() {
        user1.setLogin("");

        final ValidationException exception = assertThrows(ValidationException.class,
                () -> userController.postUser(user1));

        final List<User> getUsers = userController.getAllUsers();

        assertEquals("POST: [login пустой или содержит пробелы]", exception.getMessage());
        assertTrue(getUsers.isEmpty());
    }

    @Test
    public void userWithSpacesInLoginWillNotBeAddedViaPostMethod() {
        user1.setLogin("login 1");

        final ValidationException exception = assertThrows(ValidationException.class,
                () -> userController.postUser(user1));

        final List<User> getUsers = userController.getAllUsers();

        assertEquals("POST: [login пустой или содержит пробелы]", exception.getMessage());
        assertTrue(getUsers.isEmpty());
    }

    @Test
    public void ifNameIsEmptyNameWillBeLoginViaPostMethod() {
        user1.setName("");
        userController.postUser(user1);

        assertEquals(user1.getLogin(), user1.getName());
    }

    @Test
    public void userWithDateOfBirthInFutureWillNotBeAddedViaPostMethod() {
        user1.setBirthday(LocalDate.now().plusDays(1));

        final ValidationException exception = assertThrows(ValidationException.class,
                () -> userController.postUser(user1));

        final List<User> getUsers = userController.getAllUsers();

        assertEquals("POST: [birthday в будущем]", exception.getMessage());
        assertTrue(getUsers.isEmpty());
    }

    // PUT проверки
    @Test
    public void emptyRequestWillNotBeAddedViaPutMethod() {
        userController.postUser(user1);

        final ValidationException exception = assertThrows(ValidationException.class,
                () -> userController.putUser(null));

        final List<User> getUsers = userController.getAllUsers();

        assertEquals("PUT: [Запрос пустой]", exception.getMessage());
        assertEquals(user1, getUsers.get(0));
    }

    @Test
    public void putMethodWithCorrectParametersShouldWork() {
        userController.postUser(user1);

        user2.setId(1);
        userController.putUser(user2);

        final List<User> getUsers = userController.getAllUsers();

        assertEquals(user2, getUsers.get(0));
    }

    @Test
    public void userWithEmptyEmailWillNotBeAddedViaPutMethod() {
        userController.postUser(user1);

        user2.setId(1);
        user2.setEmail("");

        final ValidationException exception = assertThrows(ValidationException.class,
                () -> userController.putUser(user2));

        final List<User> getUsers = userController.getAllUsers();

        assertEquals("PUT: [email пустой или не содержит символ @]", exception.getMessage());
        assertEquals(user1, getUsers.get(0));
    }

    @Test
    public void userWithEmailWithoutAtWillNotBeAddedViaPutMethod() {
        userController.postUser(user1);

        user2.setId(1);
        user2.setEmail("user2gmail.com");

        final ValidationException exception = assertThrows(ValidationException.class,
                () -> userController.putUser(user2));

        final List<User> getUsers = userController.getAllUsers();

        assertEquals("PUT: [email пустой или не содержит символ @]", exception.getMessage());
        assertEquals(user1, getUsers.get(0));
    }

    @Test
    public void userWithAnEmptyLoginWillNotBeAddedViaPutMethod() {
        userController.postUser(user1);

        user2.setId(1);
        user2.setLogin("");

        final ValidationException exception = assertThrows(ValidationException.class,
                () -> userController.putUser(user2));

        final List<User> getUsers = userController.getAllUsers();

        assertEquals("PUT: [login пустой или содержит пробелы]", exception.getMessage());
        assertEquals(user1, getUsers.get(0));
    }

    @Test
    public void userWithSpacesInLoginWillNotBeAddedViaPutMethod() {
        userController.postUser(user1);

        user2.setId(1);
        user2.setLogin("login 2");

        final ValidationException exception = assertThrows(ValidationException.class,
                () -> userController.putUser(user2));

        final List<User> getUsers = userController.getAllUsers();

        assertEquals("PUT: [login пустой или содержит пробелы]", exception.getMessage());
        assertEquals(user1, getUsers.get(0));
    }

    @Test
    public void ifNameIsEmptyNameWillBeLoginViaPutMethod() {
        userController.postUser(user1);

        user2.setId(1);
        user2.setName("");

        userController.putUser(user2);

        assertEquals(user2.getLogin(), user2.getName());
    }

    @Test
    public void userWithDateOfBirthInFutureWillNotBeAddedViaPutMethod() {
        userController.postUser(user1);

        user2.setId(1);
        user2.setBirthday(LocalDate.now().plusDays(1));

        final ValidationException exception = assertThrows(ValidationException.class,
                () -> userController.putUser(user2));

        final List<User> getUsers = userController.getAllUsers();

        assertEquals("PUT: [birthday в будущем]", exception.getMessage());
        assertEquals(user1, getUsers.get(0));
    }
}