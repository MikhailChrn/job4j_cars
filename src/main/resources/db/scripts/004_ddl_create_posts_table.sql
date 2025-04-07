CREATE TABLE posts (
    id             SERIAL     PRIMARY KEY,
    title          TEXT       NOT NULL,
    description    TEXT,
    created        TIMESTAMP  NOT NULL,
    issold         BOOLEAN,
    user_id        INT        NOT NULL   REFERENCES users(id),
    car_id         INT        REFERENCES cars(id),
    file_id        INT        REFERENCES files(id)
);

CREATE TABLE price_history (
   id              SERIAL     PRIMARY KEY,
   before          BIGINT,
   after           BIGINT     NOT NULL,
   created         TIMESTAMP  WITHOUT TIME ZONE,
   post_id         INT        REFERENCES posts(id)
);