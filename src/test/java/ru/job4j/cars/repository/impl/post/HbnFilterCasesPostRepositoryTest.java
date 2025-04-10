package ru.job4j.cars.repository.impl.post;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.configuration.HibernateConfiguration;
import ru.job4j.cars.entity.User;
import ru.job4j.cars.entity.carcomponents.Car;
import ru.job4j.cars.entity.carcomponents.CarBrand;
import ru.job4j.cars.entity.post.File;
import ru.job4j.cars.entity.post.Post;
import ru.job4j.cars.repository.CrudRepository;
import ru.job4j.cars.repository.PostRepository;
import ru.job4j.cars.repository.RegularRepository;
import ru.job4j.cars.repository.impl.HbnUserRepository;
import ru.job4j.cars.repository.impl.carcomponents.HbnCarBrandRepository;
import ru.job4j.cars.repository.impl.carcomponents.HbnCarRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class HbnFilterCasesPostRepositoryTest {

    private static RegularRepository<User> userRepository;

    private static RegularRepository<File> fileRepository;

    private static RegularRepository<CarBrand> carBrandRepository;

    private static RegularRepository<Car> carRepository;

    private static PostRepository postRepository;

    @BeforeAll
    public static void initRepositories() {
        SessionFactory sessionFactory = new HibernateConfiguration().sessionFactory();
        CrudRepository crudRepository = new CrudRepository(sessionFactory);
        userRepository = new HbnUserRepository(crudRepository);
        fileRepository = new HbnFileRepository(crudRepository);
        carBrandRepository = new HbnCarBrandRepository(crudRepository);
        carRepository = new HbnCarRepository(crudRepository);
        postRepository = new HbnPostRepository(crudRepository);
    }

    @AfterEach
    public void clearRepositories() {
        postRepository.findAll().forEach(
                post -> postRepository.deleteById(post.getId())
        );
        carRepository.findAll().forEach(
                car -> carRepository.deleteById(car.getId())
        );
        carBrandRepository.findAll().forEach(
                carBrand -> carBrandRepository.deleteById(carBrand.getId())
        );
        userRepository.findAll().forEach(
                user -> userRepository.deleteById(user.getId())
        );
    }

    @Test
    public void whenFindPostByCarBrandThenGetSuccessResult() {
        User user = userRepository.save(User.builder()
                .login("login").password("password").build()).get();

        CarBrand ladaBrand = carBrandRepository.save(CarBrand.builder()
                .title("LADA").build()).get();
        CarBrand moskvichBrand = carBrandRepository.save(CarBrand.builder()
                .title("Moskvich").build()).get();

        Car car1 = carRepository.save(Car.builder().
                title("Lada #1").carBrand(ladaBrand).build()).get();
        Car car2 = carRepository.save(Car.builder().
                title("Moskvich #1").carBrand(moskvichBrand).build()).get();
        Car car3 = carRepository.save(Car.builder().
                title("Lada #2").carBrand(ladaBrand).build()).get();
        Car car4 = carRepository.save(Car.builder().
                title("Moskvich #2").carBrand(moskvichBrand).build()).get();
        Car car5 = carRepository.save(Car.builder().
                title("Lada #3").carBrand(ladaBrand).build()).get();

        List.of(car5, car4, car3, car2, car1).forEach(
                car -> postRepository.save(
                        new Post("title", user, car)));

        Collection<Car> expList = List.of(car5, car3, car1);

        Collection<Post> afterUseFilter = postRepository.findByCarBrand(ladaBrand.getId());

        assertThat(afterUseFilter.stream()
                .map(post -> post.getCar()).toList().size())
                .isEqualTo(expList.size());
        assertThat(afterUseFilter.stream()
                .map(post -> post.getCar()).toList().containsAll(expList));

    }

    @Test
    public void whenFindPostWithPhotoThenGetSuccessResult() {
        User user = userRepository.save(User.builder()
                .login("login").password("password").build()).get();

        Post withoutPhotoPost = postRepository.save(
                new Post("without photo", user)).get();
        Post withPhotoPost = postRepository.save(
                new Post("with photo", user)).get();

        File file1 = fileRepository.save(File.builder()
                .title("title-1").path("path-1").build()).get();
        File file2 = fileRepository.save(File.builder()
                .title("title-2").path("path-2").build()).get();
        File file3 = fileRepository.save(File.builder()
                .title("title-3").path("path-3").build()).get();

        List.of(file3, file2, file1)
                .forEach(withPhotoPost::addFile);

        List.of(withPhotoPost, withoutPhotoPost)
                .forEach(postRepository::update);

        assertThat(postRepository.findWithPhoto().size()).isEqualTo(1);
        assertThat(postRepository.findWithPhoto().stream().findFirst().get())
                .isEqualTo(withPhotoPost);
    }

    @Test
    public void whenFindPostForLastDayThenGetSuccessResult() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC"));
        User user = userRepository.save(User.builder()
                .login("login").password("password").build()).get();

        Post post1 = postRepository.save(Post.builder()
                .title("title-1").user(user).created(now.minusHours(1))
                .build()).get();
        Post post2 = postRepository.save(Post.builder()
                .title("title-2").user(user).created(now.minusMonths(2))
                .build()).get();
        Post post3 = postRepository.save(Post.builder()
                .title("title-3").user(user).created(now.minusHours(5))
                .build()).get();
        Post post4 = postRepository.save(Post.builder()
                .title("title-4").user(user).created(now.minusYears(1))
                .build()).get();
        Post post5 = postRepository.save(Post.builder()
                .title("title-1").user(user).created(now.minusHours(13))
                .build()).get();

        Collection<Post> expList = List.of(post5, post3, post1);

        assertThat(postRepository.findByLastDay().size()).isEqualTo(3);
        assertThat(postRepository.findByLastDay().containsAll(expList));
    }
}
