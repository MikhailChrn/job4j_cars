package ru.job4j.cars.service.impl;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.comparator.PostShortViewDtoDescByCreate;
import ru.job4j.cars.dto.PostShortViewDto;
import ru.job4j.cars.entity.User;
import ru.job4j.cars.entity.carcomponents.BodyType;
import ru.job4j.cars.entity.carcomponents.Car;
import ru.job4j.cars.entity.carcomponents.CarBrand;
import ru.job4j.cars.entity.carcomponents.Engine;
import ru.job4j.cars.entity.carhistory.CarOwnerHistory;
import ru.job4j.cars.entity.carhistory.Owner;
import ru.job4j.cars.entity.post.Post;
import ru.job4j.cars.entity.post.PriceHistory;
import ru.job4j.cars.mapper.PostMapper;
import ru.job4j.cars.mapper.impl.RegularPostMapper;
import ru.job4j.cars.repository.PostRepository;
import ru.job4j.cars.repository.PriceHistoryRepository;
import ru.job4j.cars.repository.RegularRepository;
import ru.job4j.cars.service.PostService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RegularPostServiceTest {

    private static RegularRepository<User> userRepository;

    private static RegularRepository<Owner> ownerRepository;

    private static RegularRepository<CarBrand> carBrandRepository;

    private static RegularRepository<BodyType> bodyTypeRepository;

    private static RegularRepository<Engine> engineRepository;

    private static RegularRepository<Car> carRepository;

    private static RegularRepository<CarOwnerHistory> carOwnerHistoryRepository;

    private static PriceHistoryRepository priceHistoryRepository;

    private static PostMapper postMapper;

    private static PostShortViewDtoDescByCreate postDtoComparator;

    private static PostRepository postRepository;

    private static PostService postService;

    @BeforeAll
    public static void initServices() {
        userRepository = mock(RegularRepository.class);
        ownerRepository = mock(RegularRepository.class);
        postRepository = mock(PostRepository.class);
        carBrandRepository = mock(RegularRepository.class);
        bodyTypeRepository = mock(RegularRepository.class);
        carRepository = mock(RegularRepository.class);
        engineRepository = mock(RegularRepository.class);
        priceHistoryRepository = mock(PriceHistoryRepository.class);
        postMapper = new RegularPostMapper(carBrandRepository,
                bodyTypeRepository,
                engineRepository,
                priceHistoryRepository);
        postDtoComparator = new PostShortViewDtoDescByCreate();

        postService = new RegularPostService(ownerRepository,
                carOwnerHistoryRepository,
                priceHistoryRepository,
                carBrandRepository,
                bodyTypeRepository,
                engineRepository,
                carRepository,
                postRepository,
                postMapper,
                postDtoComparator);
    }

    @Disabled
    @Test
    public void whenFindAllOrderByCreatedThenGetCorrectResult() {
        User user = User.builder().login("login").password("password").build();

        LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC"));

        CarBrand lada = CarBrand.builder().title("LADA").build();

        Car car = Car.builder().id(9).carBrand(lada).build();

        List<PriceHistory> history = List.of(PriceHistory.builder().after(2_000_000).build());

        Post post1 = Post.builder().id(1).title("post1").user(user).car(car)
                .created(now.minusYears(1)).priceHistories(history).isSold(true).build();
        Post post2 = Post.builder().id(2).title("post2").user(user).car(car)
                .created(now.minusYears(2)).priceHistories(history).isSold(false).build();
        Post post3 = Post.builder().id(3).title("post3").user(user).car(car)
                .created(now.minusYears(3)).priceHistories(history).isSold(true).build();
        Post post4 = Post.builder().id(4).title("post4").user(user).car(car)
                .created(now.minusYears(4)).priceHistories(history).isSold(false).build();
        Post post5 = Post.builder().id(5).title("post5").user(user).car(car)
                .created(now.minusYears(5)).priceHistories(history).isSold(true).build();

        when(postRepository.findAll())
                .thenReturn(List.of(post5, post1, post4, post2, post3));

        /**
         * TODO требуются priceHistory для корректной работы теста
         */

        List<LocalDateTime> expListOfDates =
                List.of(post1.getCreated(), post2.getCreated(), post3.getCreated(),
                        post4.getCreated(), post5.getCreated());

        List<LocalDateTime> afterServiceDates =
                postService.findAllDesc().stream()
                        .map(PostShortViewDto::getCreate).toList();

        assertThat(expListOfDates).isEqualTo(afterServiceDates);
    }

    @Test
    public void whenUseDateFormatterThenGetSuccess() {
        LocalDate localDate = LocalDate.of(2022, 04, 11);

        String input = "2022-04-11";

        assertThat(localDate)
                .isEqualTo(LocalDate.parse(input, RegularPostService.FORMATTER));
    }
}