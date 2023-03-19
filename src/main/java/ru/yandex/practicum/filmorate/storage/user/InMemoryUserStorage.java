package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private long id;
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public List<User> getAllUsers() {
        log.info("Список всех пользователей {} отправлен клиенту", users.values());
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(Long userId) {
        log.info("Пользователь с id {} отправлены клиенту", userId);
        return users.get(userId);
    }



    @Override
    public User postUser(User user) {
        user.setId(++id);

        users.put(user.getId(), user);
        log.info("Пользователь добавлен {}", user);

        return user;
    }

    @Override
    public User putUser(User user) {
        // Если все проверки пройдены, то объект user обновляется.
        users.put(user.getId(), user);
        log.info("Пользователь обновлен {}", user);

        return user;
    }

    @Override
    public List<User> getUserFriendsList(Long userId) {
        return null;
    }

    @Override
    public List<User> getListOfCommonFriends(Long userId, Long friendId) {
        return null;
    }
}
