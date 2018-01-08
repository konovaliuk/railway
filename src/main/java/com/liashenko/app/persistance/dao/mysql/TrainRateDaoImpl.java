package com.liashenko.app.persistance.dao.mysql;

import com.liashenko.app.persistance.dao.AbstractJDBCDao;
import com.liashenko.app.persistance.dao.Identified;
import com.liashenko.app.persistance.dao.TrainRateDao;
import com.liashenko.app.persistance.dao.exceptions.DAOException;
import com.liashenko.app.persistance.domain.TrainRate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class TrainRateDaoImpl extends AbstractJDBCDao implements TrainRateDao {
    private static final Logger classLogger = LogManager.getLogger(TrainRateDaoImpl.class);

    public TrainRateDaoImpl(Connection connection, ResourceBundle localeQueries) {
        super(connection, localeQueries);
    }

    @Override
    public String getExistsQuery() {
        return localeQueries.getString("");
    }

    @Override
    public String getSelectQuery() {
        return localeQueries.getString("");
    }

    @Override
    public String getCreateQuery() {
        return localeQueries.getString("");
    }

    @Override
    public String getUpdateQuery() {
        return localeQueries.getString("");
    }

    @Override
    public String getDeleteQuery() {
        return localeQueries.getString("");
    }

    @Override
    public boolean isExists(Long key) {
        return super.isExists(key);
    }

    @Override
    protected List<TrainRate> parseResultSet(ResultSet rs) {
        if (rs == null) return Collections.emptyList();
        List<TrainRate> list = new ArrayList<>();
        try {
            while (rs.next()) {
                TrainRate trainRate = new TrainRate();
                trainRate.setId(rs.getLong("id"));
                trainRate.setRate(rs.getFloat("rate"));
                trainRate.setTrainId(rs.getLong("train_id"));

                list.add(trainRate);
            }
        } catch (SQLException e) {
            classLogger.error("Couldn't parse ResultSet", e);
            throw new DAOException(e);
        }
        return list;
    }

    @Override
    public void create(TrainRate object) {
        super.create(object);
    }

    @Override
    public Optional<TrainRate> persist(TrainRate object) {
        return super.persist(object).map(obj -> (TrainRate) obj);
    }

    @Override
    public void update(TrainRate object) {
        super.update(object);
    }

    @Override
    public void delete(TrainRate object) {
        super.delete(object);
    }

    @Override
    public Optional<TrainRate> getByPK(Long key) {
        return super.getByPK(key).map(obj -> (TrainRate) obj);
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, Identified object) {
//        try {
//            TrainRate trainRate = (TrainRate) object;
//        } catch (ClassCastException e) {
//            classLogger.error("Couldn't make PreparedStatement for INSERT", e);
//            throw new DAOException(e);
//        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Identified object) {
//        try {
//            TrainRate trainRate = (TrainRate) object;
//        } catch (ClassCastException e) {
//            classLogger.error("Couldn't make PreparedStatement for UPDATE", e);
//            throw new DAOException(e);
//        }
    }

    @Override
    public Optional<List<TrainRate>> getAll() {
        return super.getAll().map(list -> (List<TrainRate>) list);
    }
}
