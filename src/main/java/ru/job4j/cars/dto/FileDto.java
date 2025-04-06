package ru.job4j.cars.dto;

import lombok.*;

/**
 * DTO for {@link ru.job4j.cars.entity.File}
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileDto {

    private int id;

    private String title;

    private byte[] content;

}