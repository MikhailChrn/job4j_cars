package ru.job4j.cars.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.entity.carcomponents.Engine;
import ru.job4j.cars.repository.RegularRepository;
import ru.job4j.cars.service.RegularService;

import java.util.Collection;

@Service
@AllArgsConstructor
public class RegularEngineService implements RegularService<Engine> {

    private final RegularRepository<Engine> engineRepository;

    @Override
    public Collection<Engine> findAll() {
        return engineRepository.findAll();
    }
}
