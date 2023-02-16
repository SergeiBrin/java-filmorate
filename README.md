# java-filmorate
Template repository for Filmorate project  
  
  
![Image database diagram](https://github.com/SergeiBrin/java-filmorate/blob/controllers-films-users/Diagram%20Filmorate.png)

# Database diagram  
## Краткое описание
Database Diagram - это ER диаграмма, которая описывает структуру базы данных для приложения Filmorate. 

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
1. **Получить список фильмов**
```sql 
SELECT *
FROM film;
```
2. **Получить список популярных фильмов**
```sql
SELECT *
FROM popular_films AS pf
JOIN film AS f ON pf.film_id = f.film_id
ORDER BY pf.popular_id;
```
3. **Получить список фильмов с названиями жанров**
```sql
SELECT *
FROM film AS f
LEFT JOIN film_genre AS fg ON f.film_id = fg.film_id
LEFT JOIN genre AS g ON fg.genre_id = g.genre_id;
```
4. **Получить список фильмов - каждый с количеством лайков**
```sql 
SELECT f.film_id, 
       f.name,
       COUNT(l.user_id)
FROM film AS f
LEFT JOIN like AS l ON f.film_id = l.film_id
GROUP BY f.film_id, f.name, l.user_id
ORDER BY f.film_id;
```  
5. **Получить список пользователей**
```sql 
SELECT *
FROM user;
```
6. **Получить список друзей пользователя**
```sql
SELECT *
FROM user
WHERE user.id IN (SELECT DISTINCT f.friend_id
                  FROM friendship AS f 
                  WHERE f.user_id = 1 AND f.status_id = 1);
                     -- f.user_id = 1 - это id пользователя, 
                     -- чей список друзей нужно вернуть.
```                     
                  
7. **Получить список общих друзей у двух пользователей**
```sql 
SELECT *
FROM user AS us
WHERE  us.user_id IN (SELECT f.friend_id    -- Берем id друзей первого пользователя и сравниваем их с id друзей второго пользователя через оператор IN.
                      FROM friendship AS f  -- Cовпадения пробрасываем дальше, где они сравниваются с id всех пользователей,
                      WHERE f.user_id = 1   -- и совпадения - то есть данные общих друзей выводятся на экран. 
                      AND f.status_id = 1   
                      AND f.friend_id IN (SELECT DISTINCT f.friend_id  -- Берем id друзей второго пользователя и пробрасываем 
                                          FROM friendship AS f         -- выше - для сравнения.
                                          WHERE f.user_id = 2 
                                          AND f.status_id = 1); 
``` 
