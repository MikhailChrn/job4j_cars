package ru.job4j.cars.repository;

import ru.job4j.cars.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    public User create(User user);

    public void update(User user);

    public void delete(int userId);

    public List<User> findAllOrderById();

    public Optional<User> findById(int userId);

    public List<User> findByLikeLogin(String key);

    public Optional<User> findByLogin(String login);

}
