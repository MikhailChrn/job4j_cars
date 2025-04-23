package ru.job4j.cars.entity.carhistory;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "owners")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Owner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    public Owner(String name) {
        this.name = name;
    }
}
