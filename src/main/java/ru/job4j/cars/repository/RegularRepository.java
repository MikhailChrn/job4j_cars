package ru.job4j.cars.repository;

import java.util.Collection;
import java.util.Optional;

public interface RegularRepository<T> {

    Optional<T> save(T t);

    boolean update(T t);

    boolean deleteById(int id);

    Collection<T> findAll();

    Optional<T> findById(int id);

}
