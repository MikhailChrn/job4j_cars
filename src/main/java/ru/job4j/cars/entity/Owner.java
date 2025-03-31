package ru.job4j.cars.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id", "name"})
@Entity
@Table(name = "owners")
public class Owner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    /**
     * Хранение хронологии автомобилей у собственника
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "cars_owners_history",
            joinColumns = {@JoinColumn(name = "owner_id")},
            inverseJoinColumns = {@JoinColumn(name = "car_id")}
    )
    private Collection<Car> cars;

}
