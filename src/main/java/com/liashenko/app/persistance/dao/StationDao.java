package com.liashenko.app.persistance.dao;

import com.liashenko.app.persistance.domain.Station;

import java.util.List;
import java.util.Optional;

public interface StationDao {
    boolean isExists(Long key);

    /** Создает новую запись и соответствующий ей объект */
    void create(Station object);

    /** Создает новую запись, соответствующую объекту object */
    Optional<Station> persist(Station object);

    /** Возвращает объект соответствующий записи с первичным ключом key или null */
    Optional<Station> getByPK(Long key);

    /** Сохраняет состояние объекта group в базе данных */
    void update(Station object);

    /** Удаляет запись об объекте из базы данных */
    void delete(Station object);

    /** Возвращает список объектов соответствующих всем записям в базе данных */
    Optional<List<Station>> getAll();

    Optional<List<Station>> getStationsLike(String likePattern);
}
