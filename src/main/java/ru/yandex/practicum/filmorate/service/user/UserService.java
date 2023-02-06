package ru.yandex.practicum.filmorate.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(InMemoryUserStorage userStorage) { // Может быть тут нужен UserStorage?
        this.userStorage = userStorage;
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User getUserById(Long userId) {
        return userStorage.getUserById(userId);
    }

    public List<User> getUserFriendsList(Long userId) {
        List<User> friends = new ArrayList<>();
        User user = userStorage.getUserById(userId);
        Set<Long> friendId = user.getFriends();

        for (Long id : friendId) {
            friends.add(userStorage.getUserById(id));
        }

        return friends;
    }

    public List<User> getListOfMutualFriends(Long userId, Long friendId) {
        List<User> friends = new ArrayList<>();

        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);

        Set<Long> usersId = user.getFriends();
        Set<Long> friendsId = friend.getFriends();

        for (Long id : usersId) {
            if (friendsId.contains(id)) {
                friends.add(userStorage.getUserById(id));
            }
        }

        return friends;
    }

    public User postUser(User user) {
        return userStorage.postUser(user);
    }

    public User putUser(User user) {
        return userStorage.putUser(user);
    }

    public User putFriendsToUser(Long userId, Long friendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);

        user.addFriend(friendId);
        friend.addFriend(userId);

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
