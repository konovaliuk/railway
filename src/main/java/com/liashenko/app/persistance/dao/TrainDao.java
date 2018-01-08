package com.liashenko.app.persistance.dao;


import com.liashenko.app.persistance.domain.Train;

import java.util.List;
import java.util.Optional;

public interface TrainDao extends GenericJDBCDao {
    boolean isExists(Long key);

    /**
     * Создает новую запись и соответствующий ей объект
     */
    void create(Train object);

    /**
     * Создает новую запись, соответствующую объекту object
     */
    Optional<Train> persist(Train object);

    /**
     * Возвращает объект соответствующий записи с первичным ключом key или null
     */
    Optional<Train> getByPK(Long key);

    /**
     * Сохраняет состояние объекта group в базе данных
     */
    void update(Train object);

    /**
     * Удаляет запись об объекте из базы данных
     */
    void delete(Train object);

    /**
     * Возвращает список объектов соответствующих всем записям в базе данных
     */
    Optional<List<Train>> getAll();

    Optional<Train> getByRoute(Long routeId);
}
