package ru.job4j.cars.repository;

import ru.job4j.cars.entity.Car;

import java.util.Collection;
import java.util.Optional;

public interface CarRepository {

    Optional<Car> save(Car car);

    boolean update(Car car);

    boolean deleteById(int carId);

    Collection<Car> findAll();

    Optional<Car> findById(int carId);

}
