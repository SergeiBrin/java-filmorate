package ru.yandex.practicum.filmorate.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.user.UserStorageDao;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class InMemoryUserService implements UserService {
    private final UserStorageDao userStorage;

    @Autowired
    public InMemoryUserService(UserStorageDao userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User getUserById(Long userId) {
        return userStorage.getUserById(userId);
    }

    // Здесь нужна доработка. Ведь теперь есть ещё true и false
    public List<User> getUserFriendsList(Long userId) {
        List<User> friends = new ArrayList<>();
        User user = userStorage.getUserById(userId);
        Set<Long> friendId = user.getFriends().keySet();

        for (Long id : friendId) {
            friends.add(userStorage.getUserById(id));
        }

        return friends;
    }

    // Здесь можно доработать до true и false
    public List<User> getListOfCommonFriends(Long userId, Long friendId) {
        List<User> friends = new ArrayList<>();

        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);

        Set<Long> usersId = user.getFriends().keySet();
        Set<Long> friendsId = friend.getFriends().keySet();

        for (Long id : usersId) {
            if (friendsId.contains(id)) {
                friends.add(userStorage.getUserById(id));
            }
        }

        return friends;
    }

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public User addFriendToUser(Long userId, Long friendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);

        user.addFriend(friendId, true);
        friend.addFriend(userId, false);

        return userStorage.getUserById(userId);
    }

    public User deleteFriendFromUser(Long userId, Long friendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);

        user.deleteFriend(friendId);
        friend.deleteFriend(userId);

        return user;
    }
}
