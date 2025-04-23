package ru.job4j.cars.dto;

import lombok.Builder;
import lombok.Data;
import ru.job4j.cars.entity.post.PriceHistory;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class PostFullViewDto {

    private int id;

    private String postTitle;

    private String postDescription;

    private int userId;

    private String carBrand;

    private String bodyType;

    private String engine;

    private LocalDateTime postCreate;

    private List<PriceHistory> priceHistories;

    private boolean isSold;

}
