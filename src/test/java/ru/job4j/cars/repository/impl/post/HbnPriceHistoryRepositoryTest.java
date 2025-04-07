package ru.job4j.cars.repository.impl.post;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.configuration.HibernateConfiguration;
import ru.job4j.cars.entity.post.PriceHistory;
import ru.job4j.cars.repository.CrudRepository;
import ru.job4j.cars.repository.RegularRepository;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class HbnPriceHistoryRepositoryTest {

    private static RegularRepository<PriceHistory> priceHistoryRepository;

    @BeforeAll
    public static void initRepositories() {
        SessionFactory sessionFactory = new HibernateConfiguration().sessionFactory();
        CrudRepository crudRepository = new CrudRepository(sessionFactory);
        priceHistoryRepository = new HbnPriceHistoryRepository(crudRepository);
    }

    @AfterEach
    public void clearRepositories() {
        priceHistoryRepository.findAll().forEach(
                priceHistory -> priceHistoryRepository.deleteById(priceHistory.getId())
        );
    }

    @Test
    public void whenDeleteByInvalidIdThenGetFalse() {
        assertThat(priceHistoryRepository.deleteById(99))
                .isFalse();
    }

    @Test
    public void whenDontSaveThenNothingFoundAndGetFalse() {
        assertThat(priceHistoryRepository.deleteById(0)).isFalse();
    }

    @Test
    public void whenSaveSeveralThenGetAllEntities() {
        PriceHistory history1 = PriceHistory.builder().after(1_000_000).build();
        PriceHistory history2 = PriceHistory.builder().after(2_000_000).build();
        PriceHistory history3 = PriceHistory.builder().after(2_999_999).build();

        List.of(history3, history2, history1)
                .forEach(carBrand -> priceHistoryRepository.save(carBrand));

        Collection<PriceHistory> expected = List.of(history1, history2, history3);

        Collection<PriceHistory> priceHistReposResponse = priceHistoryRepository.findAll();

        assertTrue(expected.size() == priceHistReposResponse.size());
        assertTrue(expected.containsAll(priceHistReposResponse));
    }

    @Test
    public void whenUpdateThenGetRefreshEntity() {
        PriceHistory beforeUpdate = priceHistoryRepository.save(
                PriceHistory.builder().after(3_000_099)
                        .build()).get();

        PriceHistory afterUpdate = PriceHistory.builder()
                .id(beforeUpdate.getId())
                .after(4_000_099)
                .build();

        priceHistoryRepository.update(afterUpdate);

        assertThat(priceHistoryRepository.findById(beforeUpdate.getId()).get())
                .isEqualTo(afterUpdate);
    }

}