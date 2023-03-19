package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    List<User> getAllUsers();
    User getUserById(Long userId);
    User postUser(User user);
    User putUser(User user);
    List<User> getUserFriendsList(Long userId);
    List<User> getListOfCommonFriends(Long userId, Long friendId);
}
