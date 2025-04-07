package ru.job4j.cars.repository.impl.post;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.configuration.HibernateConfiguration;
import ru.job4j.cars.entity.User;
import ru.job4j.cars.entity.post.Post;
import ru.job4j.cars.entity.post.PriceHistory;
import ru.job4j.cars.repository.CrudRepository;
import ru.job4j.cars.repository.RegularRepository;
import ru.job4j.cars.repository.impl.HbnUserRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class HbnOneToManyPriceHistoryRepositoryTest {

    private static RegularRepository<User> userRepository;

    private static RegularRepository<PriceHistory> priceHistoryRepository;

    private static RegularRepository<Post> postRepository;

    @BeforeAll
    public static void initRepositories() {
        SessionFactory sessionFactory = new HibernateConfiguration().sessionFactory();
        CrudRepository crudRepository = new CrudRepository(sessionFactory);
        userRepository = new HbnUserRepository(crudRepository);
        priceHistoryRepository = new HbnPriceHistoryRepository(crudRepository);
        postRepository = new HbnPostRepository(crudRepository);
    }

    @AfterEach
    public void clearRepositories() {
        priceHistoryRepository.findAll().forEach(
                post -> priceHistoryRepository.deleteById(post.getId())
        );
        postRepository.findAll().forEach(
                post -> postRepository.deleteById(post.getId())
        );
        userRepository.findAll().forEach(
                post -> userRepository.deleteById(post.getId())
        );
    }

    @Test
    public void whenCreateEmptyPostWithoutPriceHistory() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC"));

        User user = userRepository.save(User.builder()
                .login("login").password("password").build()).get();

        Post postBefore = postRepository.save(Post.builder().title("test title")
                .created(now).user(user).build()).get();

        Post postAfter = postRepository.findById(postBefore.getId()).get();

        assertThat(postAfter).isEqualTo(postBefore);
    }

    @Test
    public void whenCreateCarWithOwnersListThenGetCorrectRepositoryResponse() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC"));

        User user = userRepository.save(User.builder()
                .login("login").password("password").build()).get();

        Post postBefore = postRepository.save(new Post("test title", user)).get();

        PriceHistory priceHistory1 = priceHistoryRepository.save(PriceHistory.builder()
                .before(1_000_000).after(1_499_000).created(now.minusYears(4)).build()).get();
        PriceHistory priceHistory2 = priceHistoryRepository.save(PriceHistory.builder()
                .before(priceHistory1.getAfter())
                .after(1_999_000).created(now.minusYears(3)).build()).get();
        PriceHistory priceHistory3 = priceHistoryRepository.save(PriceHistory.builder()
                .before(priceHistory2.getAfter())
                .after(2_499_000).created(now.minusYears(2)).build()).get();

        Collection<PriceHistory> expHistoryList =
                List.of(priceHistory3, priceHistory2, priceHistory1);

        expHistoryList.forEach(postBefore::addPriceHistory);

        postRepository.update(postBefore);

        Collection<PriceHistory> responseRepository =
                postRepository.findById(postBefore.getId()).get().getPriceHistories();

        assertThat(responseRepository.size()).isEqualTo(expHistoryList.size());
        assertThat(responseRepository.containsAll(expHistoryList));
    }
}
