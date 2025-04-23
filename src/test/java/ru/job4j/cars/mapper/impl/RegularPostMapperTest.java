package ru.job4j.cars.mapper.impl;

import org.junit.jupiter.api.Test;
import ru.job4j.cars.entity.carcomponents.BodyType;
import ru.job4j.cars.entity.carcomponents.CarBrand;
import ru.job4j.cars.entity.carcomponents.Engine;
import ru.job4j.cars.entity.post.Post;
import ru.job4j.cars.entity.post.PriceHistory;
import ru.job4j.cars.mapper.PostMapper;
import ru.job4j.cars.repository.PriceHistoryRepository;
import ru.job4j.cars.repository.RegularRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import static org.mockito.Mockito.mock;

class RegularPostMapperTest {

    @Test
    public void whenGetLastPriceFromHistoryPriceListThenGetIt() {
        RegularRepository<CarBrand> carBrandRepository = mock(RegularRepository.class);
        RegularRepository<BodyType> bodyTypeRepository = mock(RegularRepository.class);
        RegularRepository<Engine> engineRepository = mock(RegularRepository.class);
        PriceHistoryRepository priceHistoryRepository = mock(PriceHistoryRepository.class);

        PostMapper postMapper = new RegularPostMapper(carBrandRepository,
                bodyTypeRepository,
                engineRepository,
                priceHistoryRepository);

        LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC"));

        Post post = Post.builder().id(9).build();

        PriceHistory pH1 = PriceHistory.builder()
                .id(3).created(now.minusYears(5)).before(0).after(1000000).post(post).build();
        PriceHistory pH2 = PriceHistory.builder()
                .id(2).created(now.minusYears(4)).before(pH1.getAfter())
                .after(1100000).post(post).build();
        PriceHistory pH3 = PriceHistory.builder()
                .id(1).created(now.minusYears(3)).before(pH2.getAfter())
                .after(1200000).post(post).build();
        PriceHistory pH4 = PriceHistory.builder()
                .id(5).created(now.minusYears(2)).before(pH3.getAfter())
                .after(1300000).post(post).build();
        PriceHistory pH5 = PriceHistory.builder()
                .id(4).created(now.minusYears(1)).before(pH4.getAfter())
                .after(900000).post(post).build();

        List<PriceHistory> priceHistoryList = List.of(pH5, pH1, pH4, pH2, pH3);

        assertThat(postMapper.getLastPrice(priceHistoryList).get())
                .isEqualTo(pH5.getAfter());
    }

}