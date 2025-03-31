package ru.job4j.cars.repository.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.entity.Car;
import ru.job4j.cars.repository.CarRepository;
import ru.job4j.cars.repository.CrudRepository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Repository
public class HbnCarRepository implements CarRepository {

    private final CrudRepository crudRepository;

    @Override
    public Optional<Car> save(Car car) {
        try {
            crudRepository.run(session -> session.persist(car));
            return Optional.of(car);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public boolean update(Car car) {
        return crudRepository.tx(
                session -> session.merge(car)).equals(car);
    }

    @Override
    public boolean deleteById(int carId) {
        return crudRepository.tx(session ->
                session.createQuery("DELETE Car c WHERE c.id = :fId")
                        .setParameter("fId", carId)
                        .executeUpdate()) > 0;
    }

    @Override
    public Collection<Car> findAll() {
        return crudRepository.query("FROM Car", Car.class);
    }

    @Override
    public Optional<Car> findById(int carId) {
        return crudRepository.optional(
                "FROM Car c WHERE c.id = :fId", Car.class,
                Map.of("fId", carId)
        );
    }
}
