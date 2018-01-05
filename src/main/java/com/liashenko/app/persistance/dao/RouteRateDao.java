package com.liashenko.app.persistance.dao;

import com.liashenko.app.persistance.domain.RouteRate;

import java.util.List;
import java.util.Optional;

public interface RouteRateDao extends GenericJDBCDao {
    boolean isExists(Long key);

    /** Создает новую запись и соответствующий ей объект */
    void create(RouteRate object);

    /** Создает новую запись, соответствующую объекту object */
    Optional<RouteRate> persist(RouteRate object);

    /** Возвращает объект соответствующий записи с первичным ключом key или null */
    Optional<RouteRate> getByPK(Long key);

    /** Сохраняет состояние объекта group в базе данных */
    void update(RouteRate object);

    /** Удаляет запись об объекте из базы данных */
    void delete(RouteRate object);

    /** Возвращает список объектов соответствующих всем записям в базе данных */
    Optional<List<RouteRate>> getAll();

    Optional<RouteRate> getByRouteId(Long routeId);
}
