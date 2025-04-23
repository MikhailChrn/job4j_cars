package ru.job4j.cars.repository;

import ru.job4j.cars.entity.User;

import java.util.Optional;

public interface UserRepository extends RegularRepository<User> {

    Optional<User> findByLoginAndPassword(String login, String password);

}
