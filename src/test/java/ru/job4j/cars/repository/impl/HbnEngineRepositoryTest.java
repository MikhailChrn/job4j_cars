package ru.job4j.cars.repository.impl;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.configuration.HibernateConfiguration;
import ru.job4j.cars.entity.Engine;
import ru.job4j.cars.repository.CrudRepository;
import ru.job4j.cars.repository.EngineRepository;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class HbnEngineRepositoryTest {

    private static EngineRepository engineRepository;

    @BeforeAll
    public static void initRepositories() {
        SessionFactory sessionFactory = new HibernateConfiguration().sessionFactory();
        CrudRepository crudRepository = new CrudRepository(sessionFactory);
        engineRepository = new HbnEngineRepository(crudRepository);
    }

    @AfterEach
    public void clearTasks() {
        engineRepository.findAll().forEach(
                engine -> engineRepository.deleteById(engine.getId())
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
    public void whenSaveSeveralThenGetAll() {
        Engine engine1 = Engine.builder().title("title 1")
                .build();
        Engine engine2 = Engine.builder().title("title 2")
                .build();
        Engine engine3 = Engine.builder().title("title 3")
                .build();

        List.of(engine3, engine2, engine1)
                .forEach(owner -> engineRepository.save(owner));

        Collection<Engine> expected = List.of(engine1, engine2, engine3);

        Collection<Engine> engineRepositoryResponse = engineRepository.findAll();

        assertTrue(expected.size() == engineRepositoryResponse.size());
        assertTrue(expected.containsAll(engineRepositoryResponse));
    }
}