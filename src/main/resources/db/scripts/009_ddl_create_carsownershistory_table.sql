CREATE TABLE cars_owners_history (
    id        SERIAL PRIMARY KEY,
    car_id    INT,
    owner_id  INT,
    startAt   TIMESTAMP,
    endAt     TIMESTAMP
);