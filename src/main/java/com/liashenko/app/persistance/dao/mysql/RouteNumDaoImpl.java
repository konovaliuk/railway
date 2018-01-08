package com.liashenko.app.persistance.dao.mysql;

import com.liashenko.app.persistance.dao.AbstractJDBCDao;
import com.liashenko.app.persistance.dao.Identified;
import com.liashenko.app.persistance.dao.RouteNumDao;
import com.liashenko.app.persistance.dao.exceptions.DAOException;
import com.liashenko.app.persistance.domain.RouteNum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class RouteNumDaoImpl extends AbstractJDBCDao implements RouteNumDao {
    private static final Logger classLogger = LogManager.getLogger(RouteNumDaoImpl.class);

    public RouteNumDaoImpl(Connection connection, ResourceBundle localeQueries) {
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
    protected List<RouteNum> parseResultSet(ResultSet rs) {
        if (rs == null) return Collections.emptyList();
        List<RouteNum> list = new ArrayList<>();
        try {
            while (rs.next()) {
                RouteNum routeNum = new RouteNum();
                routeNum.setId(rs.getLong("id"));
                routeNum.setNumber(rs.getInt("number"));
                list.add(routeNum);
            }
        } catch (SQLException e) {
            classLogger.error("Couldn't parse ResultSet", e);
            throw new DAOException(e);
        }
        return list;
    }

    @Override
    public void create(RouteNum object) {
        super.create(object);
    }

    @Override
    public Optional<RouteNum> persist(RouteNum object) {
        return super.persist(object).map(obj -> (RouteNum) obj);
    }

    @Override
    public void update(RouteNum object) {
        super.update(object);
    }

    @Override
    public void delete(RouteNum object) {
        super.delete(object);
    }

    @Override
    public Optional<RouteNum> getByPK(Long key) {
        return super.getByPK(key).map(obj -> (RouteNum) obj);
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, Identified object) {
//        try {
//            RouteNum routeNum = (RouteNum) object;
//        } catch (ClassCastException e) {
//            classLogger.error("Couldn't make PreparedStatement for INSERT", e);
//            throw new DAOException(e);
//        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Identified object) {
//        try {
//            RouteNum routeNum = (RouteNum) object;
//        } catch (ClassCastException e) {
//            classLogger.error("Couldn't make PreparedStatement for UPDATE", e);
//            throw new DAOException(e);
//        }
    }

    @Override
    public Optional<List<RouteNum>> getAll() {
        return super.getAll().map(list -> (List<RouteNum>) list);
    }
}
