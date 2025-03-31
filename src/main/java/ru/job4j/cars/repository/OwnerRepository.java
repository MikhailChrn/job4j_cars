package ru.job4j.cars.repository;

import ru.job4j.cars.entity.Owner;

import java.util.Collection;
import java.util.Optional;

public interface OwnerRepository {

    Optional<Owner> save(Owner owner);

    boolean update(Owner owner);

    boolean deleteById(int ownerId);

    Collection<Owner> findAll();

    Optional<Owner> findById(int ownerId);

}
