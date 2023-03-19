package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@Builder
public class User {
    private long id; // Может сделать Integer?
    private String name; // Может быть пустым
    @NotBlank
    @Pattern(regexp = "\\S+")
    private String login;
    @PastOrPresent
    private LocalDate birthday;
    @NotBlank
    @Email
    private String email;
    @NotNull
    private final Map<Long, Boolean> friends = new HashMap<>();

    public void addFriend(Long friendId, Boolean status) {
        friends.put(friendId, status);
    }

    public boolean deleteFriend(Long friendId) {
        return friends.remove(friendId);
    }
}
