package ru.yandex.practicum.filmorate.service.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    User getUserById(Long userId);
    User createUser(User user);
    User updateUser(User user);
    User addFriendToUser(Long userId, Long friendId);
    User deleteFriendFromUser(Long userId, Long friendId);
    List<User> getUserFriendsList(Long userId);
}
