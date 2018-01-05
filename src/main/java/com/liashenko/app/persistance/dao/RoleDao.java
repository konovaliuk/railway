package com.liashenko.app.persistance.dao;

import com.liashenko.app.persistance.domain.Role;

import java.util.List;
import java.util.Optional;

public interface RoleDao extends GenericJDBCDao {

    boolean isExists(Long key);

    /** Создает новую запись и соответствующий ей объект */
    void create(Role object);

    /** Создает новую запись, соответствующую объекту object */
    Optional<Role> persist(Role object);

    /** Возвращает объект соответствующий записи с первичным ключом key или null */
    Optional<Role> getByPK(Long key);

    /** Сохраняет состояние объекта group в базе данных */
    void update(Role object);

    /** Удаляет запись об объекте из базы данных */
    void delete(Role object);

    /** Возвращает список объектов соответствующих всем записям в базе данных */
    Optional<List<Role>> getAll();
}
