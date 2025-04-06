package ru.job4j.cars.repository;

import ru.job4j.cars.entity.File;

import java.util.Optional;

public interface FileRepository {

    Optional<File> save(File file);

    Optional<File> findById(int fileId);

    boolean deleteById(int fileId);

}
