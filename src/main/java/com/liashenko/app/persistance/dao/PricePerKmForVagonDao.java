package com.liashenko.app.persistance.dao;

import com.liashenko.app.persistance.domain.PricePerKmForVagon;

import java.util.List;
import java.util.Optional;

public interface PricePerKmForVagonDao extends GenericJDBCDao {
    boolean isExists(Long key);

    /**
     * Создает новую запись и соответствующий ей объект
     */
    void create(PricePerKmForVagon object);

    /**
     * Создает новую запись, соответствующую объекту object
     */
    Optional<PricePerKmForVagon> persist(PricePerKmForVagon object);

    /**
     * Возвращает объект соответствующий записи с первичным ключом key или null
     */
    Optional<PricePerKmForVagon> getByPK(Long key);

    /**
     * Сохраняет состояние объекта group в базе данных
     */
    void update(PricePerKmForVagon object);

    /**
     * Удаляет запись об объекте из базы данных
     */
    void delete(PricePerKmForVagon object);

    /**
     * Возвращает список объектов соответствующих всем записям в базе данных
     */
    Optional<List<PricePerKmForVagon>> getAll();

    Optional<PricePerKmForVagon> getPricePerKmForVagon(Integer vagonTypeId);
}
