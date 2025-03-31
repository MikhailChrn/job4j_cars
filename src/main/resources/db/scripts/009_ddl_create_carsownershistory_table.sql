CREATE TABLE cars_owners_history (
    id        SERIAL PRIMARY KEY,
    car_id    INT    NOT NULL  REFERENCES cars(id),
    owner_id  INT    NOT NULL  REFERENCES owners(id),
    startAt   TIMESTAMP NOT NULL,
    endAt     TIMESTAMP NOT NULL
);