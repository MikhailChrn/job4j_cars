ALTER TABLE cars
ADD COLUMN
body_type_id INT REFERENCES body_types(id);