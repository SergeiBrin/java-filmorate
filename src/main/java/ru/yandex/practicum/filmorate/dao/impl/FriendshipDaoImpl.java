package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FriendshipDao;
import ru.yandex.practicum.filmorate.model.Friendship;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@Slf4j
public class FriendshipDaoImpl implements FriendshipDao {
    JdbcTemplate jdbcTemplate;

    @Autowired
    public FriendshipDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Boolean addFriendToUser(Long userId, Long friendId) {
        String sqlQuery = "insert into friendship (user_id, friend_id, status) values (?, ?, ?)";

        Friendship userToFriend = getFriendshipStatus(userId, friendId);
        Friendship friendToUser = getFriendshipStatus(friendId, userId);

        boolean isVerifiedFriend = false;

        if (userToFriend == null) {
            isVerifiedFriend = jdbcTemplate.update(sqlQuery, userId, friendId, true) > 0;
            log.info("Пользователю с id {} присвоен статус дружбы на {} с пользователем с id {}", userId, true, friendId);
        } else if (!userToFriend.getStatus()) {
            isVerifiedFriend = updateFriendToUser(userId, friendId, true);
        }

        if (friendToUser == null) {
            jdbcTemplate.update(sqlQuery, friendId, userId, false);
        }

        return isVerifiedFriend;
    }

    @Override
    public Boolean updateFriendToUser(Long userId, Long friendId, Boolean status) {
        String sqlQuery = "update friendship set status = ? " +
                          "where user_id = ? and friend_id = ?";

        Boolean isUpdateFriend = jdbcTemplate.update(sqlQuery, status, userId, friendId) > 0;

        if (isUpdateFriend) {
            log.info("Для пользователя с id {} обновлен статус дружбы на {} с пользователем с id {}", userId, status, friendId);
        }

        return isUpdateFriend;
    }

    @Override
    public Boolean deleteFriendFromUser(Long userId, Long friendId) {
        String sqlQuery = "delete from friendship " +
                          "where (user_id = ? and friend_id = ?)";

        Boolean isDeleteFriend =  jdbcTemplate.update(sqlQuery, userId, friendId) > 0;


        if (isDeleteFriend) {
            log.info("У пользователя с id {} удален друг с id {}", userId, friendId);
        }

        return isDeleteFriend;
    }

    @Override
    public List<Friendship> getFriendsToUser(Long userId) {
        String sqlQuery = "select friend_id, status " +
                          "from friendship " +
                          "where user_id = ?";

        List<Friendship> getFriends = jdbcTemplate.query(sqlQuery, this::mapRowToFriendship, userId);
        log.info("Список id друзей со статусами дружбы для пользователя с id {} отправлен", userId);

        return getFriends;
    }

    private Friendship getFriendshipStatus(Long userId, Long friendId) {
        String sqlQuery = "select friend_id, status " +
                          "from friendship " +
                          "where user_id = ? and friend_id = ?";

        try {
            Friendship friendship = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFriendship, userId, friendId);
            log.info("Возвращен статус дружбы {} между двумя пользователями с id {} и {}", friendship, userId, friendId);
            return friendship;
        } catch (EmptyResultDataAccessException e) {
            log.info("В таблице friendship нет связи между двумя пользователями с id {} и {}", userId, friendId);
            return null;
        }
    }

    private Friendship mapRowToFriendship(ResultSet resultSet, int rowNum) throws SQLException {
        return Friendship.builder()
                .friendId(resultSet.getLong("friend_id"))
                .status(resultSet.getBoolean("status"))
                .build();
    }
}
