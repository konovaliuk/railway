package com.liashenko.app.persistance.dao;


import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface GenericJDBCDao<T extends Identified<PK>, PK extends Serializable> {

    String getExistsQuery();

    /**
     * Возвращает sql запрос для получения всех записей.
     * <p/>
     * SELECT * FROM [Table]
     */
    String getSelectQuery();

    /**
     * Возвращает sql запрос для вставки новой записи в базу данных.
     * <p/>
     * INSERT INTO [Table] ([column, column, ...]) VALUES (?, ?, ...);
     */
    String getCreateQuery();

    /**
     * Возвращает sql запрос для обновления записи.
     * <p/>
     * UPDATE [Table] SET [column = ?, column = ?, ...] WHERE id = ?;
     */
    String getUpdateQuery();

    /**
     * Возвращает sql запрос для удаления записи из базы данных.
     * <p/>
     * DELETE FROM [Table] WHERE id= ?;
     */
    String getDeleteQuery();

    boolean isExists(PK key);

    /**
     * Создает новую запись и соответствующий ей объект
     */
    void create(T object);

    /**
     * Создает новую запись, соответствующую объекту object
     */
    Optional<T> persist(T object);

    /**
     * Возвращает объект соответствующий записи с первичным ключом key или null
     */
    Optional<T> getByPK(PK key);

    /**
     * Сохраняет состояние объекта group в базе данных
     */
    void update(T object);

    /**
     * Удаляет запись об объекте из базы данных
     */
    void delete(T object);

    /**
     * Возвращает список объектов соответствующих всем записям в базе данных
     */
    Optional<List<T>> getAll();
}
