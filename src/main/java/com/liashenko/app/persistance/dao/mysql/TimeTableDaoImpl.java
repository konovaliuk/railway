package com.liashenko.app.persistance.dao.mysql;

import com.liashenko.app.controller.utils.HttpParser;
import com.liashenko.app.persistance.dao.AbstractJDBCDao;
import com.liashenko.app.persistance.dao.Identified;
import com.liashenko.app.persistance.dao.TimeTableDao;
import com.liashenko.app.persistance.dao.exceptions.DAOException;
import com.liashenko.app.persistance.domain.TimeTable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class TimeTableDaoImpl extends AbstractJDBCDao implements TimeTableDao {
    private static final Logger classLogger = LogManager.getLogger(TimeTableDaoImpl.class);

    public TimeTableDaoImpl(Connection connection, ResourceBundle localeQueries) {
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

    public String getTimeTableForDepartureStationByDataAndRouteQuery() {
        return localeQueries.getString("select_timetable_for_station_and_route_and_date");
    }

    @Override
    public boolean isExists(Long key) {
        return super.isExists(key);
    }

    @Override
    protected List<TimeTable> parseResultSet(ResultSet rs) {
        if (rs == null) return Collections.emptyList();
        List<TimeTable> list = new ArrayList<>();
        try {
            while (rs.next()) {
                TimeTable timeTable = new TimeTable();
                timeTable.setId(rs.getLong("id"));
                timeTable.setStationId(rs.getLong("station_id"));
                timeTable.setDeparture(rs.getTimestamp("departure").toLocalDateTime());
                timeTable.setArrival(rs.getTimestamp("arrival").toLocalDateTime());
                timeTable.setRouteNumberId(rs.getLong("route_number_id"));
                list.add(timeTable);
            }
        } catch (SQLException e) {
            classLogger.error("Couldn't parse ResultSet", e);
            throw new DAOException(e);
        }
        return list;
    }

    @Override
    public void create(TimeTable object) {
        super.create(object);
    }

    @Override
    public Optional<TimeTable> persist(TimeTable object) {
        return super.persist(object).map(obj -> (TimeTable) obj);
    }

    @Override
    public void update(TimeTable object) {
        super.update(object);
    }

    @Override
    public void delete(TimeTable object) {
        super.delete(object);
    }

    @Override
    public Optional<TimeTable> getByPK(Long key) {
        return super.getByPK(key).map(obj -> (TimeTable) obj);
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, Identified object) {
//        try {
//            TimeTable timeTable = (TimeTable) object;
//        } catch (ClassCastException e) {
//            classLogger.error("Couldn't make PreparedStatement for INSERT", e);
//            throw new DAOException(e);
//        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Identified object) {
//        try {
//            TimeTable timeTable = (TimeTable) object;
//        } catch (ClassCastException e) {
//            classLogger.error("Couldn't make PreparedStatement for UPDATE", e);
//            throw new DAOException(e);
//        }
    }

    @Override
    public Optional<List<TimeTable>> getAll() {
        return super.getAll().map(list -> (List<TimeTable>) list);
    }

    @Override
    public Optional<TimeTable> getTimeTableForStationByDataAndRoute(Long stationId, Long routeId, LocalDate date) {
        return getTimeTableForStationByDataAndRoute(stationId, routeId, HttpParser.convertDateToDbString(date));
    }

    @Override
    public Optional<TimeTable> getTimeTableForStationByDataAndRoute(Long stationId, Long routeId, String date) {
        String sql = getTimeTableForDepartureStationByDataAndRouteQuery();
//        System.out.println("TimeTableDaoImpl.getTimeTableForStationByDataAndRoute : " + sql);
//        System.out.println("stationId = [" + stationId + "], routeId = [" + routeId + "], date = [" + HttpParser.convertDateToDbString(date) + "]");
        List<TimeTable> timeTableList;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, routeId);
            statement.setLong(2, stationId);
//            statement.setObject(3, date);
            statement.setString(3, date);
            ResultSet rs = statement.executeQuery();
            timeTableList = parseResultSet(rs);
        } catch (DAOException | SQLException e) {
            classLogger.error(e);
            throw new DAOException(e);
        }

        if (timeTableList == null || timeTableList.size() == 0) {
            return Optional.empty();
        } else if (timeTableList.size() == 1) {
            return Optional.ofNullable(timeTableList.get(0));
        } else {
            classLogger.error("Received more than one record.");
            throw new DAOException("Received more than one record.");
        }
    }
}
