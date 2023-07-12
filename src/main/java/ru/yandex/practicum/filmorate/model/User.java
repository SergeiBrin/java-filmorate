package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
public class User {
    private long id;
    private String name; // Может быть пустым
    @NotBlank
    @Pattern(regexp = "\\S+") // Должно содержать хотя бы один непробельный символ
    private String login;
    @PastOrPresent
    private LocalDate birthday;
    @NotBlank
    @Email
    private String email;
}
