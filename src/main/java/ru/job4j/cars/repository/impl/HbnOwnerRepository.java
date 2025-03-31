package ru.job4j.cars.repository.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.entity.Owner;
import ru.job4j.cars.repository.CrudRepository;
import ru.job4j.cars.repository.OwnerRepository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Repository
public class HbnOwnerRepository implements OwnerRepository {

    private final CrudRepository crudRepository;

    @Override
    public Optional<Owner> save(Owner owner) {
        try {
            crudRepository.run(session -> session.persist(owner));
            return Optional.of(owner);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public boolean update(Owner owner) {
        return crudRepository.tx(
                session -> session.merge(owner)).equals(owner);
    }

    @Override
    public boolean deleteById(int ownerId) {
        return crudRepository.tx(session ->
                session.createQuery("DELETE Owner o WHERE o.id = :fId")
                        .setParameter("fId", ownerId)
                        .executeUpdate()) > 0;
    }

    @Override
    public Collection<Owner> findAll() {
        return crudRepository.query("FROM Owner", Owner.class);
    }

    @Override
    public Optional<Owner> findById(int ownerId) {
        return crudRepository.optional(
                "FROM Owner o WHERE o.id = :fId", Owner.class,
                Map.of("fId", ownerId)
        );
    }
}
