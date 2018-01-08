package com.liashenko.app.persistance.dao.mysql;

import com.liashenko.app.persistance.dao.AbstractJDBCDao;
import com.liashenko.app.persistance.dao.Identified;
import com.liashenko.app.persistance.dao.PricePerKmForVagonDao;
import com.liashenko.app.persistance.dao.exceptions.DAOException;
import com.liashenko.app.persistance.domain.PricePerKmForVagon;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class PricePerKmForVagonDaoImpl extends AbstractJDBCDao implements PricePerKmForVagonDao {
    private static final Logger classLogger = LogManager.getLogger(PricePerKmForVagonDaoImpl.class);

    public PricePerKmForVagonDaoImpl(Connection connection, ResourceBundle localeQueries) {
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

    public String getPricePerKmForVagonQuery() {
        return localeQueries.getString("select_price_per_km_for_vagon_type");
    }

    @Override
    protected List<PricePerKmForVagon> parseResultSet(ResultSet rs) {
        if (rs == null) return Collections.emptyList();
        List<PricePerKmForVagon> list = new ArrayList<>();
        try {
            while (rs.next()) {
                PricePerKmForVagon pricePerKmForVagon = new PricePerKmForVagon();
                pricePerKmForVagon.setId(rs.getLong("id"));
                pricePerKmForVagon.setPrice(rs.getDouble("price"));
                pricePerKmForVagon.setVagonTypeId(rs.getInt("vagon_type_id"));
                list.add(pricePerKmForVagon);
            }
        } catch (DAOException | SQLException e) {
            classLogger.error("Couldn't parse ResultSet", e);
            throw new DAOException(e);
        }
        return list;
    }

    @Override
    public void create(PricePerKmForVagon object) {
        super.create(object);
    }

    @Override
    public Optional<PricePerKmForVagon> persist(PricePerKmForVagon object) {
        return super.persist(object).map(obj -> (PricePerKmForVagon) obj);
    }

    @Override
    public void update(PricePerKmForVagon object) {
        super.update(object);
    }

    @Override
    public void delete(PricePerKmForVagon object) {
        super.delete(object);
    }

    @Override
    public Optional<PricePerKmForVagon> getByPK(Long key) {
        return super.getByPK(key).map(obj -> (PricePerKmForVagon) obj);
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, Identified object) {
        try {
            PricePerKmForVagon pricePerKmForVagon = (PricePerKmForVagon) object;
        } catch (ClassCastException e) {
            classLogger.error("Couldn't make PreparedStatement for INSERT", e);
            throw new DAOException(e);
        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Identified object) {
        try {
            PricePerKmForVagon pricePerKmForVagon = (PricePerKmForVagon) object;
        } catch (ClassCastException e) {
            classLogger.error("Couldn't make PreparedStatement for UPDATE", e);
            throw new DAOException(e);
        }
    }

    @Override
    public Optional<List<PricePerKmForVagon>> getAll() {
        return super.getAll().map(list -> (List<PricePerKmForVagon>) list);
    }

    @Override
    public Optional<PricePerKmForVagon> getPricePerKmForVagon(Integer vagonTypeId) {
        String sql = getPricePerKmForVagonQuery();
        List<PricePerKmForVagon> priceList;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, vagonTypeId);
            ResultSet rs = statement.executeQuery();
            priceList = parseResultSet(rs);
        } catch (DAOException | SQLException e) {
            classLogger.error(e);
            throw new DAOException(e);
        }
        if (priceList == null || priceList.size() == 0) {
            return Optional.empty();
        } else if (priceList.size() == 1) {
            return Optional.ofNullable(priceList.get(0));
        } else {
            classLogger.error("Received more than one record.");
            throw new DAOException("Received more than one record.");
        }
    }
}
