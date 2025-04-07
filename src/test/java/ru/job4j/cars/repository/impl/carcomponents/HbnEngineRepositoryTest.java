package ru.job4j.cars.repository.impl.carcomponents;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.configuration.HibernateConfiguration;
import ru.job4j.cars.entity.carcomponents.Engine;
import ru.job4j.cars.repository.CrudRepository;
import ru.job4j.cars.repository.RegularRepository;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class HbnEngineRepositoryTest {

    private static RegularRepository<Engine> engineRepository;

    @BeforeAll
    public static void initRepositories() {
        SessionFactory sessionFactory = new HibernateConfiguration().sessionFactory();
        CrudRepository crudRepository = new CrudRepository(sessionFactory);
        engineRepository = new HbnEngineRepository(crudRepository);
    }

    @AfterEach
    public void clearRepositories() {
        engineRepository.findAll().forEach(
                carBrand -> engineRepository.deleteById(carBrand.getId())
        );
    }

    @Test
    public void whenDeleteByInvalidIdThenGetFalse() {
        assertThat(engineRepository.deleteById(99))
                .isFalse();
    }

    @Test
    public void whenDontSaveThenNothingFoundAndGetFalse() {
        assertThat(engineRepository.deleteById(0)).isFalse();
    }

    @Test
    public void whenSaveSeveralThenGetAllEntities() {
        Engine engine1 = Engine.builder().title("title 1")
                .build();
        Engine engine2 = Engine.builder().title("title 2")
                .build();
        Engine engine3 = Engine.builder().title("title 3")
                .build();

        List.of(engine3, engine2, engine1)
                .forEach(carBrand -> engineRepository.save(carBrand));

        Collection<Engine> expected = List.of(engine1, engine2, engine3);

        Collection<Engine> ownerRepositoryResponse = engineRepository.findAll();

        assertTrue(expected.size() == ownerRepositoryResponse.size());
        assertTrue(expected.containsAll(ownerRepositoryResponse));
    }

    @Test
    public void whenUpdateThenGetRefreshEntity() {
        Engine beforeUpdate = engineRepository.save(
                Engine.builder().title("title before")
                        .build()).get();

        Engine afterUpdate = Engine.builder()
                .id(beforeUpdate.getId())
                .title("title after")
                .build();

        engineRepository.update(afterUpdate);

        assertThat(engineRepository.findById(beforeUpdate.getId()).get())
                .isEqualTo(afterUpdate);
    }
}