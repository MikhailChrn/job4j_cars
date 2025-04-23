package ru.job4j.cars.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.entity.carcomponents.CarBrand;
import ru.job4j.cars.repository.RegularRepository;
import ru.job4j.cars.service.RegularService;

import java.util.Collection;

@Service
@AllArgsConstructor
public class RegularCarBrandService implements RegularService<CarBrand> {

    private final RegularRepository<CarBrand> carBrandRepository;

    @Override
    public Collection<CarBrand> findAll() {
        return carBrandRepository.findAll();
    }
}
