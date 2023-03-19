package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FriendshipDao;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@Primary
@Slf4j
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FriendshipDao friendshipDao;

    public UserDbStorage(JdbcTemplate jdbcTemplate, FriendshipDao friendshipDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.friendshipDao = friendshipDao;
    }

    @Override
    public List<User> getAllUsers() {
        String sqlQuery = "select user_id, name, login, birthday, email " +
                          "from users";

        List<User> allUsers = jdbcTemplate.query(sqlQuery, this::mapRowToUser);
        log.info("Список всех пользователей {} отправлен клиенту", allUsers);

        return allUsers;
    }

    @Override
    public User getUserById(Long userId) {
        String sqlQuery = "select user_id, name, login, birthday, email " +
                          "from users " +
                          "where user_id = ?";

        User userById = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, userId);
        log.info("Пользователь {} с id: {} отправлен клиенту", userById, userId);

        return userById;
    }

    @Override
    public List<User> getUserFriendsList(Long userId) {
        String sqlQuery = "select user_id, name, login, birthday, email " +
                          "from users " +
                          "where user_id in (select friend_id " +
                                            "from friendship " +
                                            "where user_id = ? AND status = TRUE)";

        List<User> friendList = jdbcTemplate.query(sqlQuery, this::mapRowToUser, userId);
        User user = getUserById(userId);
        log.info("Список друзей {} пользователя {} отправлен клиенту", friendList, user);

        return friendList;
    }

    @Override
    public List<User> getListOfCommonFriends(Long userId, Long friendId) {
        String sqlQuery = "select user_id, name, login, birthday, email " +
                          "from users " +
                          "where user_id in (select friend_id " +
                                            "from friendship " +
                                            "where (status = true) and (user_id = ? OR user_id = ?) " +
                                            "group by friend_id " +
                                            "having count(user_id) = 2)";

        List<User> commonFriends = jdbcTemplate.query(sqlQuery, this::mapRowToUser, userId, friendId);
        log.info("Список общих друзей {} пользователей c id {} и {} отправлен клиенту", commonFriends, userId, friendId);

        return commonFriends;
    }

    @Override
    public User postUser(User user) {
        String sqlQuery = "insert into users (name, login, birthday, email) values (?, ?, ?, ?)";

        jdbcTemplate.update(sqlQuery,
                user.getName(),
                user.getLogin(),
                user.getBirthday(),
                user.getEmail());

        User newUser = findLastUser();
        log.info("Пользователь {} добавлен в таблицу users", newUser);

        return newUser;
    }

    @Override
    public User putUser(User user) {
        String sqlQuery = "update users set name = ?, login = ?, birthday = ?, email = ? where user_id = ?";

        jdbcTemplate.update(sqlQuery, // А что, если такого id в табице нет?
                user.getName(),
                user.getLogin(),
                user.getBirthday(),
                user.getEmail(),
                user.getId());

        User updateUser = getUserById(user.getId());
        log.info("Пользователь {} обновлен в таблице users", updateUser);

        return updateUser;
    }

    private User findLastUser() {
        String sqlQuery = "select user_id, name, login, birthday, email " +
                          "from users " +
                          "order by user_id desc " +
                          "limit 1";

        User lastUser = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser);
        log.info("Из таблицы users взят последний добавленный пользователь {}", lastUser);

        return lastUser;
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        User user =  User.builder()
                .id(resultSet.getLong("user_id"))
                .name(resultSet.getString("name"))
                .login(resultSet.getString("login"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .email(resultSet.getString("email"))
                .build();

        // Перед отправкой клиенту объект User, добавляю ему его друзей - для наглядности.
        List<Friendship> friendships = friendshipDao.getFriendsToUser(user.getId());

        for (Friendship friendship : friendships) {
            user.addFriend(friendship.getFriendId(), friendship.getStatus());
        }

        return user;
    }
}
