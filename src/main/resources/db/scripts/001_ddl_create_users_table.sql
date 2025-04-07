CREATE TABLE users (
    id        SERIAL PRIMARY KEY,
    name      TEXT,
    login     TEXT   NOT NULL UNIQUE,
    password  TEXT   NOT NULL
);