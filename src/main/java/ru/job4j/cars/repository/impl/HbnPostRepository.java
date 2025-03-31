package ru.job4j.cars.repository.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.entity.Post;
import ru.job4j.cars.repository.CrudRepository;
import ru.job4j.cars.repository.PostRepository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Repository
public class HbnPostRepository implements PostRepository {

    private final CrudRepository crudRepository;

    @Override
    public Optional<Post> save(Post post) {
        try {
            crudRepository.run(session -> session.persist(post));
            return Optional.of(post);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public boolean update(Post post) {
        return crudRepository.tx(
                session -> session.merge(post)).equals(post);
    }

    @Override
    public boolean deleteById(int postId) {
        return crudRepository.tx(session ->
                session.createQuery("DELETE Post p WHERE p.id = :fId")
                        .setParameter("fId", postId)
                        .executeUpdate()) > 0;
    }

    @Override
    public Collection<Post> findAll() {
        return crudRepository.query("FROM Post", Post.class);
    }

    @Override
    public Optional<Post> findById(int postId) {
        return crudRepository.optional(
                "FROM Post p WHERE p.id = :fId", Post.class,
                Map.of("fId", postId)
        );
    }
}
