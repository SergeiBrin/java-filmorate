package ru.yandex.practicum.filmorate.controllers.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserDbService;
import ru.yandex.practicum.filmorate.validation.validator.Validator;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserDbService userService;
    private final Validator validator; // Выбирается по Primary

    @Autowired
    public UserController(UserDbService userService, Validator validator) {
        this.userService = userService;
        this.validator = validator;
    }

    // получение списка всех пользователей
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable Long userId) {
        validator.checkIfUserExistById(userId);
        return userService.getUserById(userId);
    }

    @GetMapping("{userId}/friends")
    public List<User> getUserFriendsList(@PathVariable Long userId) {
        validator.checkIfUserExistById(userId);
        return userService.getUserFriendsList(userId);
    }

    @GetMapping("/{userId}/friends/common/{friendId}")
    public List<User> getListOfMutualFriends(@PathVariable Long userId,
                                             @PathVariable Long friendId) {
        validator.checkIfUserExistById(userId);
        validator.checkIfUserExistById(friendId);

        return userService.getListOfCommonFriends(userId, friendId);
    }

    // создание пользователя
    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        validator.checkForName(user);
        return userService.createUser(user);
    }

    // обновление пользователя
    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        validator.checkForName(user);
        validator.checkIfUserExists(user);
        return userService.updateUser(user);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public User addFriendToUser(@PathVariable Long userId,
                                @PathVariable Long friendId) {
        validator.checkIfUserExistById(userId);
        validator.checkIfUserExistById(friendId);

        return userService.addFriendToUser(userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public User deleteFriendFromUser(@PathVariable Long userId,
                                     @PathVariable Long friendId) {
        validator.checkIfUserExistById(userId);
        validator.checkIfUserExistById(friendId);

        return userService.deleteFriendFromUser(userId, friendId);
    }
}
