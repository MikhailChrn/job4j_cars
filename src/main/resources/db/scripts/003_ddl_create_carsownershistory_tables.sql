CREATE TABLE owners (
    id      SERIAL   PRIMARY KEY,
    name    TEXT     NOT NULL
);

CREATE TABLE cars_owners_history (
    id        SERIAL PRIMARY KEY,
    car_id    INT REFERENCES cars(id),
    owner_id  INT REFERENCES owners(id),
    startAt   TIMESTAMP,
    endAt     TIMESTAMP
);