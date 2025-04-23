package ru.job4j.cars.repository;

import ru.job4j.cars.entity.post.Post;

import java.util.Collection;

public interface PostRepository extends RegularRepository<Post> {

    Collection<Post> findAllByUserId(int userId);

    Collection<Post> findByCarBrand(int carBrandId);

    Collection<Post> findWithPhoto();

    Collection<Post> findByLastWeek();

    boolean updateStatusById(int id, boolean isSold);

}
