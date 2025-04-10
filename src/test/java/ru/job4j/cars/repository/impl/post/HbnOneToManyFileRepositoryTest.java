package ru.job4j.cars.repository.impl.post;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.configuration.HibernateConfiguration;
import ru.job4j.cars.entity.User;
import ru.job4j.cars.entity.post.File;
import ru.job4j.cars.entity.post.Post;
import ru.job4j.cars.repository.CrudRepository;
import ru.job4j.cars.repository.RegularRepository;
import ru.job4j.cars.repository.impl.HbnUserRepository;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class HbnOneToManyFileRepositoryTest {

    private static RegularRepository<User> userRepository;

    private static RegularRepository<File> fileRepository;

    private static RegularRepository<Post> postRepository;

    @BeforeAll
    public static void initRepositories() {
        SessionFactory sessionFactory = new HibernateConfiguration().sessionFactory();
        CrudRepository crudRepository = new CrudRepository(sessionFactory);
        userRepository = new HbnUserRepository(crudRepository);
        fileRepository = new HbnFileRepository(crudRepository);
        postRepository = new HbnPostRepository(crudRepository);
    }

    @AfterEach
    public void clearRepositories() {
        fileRepository.findAll().forEach(
                post -> fileRepository.deleteById(post.getId())
        );
        postRepository.findAll().forEach(
                post -> postRepository.deleteById(post.getId())
        );
        userRepository.findAll().forEach(
                user -> userRepository.deleteById(user.getId())
        );
    }

    @Test
    public void whenCreateCarWithOwnersListThenGetCorrectRepositoryResponse() {
        User user = userRepository.save(User.builder()
                .login("login").password("password").build()).get();

        Post postBefore = postRepository.save(new Post("test title", user)).get();

        File file1 = fileRepository.save(File.builder()
                .title("title-1").path("path-1").build()).get();
        File file2 = fileRepository.save(File.builder()
                .title("title-2").path("path-2").build()).get();
        File file3 = fileRepository.save(File.builder()
                .title("title-3").path("path-3").build()).get();
        File file4 = fileRepository.save(File.builder()
                .title("title-4").path("path-4").build()).get();

        Collection<File> expFilesList =
                List.of(file4, file3, file2, file1);

        expFilesList.forEach(postBefore::addFile);

        postRepository.update(postBefore);

        Collection<File> responseRepository =
                postRepository.findById(postBefore.getId()).get().getFiles();

        assertThat(responseRepository.size()).isEqualTo(expFilesList.size());
        assertThat(responseRepository.containsAll(expFilesList));
    }
}
