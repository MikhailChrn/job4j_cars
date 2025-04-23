CREATE TABLE car_brands (
    id           SERIAL  PRIMARY KEY,
    title        TEXT    NOT NULL
);

CREATE TABLE body_types (
    id           SERIAL  PRIMARY KEY,
    title        TEXT    NOT NULL
);

CREATE TABLE engines (
    id       SERIAL   PRIMARY KEY,
    title    TEXT     NOT NULL
);


CREATE TABLE cars (
    id           SERIAL  PRIMARY KEY,
    title        TEXT,
    car_brand_id INT     REFERENCES car_brands(id),
    body_type_id INT     REFERENCES body_types(id),
    engine_id    INT     REFERENCES engines(id)
);
