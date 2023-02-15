package ru.yandex.practicum.filmorate.controllers.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.validation.validator.Validator;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;
    private final Validator validator;

    @Autowired
    public UserController(UserService userService, Validator validator) {
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
        validator.checkUserByPathVariableId(userId);
        return userService.getUserById(userId);
    }

    @GetMapping("{userId}/friends")
    public List<User> getUserFriendsList(@PathVariable Long userId) {
        validator.checkUserByPathVariableId(userId);
        return userService.getUserFriendsList(userId);
    }

    @GetMapping("/{userId}/friends/common/{friendId}")
    public List<User> getListOfMutualFriends(@PathVariable Long userId,
                                             @PathVariable Long friendId) {
        validator.checkUserByPathVariableId(userId);
        validator.checkUserByPathVariableId(friendId);

        return userService.getListOfMutualFriends(userId, friendId);
    }

    // создание пользователя
    @PostMapping
    public User postUser(@Valid @RequestBody User user) {
        return userService.postUser(user);
    }

    // обновление пользователя
    @PutMapping
    public User putUser(@Valid @RequestBody User user) {
        validator.checkIfUserExistsById(user);
        return userService.putUser(user);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public User putFriendsToUser(@PathVariable Long userId,
                                 @PathVariable Long friendId) {
        validator.checkUserByPathVariableId(userId);
        validator.checkUserByPathVariableId(friendId);

        return userService.putFriendsToUser(userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public User deleteFriendFromUser(@PathVariable Long userId,
                                     @PathVariable Long friendId) {
        validator.checkUserByPathVariableId(userId);
        validator.checkUserByPathVariableId(friendId);

        return userService.deleteFriendFromUser(userId, friendId);
    }
}
