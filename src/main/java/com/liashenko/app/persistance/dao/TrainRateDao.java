package com.liashenko.app.persistance.dao;

import com.liashenko.app.persistance.domain.TrainRate;

import java.util.List;
import java.util.Optional;

public interface TrainRateDao extends GenericJDBCDao {
    boolean isExists(Long key);

    /** Создает новую запись и соответствующий ей объект */
    void create(TrainRate object);

    /** Создает новую запись, соответствующую объекту object */
    Optional<TrainRate> persist(TrainRate object);

    /** Возвращает объект соответствующий записи с первичным ключом key или null */
    Optional<TrainRate> getByPK(Long key);

    /** Сохраняет состояние объекта group в базе данных */
    void update(TrainRate object);

    /** Удаляет запись об объекте из базы данных */
    void delete(TrainRate object);

    /** Возвращает список объектов соответствующих всем записям в базе данных */
    Optional<List<TrainRate>> getAll();
}
