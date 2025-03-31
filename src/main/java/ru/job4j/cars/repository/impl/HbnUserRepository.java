package ru.job4j.cars.repository.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.entity.User;
import ru.job4j.cars.repository.CrudRepository;
import ru.job4j.cars.repository.UserRepository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Repository
public class HbnUserRepository implements UserRepository {

    private final CrudRepository crudRepository;

    @Override
    public Optional<User> save(User user) {
        try {
            crudRepository.run(session -> session.persist(user));
            return Optional.of(user);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public boolean update(User user) {
        return crudRepository.tx(
                session -> session.merge(user)).equals(user);
    }

    @Override
    public boolean deleteById(int userId) {
        return crudRepository.tx(session ->
                session.createQuery("DELETE User u WHERE u.id = :fId")
                        .setParameter("fId", userId)
                        .executeUpdate()) > 0;
    }

    @Override
    public Collection<User> findAll() {
        return crudRepository.query("FROM User", User.class);
    }

    @Override
    public Optional<User> findById(int userId) {
        return crudRepository.optional(
                "FROM User u WHERE u.id = :fId", User.class,
                Map.of("fId", userId)
        );
    }
}
