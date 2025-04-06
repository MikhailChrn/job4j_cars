package ru.job4j.cars.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;

    private String path;

    public File(String title, String path) {
        this.title = title;
        this.path = path;
    }
}
