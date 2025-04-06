package ru.job4j.cars.repository.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.entity.BodyType;
import ru.job4j.cars.repository.CrudRepository;
import ru.job4j.cars.repository.RegularRepository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Repository
public class HbnBodyTypeRepository implements RegularRepository<BodyType> {

    private final CrudRepository crudRepository;

    @Override
    public Optional<BodyType> save(BodyType bodyType) {
        try {
            crudRepository.run(session -> session.persist(bodyType));
            return Optional.of(bodyType);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public boolean update(BodyType bodyType) {
        return crudRepository.tx(
                session -> session.merge(bodyType)).equals(bodyType);
    }

    @Override
    public boolean deleteById(int id) {
        return crudRepository.tx(session ->
                session.createQuery("DELETE BodyType b WHERE b.id = :fId")
                        .setParameter("fId", id)
                        .executeUpdate()) > 0;
    }

    @Override
    public Collection<BodyType> findAll() {
        return crudRepository.query("FROM BodyType", BodyType.class);
    }

    @Override
    public Optional<BodyType> findById(int id) {
        return crudRepository.optional(
                "FROM BodyType b WHERE b.id = :fId", BodyType.class,
                Map.of("fId", id)
        );
    }
}
