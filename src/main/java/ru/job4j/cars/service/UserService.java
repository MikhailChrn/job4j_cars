package ru.job4j.cars.service;

import ru.job4j.cars.dto.ZoneDto;
import ru.job4j.cars.entity.User;

import java.util.Collection;
import java.util.Optional;

public interface UserService {

    Optional<User> save(User user);

    Optional<User> findByLoginAndPassword(String login, String password);

    Collection<ZoneDto> getAllZones();

}
