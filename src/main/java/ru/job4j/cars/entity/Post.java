package ru.job4j.cars.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Data
@Entity
@Builder
@Table(name = "posts")
@EqualsAndHashCode(of = {"id", "title"})
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;

    private String description;

    private LocalDateTime created = LocalDateTime.now(ZoneId.of("UTC"));

    /**
     * Автор данного объявления
     */
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Автомобиль на продажу в данном объявлении
     */
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "car_id")
    private Car car;

    /**
     * История цены автомобиля
     */
    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id")
    private List<PriceHistory> priceHistories = new ArrayList<>();

    /**
     * Подписчики на данное объявление
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "users_posts",
            joinColumns = {@JoinColumn(name = "post_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    private Set<User> users = new HashSet<>();
}
