package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ru.job4j.cars.model.User;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class HibernateUserRepository implements UserRepository {

    private final SessionFactory sessionFactory;

    /**
     * Сохранить в базе.
     *
     * @param user пользователь.
     * @return пользователь с id.
     */
    @Override
    public User create(User user) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.createQuery(
                            "INSERT INTO User (id, login, password)")
                    .executeUpdate();
            session.getTransaction().commit();

        } catch (Exception ex) {
            session.getTransaction().rollback();

        } finally {
            session.close();
        }

        return user;
    }

    /**
     * Обновить в базе пользователя.
     *
     * @param user пользователь.
     */
    @Override
    public void update(User user) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.createQuery(
                            "UPDATE User "
                                    + "SET login = :login, "
                                    + "password = :password "
                                    + "WHERE id = :id")
                    .setParameter("login", user.getLogin())
                    .setParameter("password", user.getPassword())
                    .setParameter("id", user.getId())
                    .executeUpdate();
            session.getTransaction().commit();

        } catch (Exception ex) {
            session.getTransaction().rollback();

        } finally {
            session.close();
        }
    }

    /**
     * Удалить пользователя по id.
     *
     * @param userId ID
     */
    @Override
    public void delete(int userId) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.createQuery(
                            "DELETE User WHERE id = :userId")
                    .setParameter("userId", userId)
                    .executeUpdate();
            session.getTransaction().commit();

        } catch (Exception e) {
            session.getTransaction().rollback();

        } finally {
            session.close();
        }
    }

    /**
     * Список пользователь отсортированных по id.
     *
     * @return список пользователей.
     */
    @Override
    public List<User> findAllOrderById() {
        Session session = sessionFactory.openSession();
        List<User> result = null;
        try {
            session.beginTransaction();
            result = session.createQuery(
                            "FROM User ORDER BY id", User.class)
                    .getResultList();
            session.getTransaction().commit();

        } catch (Exception e) {
            session.getTransaction().rollback();

        } finally {
            session.close();
        }

        return result;
    }

    /**
     * Найти пользователя по ID
     *
     * @return пользователь.
     */
    @Override
    public Optional<User> findById(int userId) {
        Session session = sessionFactory.openSession();
        Optional<User> result = Optional.empty();
        try {
            session.beginTransaction();
            result = session.createQuery(
                            "FROM User U WHERE U.id = :id")
                    .setParameter("id", userId)
                    .uniqueResultOptional();
            session.getTransaction().commit();

        } catch (Exception e) {
            session.getTransaction().rollback();

        } finally {
            session.close();
        }

        return result;
    }

    /**
     * Список пользователей по login LIKE %key%
     *
     * @param key key
     * @return список пользователей.
     */
    @Override
    public List<User> findByLikeLogin(String key) {
        Session session = sessionFactory.openSession();
        List<User> result = null;
        try {
            session.beginTransaction();
            result = session.createQuery(
                            "FROM User AS u WHERE u.login LIKE :key", User.class)
                    .setParameter("key", "%" + key + "%")
                    .getResultList();
            session.getTransaction().commit();

        } catch (Exception e) {
            session.getTransaction().rollback();

        } finally {
            session.close();
        }

        return result;
    }

    /**
     * Найти пользователя по login.
     *
     * @param login login.
     * @return Optional or user.
     */
    @Override
    public Optional<User> findByLogin(String login) {
        Session session = sessionFactory.openSession();
        Optional<User> result = Optional.empty();
        try {
            session.beginTransaction();
            result = session.createQuery(
                            "FROM User U WHERE U.login = :login")
                    .setParameter("login", login)
                    .uniqueResultOptional();
            session.getTransaction().commit();

        } catch (Exception e) {
            session.getTransaction().rollback();

        } finally {
            session.close();
        }

        return result;
    }
}
