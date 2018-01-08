package com.liashenko.app.persistance.dao;

import com.liashenko.app.persistance.domain.Route;

import java.util.List;
import java.util.Optional;

public interface RouteDao extends GenericJDBCDao {
    boolean isExists(Long key);

    /**
     * Создает новую запись и соответствующий ей объект
     */
    void create(Route object);

    /**
     * Создает новую запись, соответствующую объекту object
     */
    Optional<Route> persist(Route object);

    /**
     * Возвращает объект соответствующий записи с первичным ключом key или null
     */
    Optional<Route> getByPK(Long key);

    /**
     * Сохраняет состояние объекта group в базе данных
     */
    void update(Route object);

    /**
     * Удаляет запись об объекте из базы данных
     */
    void delete(Route object);

    /**
     * Возвращает список объектов соответствующих всем записям в базе данных
     */
    Optional<List<Route>> getAll();

    Optional<List<Route>> getRoutesByDepartureAndArrivalStationsId(Long departureStationId, Long arrivalStationId);

    Optional<Route> getFirstTerminalStationOnRoute(Long routeId);

    Optional<Route> getLastTerminalStationOnRoute(Long routeId);

    Optional<Route> getStationOnRoute(Long stationId, Long routeId);
}
