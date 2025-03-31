package ru.job4j.cars.repository;

import ru.job4j.cars.entity.Engine;

import java.util.Collection;
import java.util.Optional;

public interface EngineRepository {

    Optional<Engine> save(Engine engine);

    boolean update(Engine engine);

    boolean deleteById(int engineId);

    Collection<Engine> findAll();

    Optional<Engine> findById(int engineId);

}
