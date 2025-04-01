package ru.job4j.cars.repository;

import ru.job4j.cars.entity.Post;

import java.util.Collection;
import java.util.Optional;

public interface PostRepository {

    Optional<Post> save(Post post);

    boolean update(Post post);

    boolean deleteById(int postId);

    Collection<Post> findAll();

    Optional<Post> findById(int postId);

    Collection<Post> findByCarBrand(int carBrandId);

    Collection<Post> findWithPhoto();

    Collection<Post> findByLastDay();

}
