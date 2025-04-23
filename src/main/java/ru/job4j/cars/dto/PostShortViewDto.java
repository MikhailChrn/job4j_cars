package ru.job4j.cars.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PostShortViewDto {

    private int id;

    private String postTitle;

    private String carBrand;

    private LocalDateTime create;

    private long priceLast;

    private boolean isSold;

}
