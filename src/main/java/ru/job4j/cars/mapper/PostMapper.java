package ru.job4j.cars.mapper;

import ru.job4j.cars.dto.PostFullViewDto;
import ru.job4j.cars.dto.PostShortViewDto;
import ru.job4j.cars.entity.post.Post;
import ru.job4j.cars.entity.post.PriceHistory;

import java.util.List;
import java.util.Optional;

public interface PostMapper {

    PostShortViewDto getShortViewDtoFromEntity(Post post);

    PostFullViewDto getFullViewDtoFromEntity(Post post);

    Optional<Long> getLastPrice(List<PriceHistory> priceHistoryList);

}
