package ru.job4j.cars.repository.impl.post;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.configuration.HibernateConfiguration;
import ru.job4j.cars.entity.post.Post;
import ru.job4j.cars.entity.User;
import ru.job4j.cars.repository.CrudRepository;
import ru.job4j.cars.repository.RegularRepository;
import ru.job4j.cars.repository.impl.HbnUserRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class HbnPostRepositoryTest {

    private static RegularRepository<User> userRepository;

    private static RegularRepository<Post> postRepository;

    @BeforeAll
    public static void initRepositories() {
        SessionFactory sessionFactory = new HibernateConfiguration().sessionFactory();
        CrudRepository crudRepository = new CrudRepository(sessionFactory);
        userRepository = new HbnUserRepository(crudRepository);
        postRepository = new HbnPostRepository(crudRepository);
    }

    @AfterEach
    public void clearRepositories() {
        postRepository.findAll().forEach(
                post -> postRepository.deleteById(post.getId())
        );
        userRepository.findAll().forEach(
                user -> userRepository.deleteById(user.getId())
        );
    }

    @Test
    public void whenDeleteByInvalidIdThenGetFalse() {
        assertThat(postRepository.deleteById(99))
                .isFalse();
    }

    @Test
    public void whenDontSaveThenNothingFoundAndGetFalse() {
        assertThat(postRepository.deleteById(0)).isFalse();
    }

    @Test
    public void whenSaveSeveralThenGetAllEntities() {
        User user = userRepository.save(User.builder()
                .name("test name")
                .login("test login")
                .password("test password").build()).get();
        LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC"));

        Post post1 = Post.builder()
                .title("title 1")
                .user(user)
                .created(now)
                .build();
        Post post2 = Post.builder()
                .title("title 2")
                .user(user)
                .created(now)
                .build();
        Post post3 = Post.builder()
                .title("title 3")
                .user(user)
                .created(now)
                .build();

        List.of(post3, post2, post1)
                .forEach(post -> postRepository.save(post));

        Collection<Post> expected = List.of(post1, post2, post3);

        Collection<Post> postRepositoryResponse = postRepository.findAll();

        assertTrue(expected.size() == postRepositoryResponse.size());
        assertTrue(expected.containsAll(postRepositoryResponse));
    }

}