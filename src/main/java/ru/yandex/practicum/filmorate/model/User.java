package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
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
    private final Set<Long> friends = new HashSet<>();

    public boolean addFriend(Long friendId) {
        return friends.add(friendId);
    }

    public boolean deleteFriend(Long friendId) {
        return friends.remove(friendId);
    }
}
