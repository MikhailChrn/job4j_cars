package ru.job4j.cars.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.job4j.cars.entity.User;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostCreateDto {

    private String title;

    private User user;

    private String description;

    private int carBrandId;

    private int bodyTypeId;

    private int engineId;

    private String ownerName;

    private String dateCreate;

    private int cost;

}
