package com.liashenko.app.persistance.dao.mysql;

import com.liashenko.app.persistance.dao.AbstractJDBCDao;
import com.liashenko.app.persistance.dao.Identified;
import com.liashenko.app.persistance.dao.RouteDao;
import com.liashenko.app.persistance.dao.exceptions.DAOException;
import com.liashenko.app.persistance.domain.Route;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class RouteDaoImpl extends AbstractJDBCDao implements RouteDao {
    private static final Logger classLogger = LogManager.getLogger(RouteDaoImpl.class);

    public RouteDaoImpl(Connection connection, ResourceBundle localeQueries) {
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

    public String getFirstTerminalStationOnRouteQuery() {
        return localeQueries.getString("select_first_terminal_station_on_route");
    }

    public String getLastTerminalStationOnRouteQuery() {
        return localeQueries.getString("select_last_terminal_station_on_route");
    }


    public String getRoutesByDepartureAndArrivalQuery() {
        return localeQueries.getString("select_routes_by_departure_and_arrival_stations_from_route_tbl");
    }

    public String getStationOnRouteQuery() {
        return localeQueries.getString("select_station_on_route");
    }

    @Override
    public boolean isExists(Long key) {
        return super.isExists(key);
    }

    @Override
    protected List<Route> parseResultSet(ResultSet rs) {
        if (rs == null) return Collections.emptyList();
        List<Route> list = new ArrayList<>();
        try {
            while (rs.next()) {
                Route route = new Route();
                route.setId(rs.getLong("id"));
                route.setStationId(rs.getLong("station_id"));
                route.setRouteNumberId(rs.getLong("rout_number_id"));
                route.setDistance(rs.getFloat("distance"));
                list.add(route);
            }
        } catch (SQLException e) {
            classLogger.error("Couldn't parse ResultSet", e);
            throw new DAOException(e);
        }
        return list;
    }

    @Override
    public void create(Route object) {
        super.create(object);
    }

    @Override
    public Optional<Route> persist(Route object) {
        return super.persist(object).map(obj -> (Route) obj);
    }

    @Override
    public void update(Route object) {
        super.update(object);
    }

    @Override
    public void delete(Route object) {
        super.delete(object);
    }

    @Override
    public Optional<Route> getByPK(Long key) {
        return super.getByPK(key).map(obj -> (Route) obj);
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, Identified object) {
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Identified object) {
    }

    @Override
    public Optional<List<Route>> getAll() {
        return super.getAll().map(list -> (List<Route>) list);
    }

    @Override
    public Optional<List<Route>> getRoutesByDepartureAndArrivalStationsId(Long departureStationId, Long arrivalStationId) {
        String sql = getRoutesByDepartureAndArrivalQuery();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, departureStationId);
            statement.setLong(2, arrivalStationId);
            ResultSet rs = statement.executeQuery();
            return Optional.ofNullable(parseResultSet(rs));
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    public Optional<Route> getStationOnRoute(Long stationId, Long routeId) {
        List<Route> routeList;
        String sql = getStationOnRouteQuery();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, stationId);
            statement.setLong(2, routeId);
            ResultSet rs = statement.executeQuery();
            routeList = parseResultSet(rs);

            if (routeList == null || routeList.size() == 0) {
                return Optional.empty();
            } else if (routeList.size() == 1) {
                return Optional.ofNullable(routeList.get(0));
            } else {
                throw new DAOException("Received more than one record.");
            }
        } catch (DAOException | SQLException e) {
            classLogger.error(e);
            throw new DAOException(e);
        }


    }

    private Optional<Route> getStationFromRoute(Connection conn, String sql, Long routeId) {
        List<Route> routeList;
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setLong(1, routeId);
            ResultSet rs = statement.executeQuery();
            routeList = parseResultSet(rs);
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        if (routeList == null || routeList.size() == 0) {
            return Optional.empty();
        } else if (routeList.size() == 1) {
            return Optional.ofNullable(routeList.get(0));
        } else {
            throw new DAOException("Received more than one record.");
        }
    }

    @Override
    public Optional<Route> getFirstTerminalStationOnRoute(Long routeId) {
        String sql = getFirstTerminalStationOnRouteQuery();
        return getStationFromRoute(connection, sql, routeId);
    }

    @Override
    public Optional<Route> getLastTerminalStationOnRoute(Long routeId) {
        String sql = getLastTerminalStationOnRouteQuery();
        return getStationFromRoute(connection, sql, routeId);
    }
}
