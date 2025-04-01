package ru.job4j.cars.repository.impl;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.configuration.HibernateConfiguration;
import ru.job4j.cars.entity.CarBrand;
import ru.job4j.cars.repository.CarBrandRepository;
import ru.job4j.cars.repository.CrudRepository;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class HbnCarBrandRepositoryTest {

    private static CarBrandRepository carBrandRepository;

    @BeforeAll
    public static void initRepositories() {
        SessionFactory sessionFactory = new HibernateConfiguration().sessionFactory();
        CrudRepository crudRepository = new CrudRepository(sessionFactory);
        carBrandRepository = new HbnCarBrandRepository(crudRepository);
    }

    @AfterEach
    public void clearTasks() {
        carBrandRepository.findAll().forEach(
                carBrand -> carBrandRepository.deleteById(carBrand.getId())
        );
    }

    @Test
    public void whenDeleteByInvalidIdThenGetFalse() {
        assertThat(carBrandRepository.deleteById(99))
                .isFalse();
    }

    @Test
    public void whenDontSaveThenNothingFoundAndGetFalse() {
        assertThat(carBrandRepository.deleteById(0)).isFalse();
    }

    @Test
    public void whenSaveSeveralThenGetAll() {
        CarBrand carBrand1 = CarBrand.builder().title("title 1")
                .build();
        CarBrand carBrand2 = CarBrand.builder().title("title 2")
                .build();
        CarBrand carBran3 = CarBrand.builder().title("title 3")
                .build();

        List.of(carBran3, carBrand2, carBrand1)
                .forEach(carBrand -> carBrandRepository.save(carBrand));

        Collection<CarBrand> expected = List.of(carBrand1, carBrand2, carBran3);

        Collection<CarBrand> engineRepositoryResponse = carBrandRepository.findAll();

        assertTrue(expected.size() == engineRepositoryResponse.size());
        assertTrue(expected.containsAll(engineRepositoryResponse));
    }

}