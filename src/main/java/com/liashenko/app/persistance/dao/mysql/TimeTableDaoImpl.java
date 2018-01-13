package com.liashenko.app.persistance.dao.mysql;

import com.liashenko.app.controller.utils.HttpParser;
import com.liashenko.app.persistance.dao.AbstractJDBCDao;
import com.liashenko.app.persistance.dao.Identified;
import com.liashenko.app.persistance.dao.TimeTableDao;
import com.liashenko.app.persistance.dao.exceptions.DAOException;
import com.liashenko.app.persistance.domain.TimeTable;
import com.liashenko.app.persistance.result_parser.ResultSetParser;
import com.liashenko.app.persistance.result_parser.ResultSetParserException;
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
                try {
                    TimeTable timeTable = ResultSetParser.fillBeanWithResultData(rs, TimeTable.class,
                            localeQueries.getString("locale_suffix"));
                    System.out.println("TimeTableDaoImpl.parseResultSet: " + timeTable);
                    list.add(timeTable);
                } catch (ResultSetParserException ex) {
                    classLogger.error(ex);
                }
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
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Identified object) {
    }

    @Override
    public Optional<List<TimeTable>> getAll() {
        return super.getAll().map(list -> (List<TimeTable>) list);
    }

    @Override
    public Optional<TimeTable> getTimeTableForStationByDataAndRoute(Long stationId, Long routeId, LocalDate date) {
        String sql = getTimeTableForDepartureStationByDataAndRouteQuery();
        List<TimeTable> timeTableList;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, routeId);
            statement.setLong(2, stationId);
            statement.setString(3, HttpParser.convertDateToDbString(date));
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
