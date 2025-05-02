package ru.job4j.cars.repository.impl.post;

import lombok.AllArgsConstructor;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.configuration.HibernateConfiguration;
import ru.job4j.cars.entity.carcomponents.Engine;
import ru.job4j.cars.entity.post.File;
import ru.job4j.cars.repository.CrudRepository;
import ru.job4j.cars.repository.RegularRepository;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@AllArgsConstructor
@Repository
class HbnFileRepositoryTest {

    private static RegularRepository<File> fileRepository;

    @BeforeAll
    public static void initRepositories() {
        SessionFactory sessionFactory = new HibernateConfiguration().sessionFactory();
        CrudRepository crudRepository = new CrudRepository(sessionFactory);
        fileRepository = new HbnFileRepository(crudRepository);
    }

    @AfterEach
    public void clearRepositories() {
        fileRepository.findAll().forEach(
                file -> fileRepository.deleteById(file.getId())
        );
    }

    @Test
    public void whenDeleteByInvalidIdThenGetFalse() {
        assertThat(fileRepository.deleteById(99))
                .isFalse();
    }

    @Test
    public void whenDontSaveThenNothingFoundAndGetFalse() {
        assertThat(fileRepository.deleteById(0)).isFalse();
    }

    @Test
    @Disabled
    public void whenSaveSeveralThenGetAllEntities() {
        File file1 = new File("title-1", "path-1");
        File file2 = new File("title-2", "path-2");
        File file3 = new File("title-3", "path-3");

        List.of(file3, file2, file1)
                .forEach(file -> fileRepository.save(file));

        Collection<File> expected = List.of(file1, file2, file3);

        Collection<File> fileRepositoryResponse = fileRepository.findAll();

        assertTrue(expected.size() == fileRepositoryResponse.size());
        assertTrue(expected.containsAll(fileRepositoryResponse));
    }

    @Test
    public void whenUpdateThenGetRefreshEntity() {
        File beforeUpdate = fileRepository.save(
                File.builder().title("title before")
                        .path("path before").build()).get();

        File afterUpdate = File.builder()
                .id(beforeUpdate.getId())
                .title("title after")
                .path(beforeUpdate.getPath())
                .build();

        fileRepository.update(afterUpdate);

        assertThat(fileRepository.findById(beforeUpdate.getId()).get())
                .isEqualTo(afterUpdate);
    }

}