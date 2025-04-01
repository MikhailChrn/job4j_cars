package ru.job4j.cars.repository;

import ru.job4j.cars.entity.CarBrand;

import java.util.Collection;
import java.util.Optional;

public interface CarBrandRepository {

    Optional<CarBrand> save(CarBrand carBrand);

    boolean update(CarBrand carBrand);

    boolean deleteById(int carBrandId);

    Collection<CarBrand> findAll();

    Optional<CarBrand> findById(int carBrandId);

}
