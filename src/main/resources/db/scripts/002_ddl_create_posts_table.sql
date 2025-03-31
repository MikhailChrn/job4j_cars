CREATE TABLE posts (
    id             SERIAL     PRIMARY KEY,
    title          TEXT       NOT NULL,
    description    TEXT,
    created        TIMESTAMP  NOT NULL,
    user_id        INT        NOT NULL    REFERENCES users(id)
);