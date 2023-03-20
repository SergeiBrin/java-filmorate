package ru.yandex.practicum.filmorate.dao.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorageDao {
    List<User> getAllUsers();
    User getUserById(Long userId);
    User createUser(User user);
    User updateUser(User user);
    List<User> getUserFriendsList(Long userId);
    List<User> getListOfCommonFriends(Long userId, Long friendId);
}
