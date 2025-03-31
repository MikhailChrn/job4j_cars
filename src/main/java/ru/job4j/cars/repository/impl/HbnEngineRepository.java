package ru.job4j.cars.repository.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.entity.Engine;
import ru.job4j.cars.repository.CrudRepository;
import ru.job4j.cars.repository.EngineRepository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Repository
public class HbnEngineRepository implements EngineRepository {

    private final CrudRepository crudRepository;

    @Override
    public Optional<Engine> save(Engine engine) {
        try {
            crudRepository.run(session -> session.persist(engine));
            return Optional.of(engine);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public boolean update(Engine engine) {
        return crudRepository.tx(
                session -> session.merge(engine)).equals(engine);
    }

    @Override
    public boolean deleteById(int engineId) {
        return crudRepository.tx(session ->
                session.createQuery("DELETE Engine e WHERE e.id = :fId")
                        .setParameter("fId", engineId)
                        .executeUpdate()) > 0;
    }

    @Override
    public Collection<Engine> findAll() {
        return crudRepository.query("FROM Engine", Engine.class);
    }

    @Override
    public Optional<Engine> findById(int engineId) {
        return crudRepository.optional(
                "FROM Engine e WHERE e.id = :fId", Engine.class,
                Map.of("fId", engineId)
        );
    }
}
