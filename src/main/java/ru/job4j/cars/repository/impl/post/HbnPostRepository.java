package ru.job4j.cars.repository.impl.post;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.entity.post.Post;
import ru.job4j.cars.repository.CrudRepository;
import ru.job4j.cars.repository.PostRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Repository
public class HbnPostRepository implements PostRepository {

    private final CrudRepository crudRepository;

    @Override
    public Optional<Post> save(Post post) {
        try {
            crudRepository.run(session -> session.persist(post));
            return Optional.of(post);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public boolean update(Post post) {
        return crudRepository.tx(
                session -> session.merge(post)).equals(post);
    }

    @Override
    public boolean deleteById(int postId) {
        return crudRepository.tx(session ->
                session.createQuery("DELETE Post p WHERE p.id = :fId")
                        .setParameter("fId", postId)
                        .executeUpdate()) > 0;
    }

    @Override
    public Collection<Post> findAll() {
        return crudRepository.query("""
                        FROM Post p
                        LEFT JOIN FETCH p.car
                        LEFT JOIN FETCH p.files
                        """,
                Post.class);
    }

    /**
     * После использования JOIN FETCH запрос не вернет нам объекты Post,
     * у которых нет связанных с ними объектов в таблице files. Именно так работает inner join.
     * Поэтому нам нужно дополнить наш join оператором left и превратить его в left join.
     */

    @Override
    public Optional<Post> findById(int postId) {
        return crudRepository.optional("""
                        FROM Post p
                        LEFT JOIN FETCH p.car
                        LEFT JOIN FETCH p.files
                        WHERE p.id = :fId
                        """,
                Post.class,
                Map.of("fId", postId)
        );
    }

    @Override
    public Collection<Post> findByCarBrand(int carBrandId) {
        return crudRepository.query("""
                        FROM Post p
                        LEFT JOIN FETCH p.car
                        LEFT JOIN FETCH p.files
                        WHERE p.car.id IN
                            (SELECT c.id FROM Car c
                            WHERE c.carBrand.id = :fCarBrandId)
                        """,
                Post.class, Map.of("fCarBrandId", carBrandId));
    }

    /**
     *  Вы можете проверить размер коллекции специальным свойством size
     *  или специальной функцией size().
     *  from Cat cat where cat.kittens.size > 0
     *  from Cat cat where size(cat.kittens) > 0
     */

    @Override
    public Collection<Post> findWithPhoto() {
        return crudRepository.query("""
                        FROM Post p
                        LEFT JOIN FETCH p.car
                        LEFT JOIN FETCH p.files
                        WHERE SIZE(p.files) > 0
                        """,
                Post.class);
    }

    @Override
    public Collection<Post> findByLastDay() {
        LocalDateTime today = LocalDateTime.now();
        return crudRepository.query("""
                        FROM Post p
                        LEFT JOIN FETCH p.car
                        LEFT JOIN FETCH p.files
                        WHERE p.created > :fDate
                        """,
                Post.class,
                Map.of("fDate", today.minusDays(1)));
    }
}
