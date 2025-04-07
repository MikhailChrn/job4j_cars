package ru.job4j.cars.repository.impl.carhistory;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.configuration.HibernateConfiguration;
import ru.job4j.cars.entity.carhistory.CarOwnerHistory;
import ru.job4j.cars.entity.carhistory.Owner;
import ru.job4j.cars.repository.CrudRepository;
import ru.job4j.cars.repository.RegularRepository;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class HbnCarOwnerHistoryRepositoryTest {

    private static RegularRepository<Owner> ownerRepository;

    private static RegularRepository<CarOwnerHistory> carOwnerHistoryRepository;

    @BeforeAll
    public static void initRepositories() {
        SessionFactory sessionFactory = new HibernateConfiguration().sessionFactory();
        CrudRepository crudRepository = new CrudRepository(sessionFactory);
        ownerRepository = new HbnOwnerRepository(crudRepository);
        carOwnerHistoryRepository = new HbnCarOwnerHistoryRepository(crudRepository);
    }

    @AfterEach
    public void clearRepositories() {
        carOwnerHistoryRepository.findAll().forEach(
                history -> carOwnerHistoryRepository.deleteById(history.getId())
        );
        ownerRepository.findAll().forEach(
                owner -> ownerRepository.deleteById(owner.getId())
        );
    }

    @Test
    public void whenDeleteByInvalidIdThenGetFalse() {
        assertThat(carOwnerHistoryRepository.deleteById(99))
                .isFalse();
    }

    @Test
    public void whenDontSaveThenNothingFoundAndGetFalse() {
        assertThat(carOwnerHistoryRepository.deleteById(0)).isFalse();
    }

    @Test
    public void whenSaveSeveralThenGetAllEntities() {
        Owner owner1 = ownerRepository.save(
                Owner.builder().name("name 1")
                .build()).get();
        Owner owner2 = ownerRepository.save(
                Owner.builder().name("name 2")
                .build()).get();
        Owner owner3 = ownerRepository.save(
                Owner.builder().name("name 3")
                .build()).get();

        Collection<Owner> expectedOwners = List.of(owner3, owner2, owner1);

        expectedOwners.forEach(
                owner -> carOwnerHistoryRepository.save(
                        CarOwnerHistory.builder().owner(owner).build()
                ));

        Collection<Owner> historyRepositoryResponse =
                carOwnerHistoryRepository.findAll().stream()
                        .map(CarOwnerHistory::getOwner)
                        .toList();

        assertTrue(expectedOwners.size() == historyRepositoryResponse.size());
        assertTrue(expectedOwners.containsAll(historyRepositoryResponse));
    }

    @Test
    public void whenUpdateThenGetRefreshEntity() {
        Owner ownerBefore = ownerRepository.save(Owner.builder()
                .name("name before").build()).get();
        Owner ownerAfter = ownerRepository.save(Owner.builder()
                .name("name after").build()).get();

        CarOwnerHistory beforeUpdate = carOwnerHistoryRepository.save(
                CarOwnerHistory.builder().owner(ownerBefore).build()).get();

        CarOwnerHistory afterUpdate = CarOwnerHistory.builder()
                .id(beforeUpdate.getId()).owner(ownerAfter).build();

        carOwnerHistoryRepository.update(afterUpdate);

        assertThat(carOwnerHistoryRepository.findById(beforeUpdate.getId()).get().getOwner())
                .isEqualTo(ownerAfter);
    }

}