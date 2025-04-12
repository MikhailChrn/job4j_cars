INSERT INTO car_brands (id, title)
VALUES
(0, 'LADA'),
(1, 'Moskvich'),
(2, 'GAZ');

INSERT INTO body_types (id, title)
VALUES
(0, 'Sedan'),
(1, 'Hatchback'),
(2, 'Wagon');

INSERT INTO engines (id, title)
VALUES
(0, 'Gasoline'),
(1, 'Dizel'),
(2, 'Electric');

INSERT INTO cars (id, title, car_brand_id, body_type_id, engine_id)
VALUES
(0, 'Vesta SPORTedition', 0, 0, 0),
(1, 'Vesta WagonElectric', 0, 2, 2),
(2, 'Moskvich 3 electric', 1, 1, 1),
(3, 'Moskvich 6', 1, 0, 0),
(4, 'Sobol NN', 2, 2, 0),
(5, 'Sobol NN', 2, 2, 1);