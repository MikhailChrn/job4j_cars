package ru.job4j.cars.service;

import java.util.Collection;

public interface RegularService<T> {

    Collection<T> findAll();

}
