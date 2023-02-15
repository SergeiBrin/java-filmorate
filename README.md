# java-filmorate
Template repository for Filmorate project  
  
  
![Image database diagram](https://github.com/SergeiBrin/java-filmorate/blob/controllers-films-users/Diagram%20Filmorate.png)

# Database diagram  
## Краткое описание
Database Diagram - это ER диаграмма, которая описывает структуру хранения базы данных приложения Filmorate. 

В приложении Filmorate есть 2 основные сущности - пользователи (User) и фильмы (Film), которые хранятся в базе данных, а также взаимодействуют между собой. Система хранения в Database Diagram устроена следующим образом:
1. **film** - таблица, которая хранит фильмы;
2. **genre** - названия жанров для фильмов;
3. **film_genre** - таблица, связывающая фильмы и жанры к ним; 
4. **popular_films** - 10 самых популярных фильмов;
5. **user** - таблица, которая хранит пользователей;
6. **friendship** - таблица, которая хранит данные о дружбе между пользователями;
7. **status** - статусы true и false для подтверждения дружбы между пользователями в таблице friendship;
8. **like** - таблица, которая хранит лайки от пользователей к фильмам.
  
## Описание основных операций:
1. **Получить список фильмов**__
SELECT *__
FROM film;'


