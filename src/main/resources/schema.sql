-- Методы пригодятся, если нужно очистить пересоздать таблицы,
-- тем самым, очистив их.

--DROP TABLE IF EXISTS genres CASCADE;
--DROP TABLE IF EXISTS rates CASCADE;
--DROP TABLE IF EXISTS statuses CASCADE;
--DROP TABLE IF EXISTS users CASCADE;
--DROP TABLE IF EXISTS friendship CASCADE;
--DROP TABLE IF EXISTS films CASCADE;
--DROP TABLE IF EXISTS film_genre CASCADE;
--DROP TABLE IF EXISTS likes CASCADE;

CREATE TABLE IF NOT EXISTS users (
user_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
name varchar(50),
login varchar(50) NOT NULL,
birthday date NOT NULL,
email varchar(50) NOT NULL,

CHECK (birthday <= NOW())
);

CREATE TABLE IF NOT EXISTS friendship (
user_id INTEGER REFERENCES users (user_id) ON DELETE CASCADE,
friend_id INTEGER REFERENCES users (user_id) ON DELETE CASCADE,
status boolean NOT NULL,

PRIMARY KEY (user_id, friend_id),
CHECK (user_id <> friend_id)
);

CREATE TABLE IF NOT EXISTS mpa (
mpa_id int PRIMARY KEY,
mpa_name varchar(50)
);

CREATE TABLE IF NOT EXISTS films (
film_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
name varchar(50) NOT NULL,
description varchar(200),
release_date date NOT NULL,
duration int NOT NULL,
mpa_id INTEGER REFERENCES mpa (mpa_id),

CHECK (release_date >= '1895-12-28'),
CHECK (duration > 0)
);

CREATE TABLE IF NOT EXISTS genres (
genre_id int PRIMARY KEY,
genre_name varchar(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS film_genre (
film_id INTEGER REFERENCES films (film_id) ON DELETE CASCADE,
genre_id INTEGER REFERENCES genres (genre_id) ON DELETE CASCADE,

PRIMARY KEY (film_id, genre_id)
);

CREATE TABLE IF NOT EXISTS likes (
user_id INTEGER REFERENCES users (user_id) ON DELETE CASCADE,
film_id INTEGER REFERENCES films (film_id) ON DELETE CASCADE,

PRIMARY KEY (user_id, film_id)
);