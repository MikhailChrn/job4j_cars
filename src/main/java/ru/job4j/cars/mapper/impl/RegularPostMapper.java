package ru.job4j.cars.mapper.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.dto.PostFullViewDto;
import ru.job4j.cars.dto.PostShortViewDto;
import ru.job4j.cars.entity.carcomponents.BodyType;
import ru.job4j.cars.entity.carcomponents.CarBrand;
import ru.job4j.cars.entity.carcomponents.Engine;
import ru.job4j.cars.entity.post.Post;
import ru.job4j.cars.entity.post.PriceHistory;
import ru.job4j.cars.mapper.PostMapper;
import ru.job4j.cars.repository.PriceHistoryRepository;
import ru.job4j.cars.repository.RegularRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RegularPostMapper implements PostMapper {

    private final RegularRepository<CarBrand> carBrandRepository;

    private final RegularRepository<BodyType> bodyTypeRepository;

    private final RegularRepository<Engine> engineRepository;

    private final PriceHistoryRepository priceHistoryRepository;

    @Override
    public PostShortViewDto getShortViewDtoFromEntity(Post post) {
        return PostShortViewDto.builder()
                .id(post.getId())
                .postTitle(post.getTitle())
                .create(post.getCreated())
                .carBrand(carBrandRepository.findById(
                        post.getCar().getCarBrand().getId()).get().getTitle())
                .priceLast(getLastPrice(post.getPriceHistories()).get())
                .isSold(post.isSold())
                .build();
    }

    @Override
    public PostFullViewDto getFullViewDtoFromEntity(Post post) {
        return PostFullViewDto.builder().id(post.getId())
                .postTitle(post.getTitle())
                .postDescription(post.getDescription())
                .userId(post.getUser().getId())
                .carBrand(carBrandRepository.findById(post.getCar()
                        .getCarBrand().getId()).get().getTitle())
                .bodyType(bodyTypeRepository.findById(post.getCar()
                        .getBodyType().getId()).get().getTitle())
                .engine(engineRepository.findById(post.getCar()
                        .getEngine().getId()).get().getTitle())
                .postCreate(post.getCreated())
                .priceHistories((List<PriceHistory>)
                        priceHistoryRepository.findAllByPostId(post.getId()))
                .isSold(post.isSold())
                .build();
    }

    /**
     * Получаем последнюю известную цену
     */
    @Override
    public Optional<Long> getLastPrice(List<PriceHistory> priceHistoryList) {

        return Optional.of(priceHistoryList.stream()
                .sorted(Comparator.comparing(PriceHistory::getCreated).reversed())
                .findFirst().get().getAfter());
    }
}
