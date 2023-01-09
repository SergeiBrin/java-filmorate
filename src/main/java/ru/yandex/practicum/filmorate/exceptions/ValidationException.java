package ru.yandex.practicum.filmorate.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }

    // Отправляет клиенту код 400. Такое решение я нашел в гугле.
    // Просто по дефолту в Postman приходит код 500, что, как мне кажется, не совсем правильно.
    public void badRequest() {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "(CODE 400)\n");
    }
}
