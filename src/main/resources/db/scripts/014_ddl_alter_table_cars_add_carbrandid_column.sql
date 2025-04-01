ALTER TABLE cars
ADD COLUMN car_brand_id INT REFERENCES car_brands(id);