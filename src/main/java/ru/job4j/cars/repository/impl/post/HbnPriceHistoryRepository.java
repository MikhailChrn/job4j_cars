package ru.job4j.cars.repository.impl.post;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.entity.post.PriceHistory;
import ru.job4j.cars.repository.CrudRepository;
import ru.job4j.cars.repository.RegularRepository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class HbnPriceHistoryRepository implements RegularRepository<PriceHistory> {

    private final CrudRepository crudRepository;

    @Override
    public Optional<PriceHistory> save(PriceHistory priceHistory) {
        try {
            crudRepository.run(session -> session.persist(priceHistory));
            return Optional.of(priceHistory);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public boolean update(PriceHistory priceHistory) {
        return crudRepository.tx(
                session -> session.merge(priceHistory)).equals(priceHistory);
    }

    @Override
    public boolean deleteById(int id) {
        return crudRepository.tx(session ->
                session.createQuery("DELETE PriceHistory ph WHERE ph.id = :fId")
                        .setParameter("fId", id)
                        .executeUpdate()) > 0;
    }

    @Override
    public Collection<PriceHistory> findAll() {
        return crudRepository.query("FROM PriceHistory", PriceHistory.class);
    }

    @Override
    public Optional<PriceHistory> findById(int id) {
        return crudRepository.optional(
                "FROM PriceHistory ph WHERE ph.id = :fId", PriceHistory.class,
                Map.of("fId", id)
        );
    }
}
