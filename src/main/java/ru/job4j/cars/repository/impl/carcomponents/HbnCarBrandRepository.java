package ru.job4j.cars.repository.impl.carcomponents;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.entity.carcomponents.CarBrand;
import ru.job4j.cars.repository.CrudRepository;
import ru.job4j.cars.repository.RegularRepository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Repository
public class HbnCarBrandRepository implements RegularRepository<CarBrand> {

    private final CrudRepository crudRepository;

    @Override
    public Optional<CarBrand> save(CarBrand carBrand) {
        try {
            crudRepository.run(session -> session.persist(carBrand));
            return Optional.of(carBrand);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public boolean update(CarBrand carBrand) {
        return crudRepository.tx(
                session -> session.merge(carBrand)).equals(carBrand);
    }

    @Override
    public boolean deleteById(int id) {
        return crudRepository.tx(session ->
                session.createQuery("DELETE CarBrand c WHERE c.id = :fId")
                        .setParameter("fId", id)
                        .executeUpdate()) > 0;
    }

    @Override
    public Collection<CarBrand> findAll() {
        return crudRepository.query("FROM CarBrand", CarBrand.class);
    }

    @Override
    public Optional<CarBrand> findById(int id) {
        return crudRepository.optional(
                "FROM CarBrand c WHERE c.id = :fId", CarBrand.class,
                Map.of("fId", id)
        );
    }
}
