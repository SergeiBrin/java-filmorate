package ru.yandex.practicum.filmorate.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.user.UserStorageDao;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Service
public abstract class InMemoryUserService implements UserService {
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

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }
}

    /*
    !!! При включении этих методов для InMemoryUserStorage, нужно создать
        в классе User поле: Set<Long> friends = new HashSet<>();
        и сделать класс InMemoryUserService неабстрактным.

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


    // Здесь нужна доработка. Ведь теперь есть ещё true и false
    public List<User> getUserFriendsList(Long userId) {
        List<User> friends = new ArrayList<>();
        User user = userStorage.getUserById(userId);
        Set<Long> friendId = user.getFriends();

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

        Set<Long> usersId = user.getFriends();
        Set<Long> friendsId = friend.getFriends();

        for (Long id : usersId) {
            if (friendsId.contains(id)) {
                friends.add(userStorage.getUserById(id));
            }
        }

        return friends;
    }
    */
