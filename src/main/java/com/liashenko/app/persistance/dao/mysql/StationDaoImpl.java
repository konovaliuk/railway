package com.liashenko.app.persistance.dao.mysql;

import com.liashenko.app.persistance.dao.AbstractJDBCDao;
import com.liashenko.app.persistance.dao.Identified;
import com.liashenko.app.persistance.dao.StationDao;
import com.liashenko.app.persistance.dao.exceptions.DAOException;
import com.liashenko.app.persistance.domain.Station;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class StationDaoImpl extends AbstractJDBCDao implements StationDao {
    private static final Logger classLogger = LogManager.getLogger(StationDaoImpl.class);

    public StationDaoImpl(Connection connection, ResourceBundle localeQueries) {
        super(connection, localeQueries);
    }

    @Override
    public String getExistsQuery() {
        return localeQueries.getString("");
    }

    @Override
    public String getSelectQuery() {
        return localeQueries.getString("select_all_from_station_tbl");
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


    public String getStationsLikeQuery() {
        return localeQueries.getString("select_from_station_tbl_like");
    }

    @Override
    public boolean isExists(Long key) {
        return super.isExists(key);
    }

    @Override
    protected List<Station> parseResultSet(ResultSet rs) {
        if (rs == null) return Collections.emptyList();
        List<Station> list = new ArrayList<>();
        try {
            while (rs.next()) {
                Station station = new Station();
                station.setId(rs.getLong("id"));
                station.setCity(rs.getString("city" + localeQueries.getString("locale_suffix")));
                station.setName(rs.getString("name" + localeQueries.getString("locale_suffix")));
                list.add(station);
            }
        } catch (SQLException e) {
            classLogger.error("Couldn't parse ResultSet", e);
            throw new DAOException(e);
        }
        return list;
    }

    @Override
    public void create(Station object) {
        super.create(object);
    }

    @Override
    public Optional<Station> persist(Station object) {
        return super.persist(object).map(obj -> (Station) obj);
    }

    @Override
    public void update(Station object) {
        super.update(object);
    }

    @Override
    public void delete(Station object) {
        super.delete(object);
    }

    @Override
    public Optional<Station> getByPK(Long key) {
        return super.getByPK(key).map(obj -> (Station) obj);
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, Identified object) {
//        try {
//            Station station = (Station) object;
//        } catch (ClassCastException e) {
//            classLogger.error("Couldn't make PreparedStatement for INSERT", e);
//            throw new DAOException(e);
//        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Identified object) {
//        try {
//            Station user = (Station) object;
//        } catch (ClassCastException e) {
//            classLogger.error("Couldn't make PreparedStatement for UPDATE", e);
//            throw new DAOException(e);
//        }
    }

    @Override
    public Optional<List<Station>> getAll() {
        return super.getAll().map(list -> (List<Station>) list);
    }

    public Optional<List<Station>> getStationsLike(String likePattern) {
        Optional<List<Station>> listOpt;
        String sql = getStationsLikeQuery();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, likePattern);
            ResultSet rs = statement.executeQuery();
            listOpt = Optional.ofNullable(parseResultSet(rs));
        } catch (DAOException | SQLException e) {
            classLogger.error(e);
            throw new DAOException(e);
        }
        return listOpt;
    }
}
