package com.liashenko.app.persistance.dao.mysql;

import com.liashenko.app.persistance.dao.AbstractJDBCDao;
import com.liashenko.app.persistance.dao.DAOException;
import com.liashenko.app.persistance.dao.Identified;
import com.liashenko.app.persistance.dao.TrainDao;
import com.liashenko.app.persistance.domain.Train;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class TrainDaoImpl extends AbstractJDBCDao implements TrainDao {
    private static final Logger classLogger = LogManager.getLogger(TrainDaoImpl.class);

    public TrainDaoImpl(Connection connection, ResourceBundle localeQueries) {
        super(connection, localeQueries);
    }

    @Override
    public String getExistsQuery() {
        return localeQueries.getString("");
    }

    @Override
    public String getSelectQuery() {
        return localeQueries.getString("select_all_from_train_tbl");
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

    public String getByRouteQuery(){
        return localeQueries.getString("select_train_by_route");
    }

    @Override
    public boolean isExists(Long key) {
        return super.isExists(key);
    }

    @Override
    protected List<Train> parseResultSet(ResultSet rs) {
        if (rs == null) return Collections.emptyList();
        List<Train> list = new ArrayList<>();
        try {
            while (rs.next()) {
                Train train = new Train();
                train.setId(rs.getLong("id"));
                train.setNumber(rs.getString("vagon_number"));
                train.setRouteNumId(rs.getLong("route_num_id"));
                train.setVagonCount(rs.getInt("vagon_count"));
                list.add(train);
            }
        } catch (SQLException e) {
            classLogger.error("Couldn't parse ResultSet", e);
            throw new DAOException(e);
        }
        return list;
    }

    @Override
    public void create(Train object) {
        super.create(object);
    }

    @Override
    public Optional<Train> persist(Train object){
        return super.persist(object).map(obj -> (Train) obj);
    }

    @Override
    public void update(Train object) {
        super.update(object);
    }

    @Override
    public void delete(Train object) {
        super.delete(object);
    }

    @Override
    public Optional<Train> getByPK(Long key) {
        return super.getByPK(key).map(obj -> (Train) obj);
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, Identified object) {
        try {
            Train train = (Train) object;
        } catch (ClassCastException e) {
            classLogger.error("Couldn't make PreparedStatement for INSERT", e);
            throw new DAOException(e);
        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Identified object) {
        try {
            Train train = (Train) object;
        } catch (ClassCastException e) {
            classLogger.error("Couldn't make PreparedStatement for UPDATE", e);
            throw new DAOException(e);
        }
    }

    @Override
    public Optional<List<Train>> getAll() {
        return super.getAll().map(list -> (List<Train>) list);
    }

    @Override
    public Optional<Train> getByRoute(Long routeId){
        String sql = getByRouteQuery();
        List<Train> userList;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, routeId);
            ResultSet rs = statement.executeQuery();
            userList = parseResultSet(rs);
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        if (userList == null || userList.size() == 0) {
            return Optional.empty();
        } else if (userList.size() == 1) {
            return Optional.ofNullable(userList.get(0));
        } else {
            throw new DAOException("Received more than one record.");
        }

    }
}
