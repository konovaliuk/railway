package com.liashenko.app.persistance.dao;

import com.liashenko.app.persistance.domain.VagonType;

import java.util.List;
import java.util.Optional;

public interface VagonTypeDao extends GenericJDBCDao {
    boolean isExists(Long key);

    /**
     * Создает новую запись и соответствующий ей объект
     */
    void create(VagonType object);

    /**
     * Создает новую запись, соответствующую объекту object
     */
    Optional<VagonType> persist(VagonType object);

    /**
     * Возвращает объект соответствующий записи с первичным ключом key или null
     */
    Optional<VagonType> getByPK(Integer key);

    /**
     * Сохраняет состояние объекта group в базе данных
     */
    void update(VagonType object);

    /**
     * Удаляет запись об объекте из базы данных
     */
    void delete(VagonType object);

    /**
     * Возвращает список объектов соответствующих всем записям в базе данных
     */
    Optional<List<VagonType>> getAll();
}
