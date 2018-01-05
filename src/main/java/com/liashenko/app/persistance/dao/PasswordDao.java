package com.liashenko.app.persistance.dao;

import com.liashenko.app.persistance.domain.Password;

import java.util.List;
import java.util.Optional;

public interface PasswordDao extends GenericJDBCDao {

    boolean isExists(Long key);

    /** Создает новую запись и соответствующий ей объект */
    void create(Password object);

    /** Создает новую запись, соответствующую объекту object */
    Optional<Password> persist(Password object);

    /** Возвращает объект соответствующий записи с первичным ключом key или null */
    Optional<Password> getByPK(Long key);

    /** Сохраняет состояние объекта group в базе данных */
    void update(Password object);

    /** Удаляет запись об объекте из базы данных */
    void delete(Password object);

    /** Возвращает список объектов соответствующих всем записям в базе данных */
    Optional<List<Password>> getAll();
}
