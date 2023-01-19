package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
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
    public User postUser(@Valid @RequestBody User user) {
        if (user.getId() != 0) {
            log.warn("В метод POST передан id пользователя");
            throw new ValidationException("В метод POST нельзя передавать id пользователя");
        }

        checkName(user);
        user.setId(++id);

        users.put(user.getId(), user);
        log.info("Пользователь добавлен {}", user);

        return user;
    }

    // обновление пользователя
    @PutMapping
    public User putUser(@Valid @RequestBody User user) {
        boolean isThereAnId = users.containsKey(user.getId());
        if (!isThereAnId) {
            log.warn("В метод PUT передан пользователь с несуществующим id");
            throw new ValidationException("Пользователя с таким id нет");
        }

        checkName(user);

        // Если все проверки пройдены, то объект user обновляется.
        users.put(user.getId(), user);
        log.info("Пользователь обновлен {}", user);

        return user;
    }

    // Если имя пустое, то login становится именем.
    private void checkName(User user) {
        boolean isNameCorrect = (user.getName() != null) && (!user.getName().isEmpty());

        if (!isNameCorrect) {
            user.setName(user.getLogin());
        }
    }
}
