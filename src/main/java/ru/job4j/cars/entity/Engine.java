package ru.job4j.cars.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "engines")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Engine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;

}
