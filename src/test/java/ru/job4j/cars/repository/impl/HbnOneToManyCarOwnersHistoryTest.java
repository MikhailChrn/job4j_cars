package ru.job4j.cars.repository.impl;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.configuration.HibernateConfiguration;
import ru.job4j.cars.entity.*;
import ru.job4j.cars.repository.CrudRepository;
import ru.job4j.cars.repository.RegularRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HbnOneToManyCarOwnersHistoryTest {

    private static RegularRepository<Owner> ownerRepository;

    private static RegularRepository<CarBrand> carBrandRepository;

    private static RegularRepository<Car> carRepository;

    private static RegularRepository<CarOwnerHistory> carOwnerHistoryRepository;

    @BeforeAll
    public static void initRepositories() {
        SessionFactory sessionFactory = new HibernateConfiguration().sessionFactory();
        CrudRepository crudRepository = new CrudRepository(sessionFactory);
        ownerRepository = new HbnOwnerRepository(crudRepository);
        carBrandRepository = new HbnCarBrandRepository(crudRepository);
        carRepository = new HbnCarRepository(crudRepository);
        carOwnerHistoryRepository = new HbnCarOwnerHistoryRepository(crudRepository);
    }

    @AfterEach
    public void clearRepositories() {
        carOwnerHistoryRepository.findAll().forEach(
                carOwnerHistory ->
                        carOwnerHistoryRepository.deleteById(carOwnerHistory.getId())
        );
        carRepository.findAll().forEach(
                car -> carRepository.deleteById(car.getId())
        );
        ownerRepository.findAll().forEach(
                owner -> ownerRepository.deleteById(owner.getId())
        );
        carBrandRepository.findAll().forEach(
                carBrand -> carBrandRepository.deleteById(carBrand.getId())
        );
    }

    @Test
    public void whenCreateEmptyCarWithoutOwners() {
        CarBrand carBrand = carBrandRepository.save(CarBrand.builder()
                .title("default carBrand")
                .build()).get();

        Car carBeforeRepo = carRepository.save(new Car("test car", carBrand)).get();

        Car carAfterRepo = carRepository.findById(carBeforeRepo.getId()).get();

        assertThat(carAfterRepo).isEqualTo(carBeforeRepo);
    }

    @Test
    public void whenCreateCarWithOwnersListThenGetCorrectRepositoryResponse() {
        LocalDateTime localDateTime = LocalDateTime.now();

        Owner owner1 = ownerRepository.save(Owner.builder()
                .name("name1").build()).get();
        Owner owner2 = ownerRepository.save(Owner.builder()
                .name("name2").build()).get();
        Owner owner3 = ownerRepository.save(Owner.builder()
                .name("name3").build()).get();
        CarBrand carBrand = carBrandRepository.save(CarBrand.builder()
                .title("default carBrand").build()).get();

        Car car = carRepository.save(new Car("title", carBrand)).get();

        CarOwnerHistory carOwnerHistory1 = carOwnerHistoryRepository.save(CarOwnerHistory.builder()
                .owner(owner1).startAt(localDateTime.minusYears(5))
                .endAt(localDateTime.minusYears(4)).build()).get();
        CarOwnerHistory carOwnerHistory2 = carOwnerHistoryRepository.save(CarOwnerHistory.builder()
                .owner(owner2).startAt(localDateTime.minusYears(4))
                .endAt(localDateTime.minusYears(3)).build()).get();
        CarOwnerHistory carOwnerHistory3 = carOwnerHistoryRepository.save(CarOwnerHistory.builder()
                .owner(owner3).startAt(localDateTime.minusYears(3))
                .endAt(localDateTime.minusYears(2)).build()).get();

        Collection<CarOwnerHistory> expHistoryList =
                List.of(carOwnerHistory3, carOwnerHistory2, carOwnerHistory1);
        expHistoryList.forEach(car::addCarOwnerHistory);

        carRepository.update(car);

        Collection<CarOwnerHistory> responseRepository =
                carRepository.findById(car.getId()).get().getCarOwnerHistories();

        assertThat(responseRepository.size()).isEqualTo(expHistoryList.size());
        assertTrue(responseRepository.containsAll(expHistoryList));
    }
}
