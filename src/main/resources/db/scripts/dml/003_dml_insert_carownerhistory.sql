INSERT INTO owners (id, name)
VALUES
(0, 'Owner 0'),
(1, 'Owner 1'),
(2, 'Owner 2'),
(3, 'Owner 3'),
(4, 'Owner 4');

INSERT INTO cars_owners_history (id, car_id, owner_id, startAt, endAt)
VALUES
(0, 0, 4, '2020-02-01 00:00', '2023-08-23 00:00'),
(1, 0, 1, '2023-08-23 00:00', 'now'),
(2, 1, 0, '2016-12-05 00:00', '2021-03-11 00:00'),
(3, 1, 2, '2021-03-11 00:00', 'now'),
(4, 2, 3, '2019-01-27 00:00', '2024-09-02 00:00'),
(5, 2, 0, '2024-09-02 00:00', 'now'),
(6, 3, 2, '2011-02-02 00:00', '2017-02-02 00:00'),
(7, 3, 4, '2017-02-02 00:00', 'now'),
(8, 4, 1, '2022-06-15 00:00', '2023-01-12 00:00'),
(9, 4, 3, '2023-01-12 00:00', 'now'),
(10, 5, 4, '2019-09-09 00:00', '2021-11-04 00:00'),
(11, 5, 0, '2021-11-04 00:00', 'now');
