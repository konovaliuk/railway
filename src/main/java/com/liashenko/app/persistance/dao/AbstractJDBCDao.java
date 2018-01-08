package com.liashenko.app.persistance.dao;


import com.liashenko.app.persistance.dao.exceptions.DAOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public abstract class AbstractJDBCDao<T extends Identified<PK>, PK extends Number> implements GenericJDBCDao<T, PK> {

    private static final Logger classLogger = LogManager.getLogger(AbstractJDBCDao.class);

    protected Connection connection;
    protected ResourceBundle localeQueries;

    public AbstractJDBCDao(Connection connection, ResourceBundle localeQueries) {
        this.connection = connection;
        this.localeQueries = localeQueries;
    }

    public abstract String getExistsQuery();

    /**
     * Возвращает sql запрос для получения всех записей.
     * <p/>
     * SELECT * FROM [Table]
     */
    public abstract String getSelectQuery();

    /**
     * Возвращает sql запрос для вставки новой записи в базу данных.
     * <p/>
     * INSERT INTO [Table] ([column, column, ...]) VALUES (?, ?, ...);
     */
    public abstract String getCreateQuery();

    /**
     * Возвращает sql запрос для обновления записи.
     * <p/>
     * UPDATE [Table] SET [column = ?, column = ?, ...] WHERE id = ?;
     */
    public abstract String getUpdateQuery();

    /**
     * Возвращает sql запрос для удаления записи из базы данных.
     * <p/>
     * DELETE FROM [Table] WHERE id= ?;
     */
    public abstract String getDeleteQuery();

    /**
     * Разбирает ResultSet и возвращает список объектов соответствующих содержимому ResultSet.
     */
    protected abstract List<T> parseResultSet(ResultSet rs);

    /**
     * Устанавливает аргументы insert запроса в соответствии со значением полей объекта object.
     */
    protected abstract void prepareStatementForInsert(PreparedStatement statement, T object);

    /**
     * Устанавливает аргументы update запроса в соответствии со значением полей объекта object.
     */
    protected abstract void prepareStatementForUpdate(PreparedStatement statement, T object);

    public boolean isExists(PK key) {
        int i = 0;
        String sql = getExistsQuery();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, key);
            ResultSet rs = statement.executeQuery();
            if (rs == null) return false;
            while (rs.next()) {
                i++;
            }
        } catch (SQLException e) {
            classLogger.error(e);
            throw new DAOException(e);
        }
        switch (i) {
            case 0:
                return false;
            case 1:
                return true;
            default:
                classLogger.error("Received more than one record.");
                throw new DAOException("Received more than one record.");
        }
    }


    @Override
    public void create(T object) {
        // Добавляем запись
        String sql = getCreateQuery();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            prepareStatementForInsert(statement, object);
            int count = statement.executeUpdate();
            if (count == 0) {
                throw new DAOException("The record isn't created.");
            }
        } catch (DAOException | SQLException e) {
            classLogger.error(e);
            throw new DAOException(e);
        }
    }

    @Override
    public Optional<T> persist(T object) {
        // Добавляем запись
        String sql = getCreateQuery();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            prepareStatementForInsert(statement, object);
            int count = statement.executeUpdate();
            if (count != 1) {
                classLogger.error("On persist modify more then 1 record: " + count);
                throw new DAOException("On persist modify more then 1 record: " + count);
            }
        } catch (DAOException | SQLException e) {
            throw new DAOException(e);
        }
        // Получаем только что вставленную запись
        sql = getSelectQuery() + " WHERE id = last_insert_id();";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            List<T> list = parseResultSet(rs);
            if ((list == null) || (list.size() != 1)) {
                classLogger.error("Exception on findByPK new persist data.");
                throw new DAOException("Exception on findByPK new persist data.");
            }
            return Optional.of(list.iterator().next());
        } catch (DAOException | SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public Optional<T> getByPK(PK key) {
        if (key == null) return Optional.empty();
        List<T> list;
        String sql = getSelectQuery();
        sql += " WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, key);
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
        } catch (DAOException | SQLException e) {
            classLogger.error(e);
            throw new DAOException(e);
        }
        if (list == null || list.size() == 0) {
            classLogger.error("Record with PK = " + key + " not found.");
            throw new DAOException("Record with PK = " + key + " not found.");
        }
        if (list.size() > 1) {
            classLogger.error("Received more than one record.");
            throw new DAOException("Received more than one record.");
        }
        return Optional.ofNullable(list.iterator().next());
    }

    @Override
    public void update(T object) {
        if (object.getId() == null) throw new DAOException("id is null");
        String sql = getUpdateQuery();
        try (PreparedStatement statement = connection.prepareStatement(sql);) {
            prepareStatementForUpdate(statement, object);
            int count = statement.executeUpdate();
            if (count != 1) {
                throw new DAOException("On update modify more then 1 record: " + count);
            }
        } catch (DAOException | SQLException e) {
            classLogger.error(e);
            throw new DAOException(e);
        }
    }

    @Override
    public void delete(T object) {
        if (object.getId() == null) throw new DAOException("id is null");
        String sql = getDeleteQuery();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, object.getId());
            int count = statement.executeUpdate();
            if (count != 1) {
                throw new DAOException("On delete modify more then 1 record: " + count);
            }
        } catch (DAOException | SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public Optional<List<T>> getAll() {
        String sql = getSelectQuery();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            return Optional.ofNullable(parseResultSet(rs));
        } catch (DAOException | SQLException e) {
            classLogger.error(e);
            throw new DAOException(e);
        }
    }
}
