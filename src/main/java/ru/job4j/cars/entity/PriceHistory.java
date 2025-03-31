package ru.job4j.cars.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
@Table(name = "price_history")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PriceHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    private LocalDateTime created = LocalDateTime.now(ZoneId.of("UTC"));

    private long before;

    private long after;

    /**
     * Связь с объявлением
     */
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

}
