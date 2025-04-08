CREATE TABLE files (
    id           SERIAL  PRIMARY KEY,
    post_id      INT,
    title        TEXT    NOT NULL,
    path         TEXT    NOT NULL
);