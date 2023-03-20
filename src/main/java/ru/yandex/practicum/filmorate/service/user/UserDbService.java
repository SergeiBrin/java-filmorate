package ru.yandex.practicum.filmorate.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.film.FriendshipDao;
import ru.yandex.practicum.filmorate.dao.user.UserStorageDao;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Service
@Primary
public class UserDbService implements UserService {
    private UserStorageDao userStorage;
    private FriendshipDao friendshipDao;

    @Autowired
    public UserDbService(UserStorageDao userStorage, FriendshipDao friendshipDao) {
        this.userStorage = userStorage;
        this.friendshipDao = friendshipDao;
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User getUserById(Long userId) {
        return userStorage.getUserById(userId);
    }

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public User addFriendToUser(Long userId, Long friendId) {
        friendshipDao.addFriendToUser(userId, friendId);
        return userStorage.getUserById(userId);
    }

    public User deleteFriendFromUser(Long userId, Long friendId) {
        friendshipDao.deleteFriendFromUser(userId, friendId);
        return userStorage.getUserById(userId);
    }

    public List<User> getUserFriendsList(Long userId) {
        return userStorage.getUserFriendsList(userId);
    }

    public List<User> getListOfCommonFriends(Long userId, Long friendId) {
        return userStorage.getListOfCommonFriends(userId, friendId);
    }
}
