package com.liashenko.app.persistance.dao;

import com.liashenko.app.persistance.domain.RouteNum;

import java.util.List;
import java.util.Optional;

public interface RouteNumDao extends GenericJDBCDao {
    boolean isExists(Long key);

    /**
     * Создает новую запись и соответствующий ей объект
     */
    void create(RouteNum object);

    /**
     * Создает новую запись, соответствующую объекту object
     */
    Optional<RouteNum> persist(RouteNum object);

    /**
     * Возвращает объект соответствующий записи с первичным ключом key или null
     */
    Optional<RouteNum> getByPK(Long key);

    /**
     * Сохраняет состояние объекта group в базе данных
     */
    void update(RouteNum object);

    /**
     * Удаляет запись об объекте из базы данных
     */
    void delete(RouteNum object);

    /**
     * Возвращает список объектов соответствующих всем записям в базе данных
     */
    Optional<List<RouteNum>> getAll();
}
