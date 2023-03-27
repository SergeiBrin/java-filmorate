# java-filmorate
Template repository for Filmorate project.

![Image database diagram](https://github.com/SergeiBrin/java-filmorate/blob/add-database/Diagram%20Filmorate.png)

# Database diagram  
## Краткое описание
Database Diagram - это ER диаграмма, которая описывает структуру базы данных для приложения Filmorate. 

  В приложении Filmorate есть 2 основные сущности - пользователь (User) и фильм (Film), которые хранятся в базе данных, и взаимодействуют между собой. Система хранения в Database Diagram устроена следующим образом:
1. **films** - таблица, которая хранит фильмы;
2. **genres** - таблица, которая хранит названия жанров для фильмов;
3. **film_genre** - таблица, связывающая фильмы и жанры к ним; 
4. **users** - таблица, которая хранит пользователей;
5. **friendship** - таблица, которая хранит данные о дружбе между пользователями;
6. **likes** - таблица, которая хранит лайки от пользователей к фильмам.

## Описание основных операций:
1. **Получить список фильмов**
```sql 
SELECT *
FROM film;
```
2. **Получить список фильмов с названиями жанров**
```sql
SELECT f.film_id,
               f.name,
               f.description,
               f.release_date,
               f.duration,
               g.genre_id,
               g.genre_name
FROM films AS f
LEFT JOIN film_genre AS fg ON f.film_id = fg.film_id
LEFT JOIN genres AS g ON fg.genre_id = g.genre_id;
```
3. **Получить список фильмов - каждый с количеством лайков через таблицу likes**
```sql 
SELECT f.film_id, 
               f.name,
               f.description,
               f.release_date, 
               COUNT(l.user_id)
FROM films AS f
LEFT JOIN likes AS l ON f.film_id = l.film_id
GROUP BY f.film_id, f.name, l.user_id
ORDER BY f.film_id desc;
```  
4. **Получить список пользователей**
```sql 
SELECT *
FROM users;
```
5. **Получить список друзей пользователя**
```sql
SELECT *
FROM users
WHERE user_id IN (SELECT friend_id
                  FROM friendship 
                  WHERE user_id = ? AND status = true);
                     -- user_id = ? - это id пользователя, 
                     -- чей список друзей нужно вернуть.
```                     

6. **Получить список общих друзей двух пользователей**
```sql 
SELECT *
FROM users
WHERE user_id IN (SELECT friend_id    -- Берем id друзей первого пользователя и сравниваем их с id друзей второго пользователя через оператор IN.
                  FROM friendship   -- Cовпадения пробрасываем дальше, где они сравниваются с id всех пользователей,
                  WHERE user_id = ?   -- и совпадения - то есть данные общих друзей - выводятся на экран. 
                  AND status = true   
                  AND friend_id IN (SELECT friend_id   
                                    FROM friendship   
                                    WHERE user_id = ?
                                    AND status = true));  
``` 
