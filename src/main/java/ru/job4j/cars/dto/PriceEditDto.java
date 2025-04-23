package ru.job4j.cars.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PriceEditDto {

    private int postId;

    private String postTitle;

    private long before;

    private long after;

}
