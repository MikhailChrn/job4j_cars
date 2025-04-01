package ru.job4j.cars.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id", "title"})
@Entity
@Table(name = "cars")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;

    /**
     * Ссылка на марку автомобиля
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_brand_id")
    private CarBrand carBrand;

    /**
     * Ссылка на марку двигателя
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "engine_id")
    private Engine engine;

    /**
     * Хранение хронологии собственников у автомобиля
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "cars_owners_history",
            joinColumns = {@JoinColumn(name = "car_id")},
            inverseJoinColumns = {@JoinColumn(name = "owner_id")}
    )
    private List<Owner> owners = new ArrayList<>();

}
