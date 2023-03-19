package ru.yandex.practicum.filmorate.service.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    User getUserById(Long userId);
    User postUser(User user);
    User putUser(User user);
    User addFriendToUser(Long userId, Long friendId);
    User deleteFriendFromUser(Long userId, Long friendId);
    List<User> getUserFriendsList(Long userId);
}
