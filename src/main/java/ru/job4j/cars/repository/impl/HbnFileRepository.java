package ru.job4j.cars.repository.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.entity.File;
import ru.job4j.cars.repository.CrudRepository;
import ru.job4j.cars.repository.FileRepository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
@AllArgsConstructor
public class HbnFileRepository implements FileRepository {

    private final AtomicInteger nextId = new AtomicInteger(0);

    private final Map<Integer, File> files = new ConcurrentHashMap<>();

    private final CrudRepository crudRepository;

    @Override
    public Optional<File> save(File file) {
        try {
            crudRepository.run(session -> session.persist(file));
            return Optional.of(file);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public Optional<File> findById(int fileId) {
        return crudRepository.optional(
                "FROM File f WHERE f.id = :fId", File.class,
                Map.of("fId", fileId)
        );
    }

    @Override
    public boolean deleteById(int fileId) {
        return crudRepository.tx(session ->
                session.createQuery("DELETE File f WHERE f.id = :fId")
                        .setParameter("fId", fileId)
                        .executeUpdate()) > 0;
    }
}
