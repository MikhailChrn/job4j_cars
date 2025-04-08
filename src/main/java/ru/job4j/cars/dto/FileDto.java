package ru.job4j.cars.dto;

import lombok.*;
import ru.job4j.cars.entity.post.File;

/**
 * DTO for {@link File}
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