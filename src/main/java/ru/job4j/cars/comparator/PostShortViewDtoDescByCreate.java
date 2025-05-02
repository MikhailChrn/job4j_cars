package ru.job4j.cars.comparator;

import org.springframework.stereotype.Component;
import ru.job4j.cars.dto.PostShortViewDto;

import java.util.Comparator;

/**
 * Используется при сортировке отображаемых в списке DTO
 * для сортировки объявлений в хронологическом порядке
 */

@Component
public class PostShortViewDtoDescByCreate implements Comparator<PostShortViewDto> {

    @Override
    public int compare(PostShortViewDto left, PostShortViewDto right) {
        return right.getCreate().compareTo(left.getCreate());
    }

}
