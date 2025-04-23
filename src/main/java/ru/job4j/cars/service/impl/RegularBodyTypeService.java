package ru.job4j.cars.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.entity.carcomponents.BodyType;
import ru.job4j.cars.repository.RegularRepository;
import ru.job4j.cars.service.RegularService;

import java.util.Collection;

@Service
@AllArgsConstructor
public class RegularBodyTypeService implements RegularService<BodyType> {

    private final RegularRepository<BodyType> bodyTypeRepository;

    @Override
    public Collection<BodyType> findAll() {
        return bodyTypeRepository.findAll();
    }
}
