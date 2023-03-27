package ru.yandex.practicum.filmorate.dao.film;

import ru.yandex.practicum.filmorate.model.Friendship;

import java.util.List;

public interface FriendshipDao {
    Boolean addFriendToUser(Long userId, Long friendId);
    Boolean updateFriendToUser(Long userId, Long friendId, Boolean status);
    Boolean deleteFriendFromUser(Long userId, Long friendId);
    List<Friendship> getFriendsToUser(Long userId);
}
