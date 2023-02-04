package ru.yandex.practicum.filmorate.controllers.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserStorage userStorage;
    private final UserService userService;

    @Autowired
    public UserController(InMemoryUserStorage userStorage, UserService userService) {
        this.userStorage = userStorage;
        this.userService = userService;
    }

    // получение списка всех пользователей
    @GetMapping
    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable Long userId) {
        if (userStorage.getUserById(userId) == null) {
            throw new UserNotFoundException(String.format("Пользователя с таким %d нет", userId));
        }

        return userStorage.getUserById(userId);
    }

    @GetMapping("{userId}/friends")
    public List<User> getUserFriendsList(@PathVariable Long userId) {
        if (userId == null) {
            throw new ValidationException("Id пользователя не передан в PathVariable");
        }

        if (userStorage.getUserById(userId) == null) {
            throw new UserNotFoundException(String.format("Пользователя с таким %d нет", userId));
        }

        return userService.getUserFriendsList(userId);
    }

    @GetMapping("/{userId}/friends/common/{friendId}")
    public List<User> getListOfMutualFriends(@PathVariable Long userId,
                                             @PathVariable Long friendId) {
        if (userId == null || friendId == null) {
            throw new ValidationException("Id пользователя и/друга не передан в PathVariable");
        }

        if (userStorage.getUserById(userId) == null) {
            throw new UserNotFoundException(String.format("Пользователя с таким %d нет", userId));
        }

        if (userStorage.getUserById(friendId) == null) {
            throw new UserNotFoundException(String.format("Пользователя с таким %d нет", friendId));
        }

        return userService.getListOfMutualFriends(userId, friendId);
    }

    // создание пользователя
    @PostMapping
    public User postUser(@Valid @RequestBody User user) {
        return userStorage.postUser(user);
    }

    // обновление пользователя
    @PutMapping
    public User putUser(@Valid @RequestBody User user) {
        if (userStorage.getUserById(user.getId()) == null) {
            throw new UserNotFoundException(String.format("Пользователя с таким %d нет", user.getId()));
        }

        return userStorage.putUser(user);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public User putFriendsToUser(@PathVariable Long userId,
                                 @PathVariable Long friendId) {
        if (userId == null || friendId == null) {
            throw new ValidationException("Id пользователя и/друга не передан в PathVariable");
        }

        if (userStorage.getUserById(userId) == null) {
            throw new UserNotFoundException(String.format("Пользователя с таким %d нет", userId));
        }

        if (userStorage.getUserById(friendId) == null) {
            throw new UserNotFoundException(String.format("Пользователя с таким %d нет", friendId));
        }

        return userService.putFriendsToUser(userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public User deleteFriendFromUser(@PathVariable Long userId,
                                     @PathVariable Long friendId) {
        if (userId == null || friendId == null) {
            throw new ValidationException("Id пользователя и/друга не передан в PathVariable");
        }

        if (userStorage.getUserById(userId) == null) {
            throw new UserNotFoundException(String.format("Пользователя с таким %d нет", userId));
        }

        if (userStorage.getUserById(friendId) == null) {
            throw new UserNotFoundException(String.format("Пользователя с таким %d нет", friendId));
        }

        return userService.deleteFriendFromUser(userId, friendId);
    }
}
