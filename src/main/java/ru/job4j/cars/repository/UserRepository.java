package ru.job4j.cars.repository;

import ru.job4j.cars.entity.User;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository {

    Optional<User> save(User user);

    boolean update(User user);

    boolean deleteById(int userId);

    Collection<User> findAll();

    Optional<User> findById(int userId);

}
