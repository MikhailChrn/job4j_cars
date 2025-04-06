ALTER TABLE cars
ADD COLUMN
car_brand_id INT NOT NULL REFERENCES car_brands(id);