package ru.job4j.cars.repository.impl.carcomponents;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.configuration.HibernateConfiguration;
import ru.job4j.cars.entity.carcomponents.CarBrand;
import ru.job4j.cars.repository.CrudRepository;
import ru.job4j.cars.repository.RegularRepository;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class HbnCarBrandRepositoryTest {

    private static RegularRepository<CarBrand> carBrandRepository;

    @BeforeAll
    public static void initRepositories() {
        SessionFactory sessionFactory = new HibernateConfiguration().sessionFactory();
        CrudRepository crudRepository = new CrudRepository(sessionFactory);
        carBrandRepository = new HbnCarBrandRepository(crudRepository);
    }

    @AfterEach
    public void clearRepositories() {
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
    public void whenSaveSeveralThenGetAllEntities() {
        CarBrand carBrand1 = CarBrand.builder().title("title 1")
                .build();
        CarBrand carBrand2 = CarBrand.builder().title("title 2")
                .build();
        CarBrand carBrand3 = CarBrand.builder().title("title 3")
                .build();

        List.of(carBrand3, carBrand2, carBrand1)
                .forEach(carBrand -> carBrandRepository.save(carBrand));

        Collection<CarBrand> expected = List.of(carBrand1, carBrand2, carBrand3);

        Collection<CarBrand> ownerRepositoryResponse = carBrandRepository.findAll();

        assertTrue(expected.size() == ownerRepositoryResponse.size());
        assertTrue(expected.containsAll(ownerRepositoryResponse));
    }

    @Test
    public void whenUpdateThenGetRefreshEntity() {
        CarBrand beforeUpdate = carBrandRepository.save(
                CarBrand.builder().title("title before")
                        .build()).get();

        CarBrand afterUpdate = CarBrand.builder()
                .id(beforeUpdate.getId())
                .title("title after")
                .build();

        carBrandRepository.update(afterUpdate);

        assertThat(carBrandRepository.findById(beforeUpdate.getId()).get())
                .isEqualTo(afterUpdate);
    }
}