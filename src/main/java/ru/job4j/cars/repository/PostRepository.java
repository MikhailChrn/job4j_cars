package ru.job4j.cars.repository;

import ru.job4j.cars.entity.Post;

import java.util.Collection;

public interface PostRepository extends RegularRepository<Post> {

    Collection<Post> findByCarBrand(int carBrandId);

    Collection<Post> findWithPhoto();

    Collection<Post> findByLastDay();

}
