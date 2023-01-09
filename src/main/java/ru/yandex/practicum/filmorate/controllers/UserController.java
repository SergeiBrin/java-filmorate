package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private int id;
    private final Map<Integer, User> users = new HashMap<>();

    // получение списка всех пользователей
    @GetMapping
    public List<User> getAllUsers() {
        log.info("Список всех пользователей {} отправлен клиенту", users.values());

        return new ArrayList<>(users.values());
    }

    // создание пользователя
    @PostMapping
    public User postUser(@RequestBody User user) {
        // Проверки на ошибки в заполнении email, login, birthday.
        validateUser(user, "POST");

        user.setId(++id);

        users.put(user.getId(), user);
        log.info("Пользователь добавлен {}", user);

        return user;
    }

    // обновление пользователя
    @PutMapping
    public User putUser(@RequestBody User user) {
        validateUser(user, "PUT");

        // Если все проверки пройдены, то объект user обновляется.
        users.put(user.getId(), user);
        log.info("Пользователь обновлен {}", user);

        return user;
    }

    // Проверка тела запроса на несоответствия правилам API.
    private void validateUser(User user, String methodName) {
        List<String> exceptionMessage = new ArrayList<>();

        if (user == null) {
            throw new ValidationException(methodName + ": [Запрос пустой]");
        }

        boolean isThereAnId = users.containsKey(user.getId());
        boolean isEmailCorrect = checkEmail(user.getEmail());
        boolean isLoginCorrect = checkLogin(user.getLogin());
        boolean isBirthdayCorrect = checkBirthday(user.getBirthday());

        switch (methodName) {
            case "POST" -> {
                if (user.getId() != 0) {
                    exceptionMessage.add("В метод POST нельзя передавать id пользователя");
                }
            }
            case "PUT" -> {
                if (!isThereAnId) {
                    exceptionMessage.add("Пользователя с таким id нет");
                }
            }
        }

        if (!isLoginCorrect) {
            exceptionMessage.add("login пустой или содержит пробелы");
        } else {
            checkName(user);
        }

        if (!isEmailCorrect) {
            exceptionMessage.add("email пустой или не содержит символ @");
        }

        if (!isBirthdayCorrect) {
            exceptionMessage.add("birthday в будущем");
        }

        // Проверка на то, что лист сообщений об ошибках пустой.
        // Если лист не пустой, то выбрасываю исключение.
        if (!exceptionMessage.isEmpty()) {
            String logMessage = methodName + ": " + exceptionMessage;
            log.warn(logMessage);
            throw new ValidationException(logMessage);
        }
    }

    // Проверка, что строка email не null, и с символом "@".
    private boolean checkEmail(String email) {
        return (email != null) && (email.contains("@"));
    }

    // Проверка, что строка login не null, не пустая и без пробелов.
    private boolean checkLogin(String login) {
        return (login != null) && (!login.isEmpty()) && (!login.contains(" "));
    }

    // Проверка, что birthday пользователя не из будущего.
    private boolean checkBirthday(LocalDate birthday) {
        return (birthday != null) && (LocalDate.now().isAfter(birthday)) || (LocalDate.now().equals(birthday));
    }

    // Если имя пустое, то login становится именем.
    private void checkName(User user) {
        boolean isNameCorrect = (user.getName() != null) && (!user.getName().isEmpty());

        if (!isNameCorrect) {
            user.setName(user.getLogin());
        }
    }
}
