package ru.job4j.cars.repository.impl;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.configuration.HibernateConfiguration;
import ru.job4j.cars.entity.User;
import ru.job4j.cars.repository.CrudRepository;
import ru.job4j.cars.repository.RegularRepository;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HbnUserRepositoryTest {

    private static RegularRepository<User> userRepository;

    @BeforeAll
    public static void initRepositories() {
        SessionFactory sessionFactory = new HibernateConfiguration().sessionFactory();
        CrudRepository crudRepository = new CrudRepository(sessionFactory);
        userRepository = new HbnUserRepository(crudRepository);
    }

    @AfterEach
    public void clearRepositories() {
        userRepository.findAll().forEach(
                user -> userRepository.deleteById(user.getId())
        );
    }

    @Test
    public void whenDeleteByInvalidIdThenGetFalse() {
        assertThat(userRepository.deleteById(99))
                .isFalse();
    }

    @Test
    public void whenDontSaveThenNothingFoundAndGetFalse() {
        assertThat(userRepository.deleteById(0)).isFalse();
    }

    @Test
    public void whenSaveSeveralThenGetAllEntities() {
        User user1 = User.builder().name("name 1")
                .login("login 1")
                .password("password 1").build();
        User user2 = User.builder().name("name 2")
                .login("login 2")
                .password("password 2").build();
        User user3 = User.builder().name("name 3")
                .login("login 3")
                .password("password 3").build();

        List.of(user1, user2, user3)
                .forEach(user -> userRepository.save(user));

        Collection<User> expected = List.of(user1, user2, user3);

        Collection<User> userRepositoryResponse = userRepository.findAll();

        assertTrue(expected.size() == userRepositoryResponse.size());
        assertTrue(expected.containsAll(userRepositoryResponse));
    }

    @Test
    public void whenUpdateThenGetRefreshEntity() {
        User beforeUpdate = userRepository.save(
                User.builder().name("name before")
                .login("login before")
                .password("password").build()).get();

        User afterUpdate = User.builder()
                .id(beforeUpdate.getId())
                .name("name after")
                .login("login after")
                .password("password").build();

        userRepository.update(afterUpdate);

        assertThat(userRepository.findById(beforeUpdate.getId()).get())
                .isEqualTo(afterUpdate);
    }
}