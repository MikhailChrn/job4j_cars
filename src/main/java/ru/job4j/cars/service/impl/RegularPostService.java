package ru.job4j.cars.service.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.job4j.cars.comparator.PostShortViewDtoDescByCreate;
import ru.job4j.cars.dto.PostCreateDto;
import ru.job4j.cars.dto.PostFullViewDto;
import ru.job4j.cars.dto.PostShortViewDto;
import ru.job4j.cars.dto.PriceEditDto;
import ru.job4j.cars.entity.carcomponents.BodyType;
import ru.job4j.cars.entity.carcomponents.Car;
import ru.job4j.cars.entity.carcomponents.CarBrand;
import ru.job4j.cars.entity.carcomponents.Engine;
import ru.job4j.cars.entity.carhistory.CarOwnerHistory;
import ru.job4j.cars.entity.carhistory.Owner;
import ru.job4j.cars.entity.post.Post;
import ru.job4j.cars.entity.post.PriceHistory;
import ru.job4j.cars.mapper.PostMapper;
import ru.job4j.cars.repository.PostRepository;
import ru.job4j.cars.repository.RegularRepository;
import ru.job4j.cars.service.PostService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class RegularPostService implements PostService {

    public static final DateTimeFormatter FORMATTER
            = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final RegularRepository<Owner> ownerRepository;

    private final RegularRepository<CarOwnerHistory> carOwnerHistoryRepository;

    private final RegularRepository<PriceHistory> priceHistoryRepository;

    private final RegularRepository<CarBrand> carBrandRepository;

    private final RegularRepository<BodyType> bodyTypeRepository;

    private final RegularRepository<Engine> engineRepository;

    private final RegularRepository<Car> carRepository;

    private final PostRepository postRepository;

    private final PostMapper postMapper;

    private final PostShortViewDtoDescByCreate postDtoComparator;

    /**
     * Получить список объявлений пользователя, отсортированных хронологически
     */
    @Override
    public Collection<PostShortViewDto> findAllByUserIdDesc(int userId) {
        return postRepository.findAllByUserId(userId).stream()
                .map(postMapper::getShortViewDtoFromEntity)
                .sorted(postDtoComparator)
                .toList();
    }

    /**
     * Получить список ВСЕХ объявлений, отсортированных хронологически
     */
    @Override
    public Collection<PostShortViewDto> findAllDesc() {
        return postRepository.findAll().stream()
                .map(postMapper::getShortViewDtoFromEntity)
                .sorted(postDtoComparator)
                .toList();
    }

    /**
     * Получить список всех объявлений с фото, отсортированных хронологически
     */
    @Override
    public Collection<PostShortViewDto> findWithPhotoDesc() {
        return postRepository.findWithPhoto().stream()
                .map(postMapper::getShortViewDtoFromEntity)
                .sorted(postDtoComparator)
                .toList();
    }

    /**
     * Получить список объявлений за последнюю неделю, отсортированных хронологически
     */
    @Override
    public Collection<PostShortViewDto> findByLastWeekDesc() {
        return postRepository.findByLastWeek().stream()
                .map(postMapper::getShortViewDtoFromEntity)
                .sorted(postDtoComparator)
                .toList();
    }

    /**
     * Получить расширенное представление 'PostFullViewDto'
     * для отображения карточки объявления
     */
    @Override
    public Optional<PostFullViewDto> getPostById(int id) {
        log.warn("TRY TO FIND POST");

        Optional<Post> optionalPost = postRepository.findById(id);
        if (optionalPost.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(
                postMapper.getFullViewDtoFromEntity(optionalPost.get()));
    }

    /**
     * Добавляем новое объявление,
     * в т.ч. создаём новый автомобиль,
     * собственника автомобиля и стартовую цену
     */
    @Override
    @Transactional
    public boolean add(PostCreateDto dto) {
        log.warn("Begin ADD post method");
        LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC"));

        Car car = carRepository.save(new Car(dto.getTitle(),
                carBrandRepository.findById(dto.getCarBrandId()).get(),
                bodyTypeRepository.findById(dto.getBodyTypeId()).get(),
                engineRepository.findById(dto.getEngineId()).get())).get();

        Owner owner = ownerRepository.save(new Owner(dto.getOwnerName())).get();

        CarOwnerHistory carOwnerHistory = carOwnerHistoryRepository.save(
                CarOwnerHistory.builder()
                        .owner(owner).startAt(LocalDateTime.of(
                                LocalDate.parse(dto.getDateCreate(), FORMATTER),
                                LocalTime.of(9, 0)))
                        .endAt(now).car(null).build()).get();

        car.addCarOwnerHistory(carOwnerHistory);

        Post post = postRepository.save(new Post(dto.getTitle(),
                dto.getUser(),
                car)).get();

        PriceHistory priceHistory = priceHistoryRepository.save(
                PriceHistory.builder()
                        .created(now)
                        .before(0)
                        .after(dto.getCost()).build()).get();

        post.addPriceHistory(priceHistory);

        log.warn("End ADD post method. Add " + post.getTitle());

        return postRepository.update(post);
    }

    /**
     * Получаем представление для добавления нового значения цены
     */
    @Override
    public Optional<PriceEditDto> getEditPriceDtoById(int id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        if (optionalPost.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(PriceEditDto.builder().postId(id)
                .postTitle(optionalPost.get().getTitle())
                .before(postMapper.getLastPrice(optionalPost.get()
                        .getPriceHistories()).get())
                .build());
    }

    /**
     * Добавляем новое значения цены для объявления
     */
    @Override
    public boolean updatePrice(PriceEditDto dto) {
        Post post = postRepository.findById(dto.getPostId()).get();
        PriceHistory priceHistory = priceHistoryRepository.save(
                new PriceHistory(dto.getBefore(), dto.getAfter())).get();
        post.addPriceHistory(priceHistory);

        return postRepository.update(post);
    }

    /**
     * Удаляем объявление, в т.ч. всю историю его цены
     */
    @Override
    public boolean deleteById(int id) {
        Post deletedPost = postRepository.findById(id).get();
        List<PriceHistory> priceHistoryList = new ArrayList<>();
        priceHistoryList.addAll(deletedPost.getPriceHistories());

        priceHistoryList.forEach(deletedPost::removePriceHistory);
        priceHistoryList.forEach(
                pH -> priceHistoryRepository.deleteById(pH.getId()));

        return postRepository.deleteById(deletedPost.getId());
    }

    /**
     * Изменяем статус объявления на "ПРОДАНО"
     */
    @Override
    public boolean updateStatusById(int postId, boolean isSold) {
        return postRepository.updateStatusById(postId, isSold);
    }
}
