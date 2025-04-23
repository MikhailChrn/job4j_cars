package ru.job4j.cars.service;

import ru.job4j.cars.dto.PostCreateDto;
import ru.job4j.cars.dto.PostFullViewDto;
import ru.job4j.cars.dto.PostShortViewDto;
import ru.job4j.cars.dto.PriceEditDto;

import java.util.Collection;
import java.util.Optional;

public interface PostService {

    Collection<PostShortViewDto> findAllByUserIdDesc(int id);

    Collection<PostShortViewDto> findAllDesc();

    Collection<PostShortViewDto> findWithPhotoDesc();

    Collection<PostShortViewDto> findByLastWeekDesc();

    Optional<PostFullViewDto> getPostById(int id);

    boolean add(PostCreateDto dto);

    Optional<PriceEditDto> getEditPriceDtoById(int id);

    boolean updatePrice(PriceEditDto dto);

    boolean deleteById(int id);

    boolean updateStatusById(int postId, boolean isSold);

}
