CREATE TABLE cars (
    id           SERIAL  PRIMARY KEY,
    title        TEXT    NOT NULL,
    engine_id    INT     REFERENCES engines(id)
);