package ru.job4j.cars.repository.impl;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.configuration.HibernateConfiguration;
import ru.job4j.cars.entity.BodyType;
import ru.job4j.cars.entity.Car;
import ru.job4j.cars.entity.CarBrand;
import ru.job4j.cars.entity.Engine;
import ru.job4j.cars.repository.CrudRepository;
import ru.job4j.cars.repository.RegularRepository;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class HbnCarRepositoryTest {

    private static RegularRepository<CarBrand> carBrandRepository;

    private static RegularRepository<BodyType> bodyTypeRepository;

    private static RegularRepository<Engine> engineRepository;

    private static RegularRepository<Car> carRepository;

    @BeforeAll
    public static void initRepositories() {
        SessionFactory sessionFactory = new HibernateConfiguration().sessionFactory();
        CrudRepository crudRepository = new CrudRepository(sessionFactory);
        carBrandRepository = new HbnCarBrandRepository(crudRepository);
        bodyTypeRepository = new HbnBodyTypeRepository(crudRepository);
        engineRepository = new HbnEngineRepository(crudRepository);
        carRepository = new HbnCarRepository(crudRepository);
    }

    @AfterEach
    public void clearRepositories() {
        carRepository.findAll().forEach(
                car -> carRepository.deleteById(car.getId())
        );
        carBrandRepository.findAll().forEach(
                car -> carBrandRepository.deleteById(car.getId())
        );
        bodyTypeRepository.findAll().forEach(
                car -> bodyTypeRepository.deleteById(car.getId())
        );
        engineRepository.findAll().forEach(
                car -> engineRepository.deleteById(car.getId())
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
    public void whenSaveSeveralThenGetAllEntities() {
        CarBrand carBrand = carBrandRepository.save(CarBrand.builder()
                .title("test carBrand title").build()).get();
        BodyType bodyType = bodyTypeRepository.save(BodyType.builder()
                .title("test bodyType title").build()).get();
        Engine engine = engineRepository.save(Engine.builder()
                .title("test engine title").build()).get();

        Car car1 = Car.builder()
                .title("title 1")
                .carBrand(carBrand)
                .bodyType(bodyType)
                .engine(engine)
                .build();
        Car car2 = Car.builder()
                .title("title 2")
                .carBrand(carBrand)
                .bodyType(bodyType)
                .engine(engine)
                .build();
        Car car3 = Car.builder()
                .title("title 3")
                .carBrand(carBrand)
                .bodyType(bodyType)
                .engine(engine)
                .build();

        List.of(car3, car2, car1)
                .forEach(car -> carRepository.save(car));

        Collection<Car> expected = List.of(car1, car2, car3);

        Collection<Car> carsRepositoryResponse = carRepository.findAll();

        assertTrue(expected.size() == carsRepositoryResponse.size());
        assertTrue(expected.containsAll(carsRepositoryResponse));
    }

    @Test
    public void whenUpdateThenGetRefreshEntity() {
        CarBrand carBrand1 = carBrandRepository.save(CarBrand.builder().title("carBrand1").build()).get();
        CarBrand carBrand2 = carBrandRepository.save(CarBrand.builder().title("carBrand2").build()).get();
        BodyType bodyType1 = bodyTypeRepository.save(BodyType.builder().title("bodyType1").build()).get();
        BodyType bodyType2 = bodyTypeRepository.save(BodyType.builder().title("bodyType2").build()).get();
        Engine engine1 = engineRepository.save(Engine.builder().title("engine1").build()).get();
        Engine engine2 = engineRepository.save(Engine.builder().title("engine2").build()).get();

        Car beforeUpdate = carRepository.save(
                Car.builder().title("title before")
                        .carBrand(carBrand1).bodyType(bodyType1)
                        .engine(engine1).build()).get();

        assertThat(carRepository.findById(beforeUpdate.getId()).get().getTitle())
                .isEqualTo("title before");

        Car afterUpdate = Car.builder()
                .id(beforeUpdate.getId())
                .title("title after")
                .carBrand(carBrand2)
                .bodyType(bodyType2)
                .engine(engine2)
                .build();

        carRepository.update(afterUpdate);

        assertThat(carRepository.findById(beforeUpdate.getId()).get())
                .isEqualTo(afterUpdate);
        assertThat(carRepository.findById(beforeUpdate.getId()).get().getTitle())
                .isEqualTo("title after");
        assertThat(carRepository.findById(beforeUpdate.getId()).get().getCarBrand())
                .isEqualTo(carBrand2);
        assertThat(carRepository.findById(beforeUpdate.getId()).get().getBodyType())
                .isEqualTo(bodyType2);
        assertThat(carRepository.findById(beforeUpdate.getId()).get().getEngine())
                .isEqualTo(engine2);
    }

}