package ru.job4j.cars.repository.impl;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.configuration.HibernateConfiguration;
import ru.job4j.cars.entity.BodyType;
import ru.job4j.cars.repository.CrudRepository;
import ru.job4j.cars.repository.RegularRepository;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class HbnBodyTypeRepositoryTest {

    private static RegularRepository<BodyType> bodyTypeRepository;

    @BeforeAll
    public static void initRepositories() {
        SessionFactory sessionFactory = new HibernateConfiguration().sessionFactory();
        CrudRepository crudRepository = new CrudRepository(sessionFactory);
        bodyTypeRepository = new HbnBodyTypeRepository(crudRepository);
    }

    @AfterEach
    public void clearRepositories() {
        bodyTypeRepository.findAll().forEach(
                bodyType -> bodyTypeRepository.deleteById(bodyType.getId())
        );
    }

    @Test
    public void whenDeleteByInvalidIdThenGetFalse() {
        assertThat(bodyTypeRepository.deleteById(99))
                .isFalse();
    }

    @Test
    public void whenDontSaveThenNothingFoundAndGetFalse() {
        assertThat(bodyTypeRepository.deleteById(0)).isFalse();
    }

    @Test
    public void whenSaveSeveralThenGetAllEntities() {
        BodyType bodyType1 = BodyType.builder().title("title 1")
                .build();
        BodyType bodyType2 = BodyType.builder().title("title 2")
                .build();
        BodyType carBrand3 = BodyType.builder().title("title 3")
                .build();

        List.of(carBrand3, bodyType2, bodyType1)
                .forEach(carBrand -> bodyTypeRepository.save(carBrand));

        Collection<BodyType> expected = List.of(bodyType1, bodyType2, carBrand3);

        Collection<BodyType> ownerRepositoryResponse = bodyTypeRepository.findAll();

        assertTrue(expected.size() == ownerRepositoryResponse.size());
        assertTrue(expected.containsAll(ownerRepositoryResponse));
    }

    @Test
    public void whenUpdateThenGetRefreshEntity() {
        BodyType beforeUpdate = bodyTypeRepository.save(
                BodyType.builder().title("title before")
                        .build()).get();

        BodyType afterUpdate = BodyType.builder()
                .id(beforeUpdate.getId())
                .title("title after")
                .build();

        bodyTypeRepository.update(afterUpdate);

        assertThat(bodyTypeRepository.findById(beforeUpdate.getId()).get())
                .isEqualTo(afterUpdate);
    }

}