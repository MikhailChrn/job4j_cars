package ru.job4j.cars.repository.impl.carhistory;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.configuration.HibernateConfiguration;
import ru.job4j.cars.entity.carhistory.Owner;
import ru.job4j.cars.repository.CrudRepository;
import ru.job4j.cars.repository.RegularRepository;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class HbnOwnerRepositoryTest {

    private static RegularRepository<Owner> ownerRepository;

    @BeforeAll
    public static void initRepositories() {
        SessionFactory sessionFactory = new HibernateConfiguration().sessionFactory();
        CrudRepository crudRepository = new CrudRepository(sessionFactory);
        ownerRepository = new HbnOwnerRepository(crudRepository);
    }

    @AfterEach
    public void clearRepositories() {
        ownerRepository.findAll().forEach(
                owner -> ownerRepository.deleteById(owner.getId())
        );
    }

    @Test
    public void whenDeleteByInvalidIdThenGetFalse() {
        assertThat(ownerRepository.deleteById(99))
                .isFalse();
    }

    @Test
    public void whenDontSaveThenNothingFoundAndGetFalse() {
        assertThat(ownerRepository.deleteById(0)).isFalse();
    }

    @Test
    public void whenSaveSeveralThenGetAllEntities() {
        Owner owner1 = Owner.builder().name("name 1")
                .build();
        Owner owner2 = Owner.builder().name("name 2")
                .build();
        Owner owner3 = Owner.builder().name("name 3")
                .build();

        List.of(owner3, owner2, owner1)
                .forEach(owner -> ownerRepository.save(owner));

        Collection<Owner> expected = List.of(owner1, owner2, owner3);

        Collection<Owner> ownerRepositoryResponse = ownerRepository.findAll();

        assertTrue(expected.size() == ownerRepositoryResponse.size());
        assertTrue(expected.containsAll(ownerRepositoryResponse));
    }

    @Test
    public void whenUpdateThenGetRefreshEntity() {
        Owner beforeUpdate = ownerRepository.save(
                Owner.builder().name("name before")
                        .build()).get();

        Owner afterUpdate = Owner.builder()
                .id(beforeUpdate.getId())
                .name("name after")
                .build();

        ownerRepository.update(afterUpdate);

        assertThat(ownerRepository.findById(beforeUpdate.getId()).get())
                .isEqualTo(afterUpdate);
    }
}