package ru.job4j.cars.repository;


import ru.job4j.cars.entity.post.PriceHistory;

import java.util.Collection;

public interface PriceHistoryRepository extends RegularRepository<PriceHistory> {

    Collection<PriceHistory> findAllByPostId(int postId);

}
