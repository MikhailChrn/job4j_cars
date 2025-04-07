package ru.job4j.cars.repository.impl.carhistory;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.entity.carhistory.CarOwnerHistory;
import ru.job4j.cars.repository.CrudRepository;
import ru.job4j.cars.repository.RegularRepository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Repository
public class HbnCarOwnerHistoryRepository
        implements RegularRepository<CarOwnerHistory> {

    private final CrudRepository crudRepository;

    @Override
    public Optional<CarOwnerHistory> save(CarOwnerHistory carOwnerHistory) {
        try {
            crudRepository.run(session -> session.persist(carOwnerHistory));
            return Optional.of(carOwnerHistory);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public boolean update(CarOwnerHistory carOwnerHistory) {
        return crudRepository.tx(
                        session -> session.merge(carOwnerHistory))
                .equals(carOwnerHistory);
    }

    @Override
    public boolean deleteById(int id) {
        return crudRepository.tx(session ->
                session.createQuery("""
                                DELETE CarOwnerHistory coh
                                 WHERE coh.id = :fId
                                """)
                        .setParameter("fId", id)
                        .executeUpdate()) > 0;
    }

    @Override
    public Collection<CarOwnerHistory> findAll() {
        return crudRepository.query("""
                FROM CarOwnerHistory coh
                LEFT JOIN FETCH coh.owner
                """,
                CarOwnerHistory.class);
    }

    @Override
    public Optional<CarOwnerHistory> findById(int id) {
        return crudRepository.optional("""
                FROM CarOwnerHistory coh
                LEFT JOIN FETCH coh.owner
                WHERE coh.id = :fId
                """, CarOwnerHistory.class,
                Map.of("fId", id)
        );
    }
}
