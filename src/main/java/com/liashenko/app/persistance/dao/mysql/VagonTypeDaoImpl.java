package com.liashenko.app.persistance.dao.mysql;

import com.liashenko.app.persistance.dao.AbstractJDBCDao;
import com.liashenko.app.persistance.dao.Identified;
import com.liashenko.app.persistance.dao.VagonTypeDao;
import com.liashenko.app.persistance.dao.exceptions.DAOException;
import com.liashenko.app.persistance.domain.VagonType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class VagonTypeDaoImpl extends AbstractJDBCDao implements VagonTypeDao {
    private static final Logger classLogger = LogManager.getLogger(VagonTypeDaoImpl.class);

    public VagonTypeDaoImpl(Connection connection, ResourceBundle localeQueries) {
        super(connection, localeQueries);
    }

    @Override
    public String getExistsQuery() {
        return localeQueries.getString("");
    }

    @Override
    public String getSelectQuery() {
        return localeQueries.getString("select_all_from_vagon_type_tbl");
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
    protected List<VagonType> parseResultSet(ResultSet rs) {
        if (rs == null) return Collections.emptyList();
        List<VagonType> list = new ArrayList<>();
        try {
            while (rs.next()) {
                VagonType vagonType = new VagonType();
                vagonType.setId(rs.getInt("id"));
                vagonType.setTypeName(rs.getString("type_name" + localeQueries.getString("locale_suffix")));
                vagonType.setPlacesCount(rs.getInt("places_count"));
                list.add(vagonType);
            }
        } catch (SQLException e) {
            classLogger.error("Couldn't parse ResultSet", e);
            throw new DAOException(e);
        }
        return list;
    }

    @Override
    public void create(VagonType object) {
        super.create(object);
    }

    @Override
    public Optional<VagonType> persist(VagonType object) {
        return super.persist(object).map(obj -> (VagonType) obj);
    }

    @Override
    public void update(VagonType object) {
        super.update(object);
    }

    @Override
    public void delete(VagonType object) {
        super.delete(object);
    }

    @Override
    public Optional<VagonType> getByPK(Integer key) {
        return super.getByPK(key).map(obj -> (VagonType) obj);
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, Identified object) {
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Identified object) {
    }

    @Override
    public Optional<List<VagonType>> getAll() {
        return super.getAll().map(list -> (List<VagonType>) list);
    }
}
