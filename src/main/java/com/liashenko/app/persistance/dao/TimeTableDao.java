package com.liashenko.app.persistance.dao;

import com.liashenko.app.persistance.domain.TimeTable;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TimeTableDao extends GenericJDBCDao {
    boolean isExists(Long key);

    /** Создает новую запись и соответствующий ей объект */
    void create(TimeTable object);

    /** Создает новую запись, соответствующую объекту object */
    Optional<TimeTable> persist(TimeTable object);

    /** Возвращает объект соответствующий записи с первичным ключом key или null */
    Optional<TimeTable> getByPK(Long key);

    /** Сохраняет состояние объекта group в базе данных */
    void update(TimeTable object);

    /** Удаляет запись об объекте из базы данных */
    void delete(TimeTable object);

    /** Возвращает список объектов соответствующих всем записям в базе данных */
    Optional<List<TimeTable>> getAll();

    Optional<TimeTable> getTimeTableForStationByDataAndRoute(Long departureStationId, Long routeId, LocalDate date);

    Optional<TimeTable> getTimeTableForStationByDataAndRoute(Long departureStationId, Long routeId, String date);
}
