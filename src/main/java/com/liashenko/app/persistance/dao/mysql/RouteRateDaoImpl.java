package com.liashenko.app.persistance.dao.mysql;

import com.liashenko.app.persistance.dao.AbstractJDBCDao;
import com.liashenko.app.persistance.dao.DAOException;
import com.liashenko.app.persistance.dao.Identified;
import com.liashenko.app.persistance.dao.RouteRateDao;
import com.liashenko.app.persistance.domain.RouteRate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class RouteRateDaoImpl  extends AbstractJDBCDao implements RouteRateDao {
    private static final Logger classLogger = LogManager.getLogger(RouteRateDaoImpl.class);

    public RouteRateDaoImpl(Connection connection, ResourceBundle localeQueries) {
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

    public String getByRouteIdQuery() {
        return localeQueries.getString("select_by_route_id");
    }


    @Override
    public boolean isExists(Long key) {
        return super.isExists(key);
    }

    @Override
    protected List<RouteRate> parseResultSet(ResultSet rs) {
        if (rs == null) return Collections.emptyList();
        List<RouteRate> list = new ArrayList<>();
        try {
            while (rs.next()) {
                RouteRate routeRate = new RouteRate();
                routeRate.setId(rs.getInt("id"));
                routeRate.setRate(rs.getFloat("rate"));
                list.add(routeRate);
            }
        } catch (SQLException e) {
            classLogger.error("Couldn't parse ResultSet", e);
            throw new DAOException(e);
        }
        return list;
    }

    @Override
    public void create(RouteRate object) {
        super.create(object);
    }

    @Override
    public Optional<RouteRate> persist(RouteRate object){
        return super.persist(object).map(obj -> (RouteRate) obj);
    }

    @Override
    public void update(RouteRate object) {
        super.update(object);
    }

    @Override
    public void delete(RouteRate object) {
        super.delete(object);
    }

    @Override
    public Optional<RouteRate> getByPK(Long key) {
        return super.getByPK(key).map(obj -> (RouteRate) obj);
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, Identified object) {
        try {
            RouteRate routeRate = (RouteRate) object;
        } catch (ClassCastException e) {
            classLogger.error("Couldn't make PreparedStatement for INSERT", e);
            throw new DAOException(e);
        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Identified object) {
        try {
            RouteRate routeRate = (RouteRate) object;
        } catch (ClassCastException e) {
            classLogger.error("Couldn't make PreparedStatement for UPDATE", e);
            throw new DAOException(e);
        }
    }

    @Override
    public Optional<List<RouteRate>> getAll() {
        return super.getAll().map(list -> (List<RouteRate>) list);
    }

    @Override
    public Optional<RouteRate> getByRouteId(Long routeId) {
        String sql = getByRouteIdQuery();
        List<RouteRate> routeRateList;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, routeId);
            ResultSet rs = statement.executeQuery();
            routeRateList = parseResultSet(rs);
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        if (routeRateList == null || routeRateList.size() == 0) {
            return Optional.empty();
        } else if (routeRateList.size() == 1) {
            return Optional.ofNullable(routeRateList.get(0));
        } else {
            throw new DAOException("Received more than one record.");
        }
    }
}
