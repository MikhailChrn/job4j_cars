package ru.job4j.cars.repository.impl;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.configuration.HibernateConfiguration;
import ru.job4j.cars.entity.Car;
import ru.job4j.cars.entity.Engine;
import ru.job4j.cars.repository.CarRepository;
import ru.job4j.cars.repository.CrudRepository;
import ru.job4j.cars.repository.EngineRepository;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class HbnCarRepositoryTest {

    private static CarRepository carRepository;

    private static EngineRepository engineRepository;

    @BeforeAll
    public static void initRepositories() {
        SessionFactory sessionFactory = new HibernateConfiguration().sessionFactory();
        CrudRepository crudRepository = new CrudRepository(sessionFactory);
        engineRepository = new HbnEngineRepository(crudRepository);
        carRepository = new HbnCarRepository(crudRepository);
    }

    @AfterEach
    public void clearTasks() {
        carRepository.findAll().forEach(
                car -> carRepository.deleteById(car.getId())
        );
        engineRepository.findAll().forEach(
                engine -> engineRepository.deleteById(engine.getId())
        );
    }

    @Test
    public void whenDeleteByInvalidIdThenGetFalse() {
        assertThat(carRepository.deleteById(99))
                .isFalse();
    }

    @Test
    public void whenDontSaveThenNothingFoundAndGetFalse() {
        assertThat(carRepository.deleteById(0)).isFalse();
    }

    @Test
    public void whenSaveSeveralThenGetAll() {
        Engine engine = engineRepository.save(Engine.builder()
                .title("test engine title").build()).get();

        Car car1 = Car.builder()
                .title("title 1")
                .engine(engine)
                .build();
        Car car2 = Car.builder()
                .title("title 2")
                .engine(engine)
                .build();
        Car car3 = Car.builder()
                .title("title 3")
                .engine(engine)
                .build();

        List.of(car3, car2, car1)
                .forEach(car -> carRepository.save(car));

        Collection<Car> expected = List.of(car1, car2, car3);

        Collection<Car> carsRepositoryResponse = carRepository.findAll();

        assertTrue(expected.size() == carsRepositoryResponse.size());
        assertTrue(expected.containsAll(carsRepositoryResponse));
    }

}