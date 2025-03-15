package ru.job4j.cars.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "auto_users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String login;

    private String password;
}
