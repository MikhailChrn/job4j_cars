package ru.job4j.cars.entity.post;

import jakarta.persistence.*;
import lombok.*;
import ru.job4j.cars.entity.User;
import ru.job4j.cars.entity.carcomponents.Car;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Entity
@Table(name = "posts")
@Data
@Builder
@EqualsAndHashCode(of = {"id", "title"})
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;

    private String description;

    private LocalDateTime created = LocalDateTime.now(ZoneId.of("UTC"));

    private boolean isSold;

    /**
     * Автор данного объявления
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Автомобиль на продажу в данном объявлении
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id")
    private Car car;

    /**
     * История цены автомобиля
     */
    @OneToMany(mappedBy = "post",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<PriceHistory> priceHistories = new ArrayList<>();

    public void addPriceHistory(PriceHistory priceHistory) {
        priceHistory.setPost(this);
        this.priceHistories.add(priceHistory);
    }

    public void removePriceHistory(PriceHistory priceHistory) {
        priceHistory.setPost(null);
        this.priceHistories.remove(priceHistory);
    }

    /**
     * Файлы с фотографиями автомобиля
     */
    @OneToMany(mappedBy = "post",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private Set<File> files = new HashSet<>();

    public void addFile(File file) {
        file.setPost(this);
        this.files.add(file);
    }

    public void removeFile(File file) {
        file.setPost(null);
        this.files.remove(file);
    }

    public Post(String title, User user) {
        this.title = title;
        this.user = user;
    }

    public Post(String title, User user, Car car) {
        this.title = title;
        this.user = user;
        this.car = car;
        this.isSold = false;
    }
}