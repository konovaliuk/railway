package com.liashenko.app.persistance.dao;

import com.liashenko.app.persistance.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserDao extends GenericJDBCDao {

    boolean isExists(Long key);

    /** Создает новую запись и соответствующий ей объект */
    void create(User object);

    /** Создает новую запись, соответствующую объекту object */
    Optional<User> persist(User object);

    /** Возвращает объект соответствующий записи с первичным ключом key или null */
    Optional<User> getByPK(Long key);

    /** Сохраняет состояние объекта group в базе данных */
    void update(User object);

    /** Удаляет запись об объекте из базы данных */
    void delete(User object);

    /** Возвращает список объектов соответствующих всем записям в базе данных */
    Optional<List<User>> getAll();

    boolean isEmailExists(String email);

    Optional<User> getUserByEmail(String email);

    Integer getCount();

    Optional<List<User>> getPages(int rowsPerPage, int offset);
}
