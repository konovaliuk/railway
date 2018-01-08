package com.liashenko.app.persistance.dao.mysql;

import com.liashenko.app.persistance.dao.AbstractJDBCDao;
import com.liashenko.app.persistance.dao.Identified;
import com.liashenko.app.persistance.dao.RoleDao;
import com.liashenko.app.persistance.dao.exceptions.DAOException;
import com.liashenko.app.persistance.domain.Role;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class RoleDaoImpl extends AbstractJDBCDao implements RoleDao {
    private static final Logger classLogger = LogManager.getLogger(RoleDaoImpl.class);

    public RoleDaoImpl(Connection connection, ResourceBundle localeQueries) {
        super(connection, localeQueries);
    }

    @Override
    public String getExistsQuery() {
        return localeQueries.getString("is_id_exists_in_role_tbl");
    }

    @Override
    public String getSelectQuery() {
        return localeQueries.getString("select_all_from_role_tbl");
    }

    @Override
    public String getCreateQuery() {
        return localeQueries.getString("create_in_role_tbl");
    }

    @Override
    public String getUpdateQuery() {
        return localeQueries.getString("update_in_role_tbl");
    }

    @Override
    public String getDeleteQuery() {
        return localeQueries.getString("delete_from_role_tbl");
    }

    @Override
    public boolean isExists(Long key) {
        return super.isExists(key);
    }

    @Override
    protected List<Role> parseResultSet(ResultSet rs) {
        if (rs == null) return Collections.emptyList();
        List<Role> list = new ArrayList<>();
        try {
            while (rs.next()) {
                Role role = new Role();
                role.setId(rs.getLong("id"));
                role.setName(rs.getString("name" + localeQueries.getString("locale_suffix")));
                list.add(role);
            }
        } catch (SQLException e) {
            classLogger.error("Couldn't parse ResultSet", e);
            throw new DAOException(e);
        }
        return list;
    }

    @Override
    public void create(Role role) {
        super.create(role);
    }

    @Override
    public Optional<Role> persist(Role object) {
        return super.persist(object).map(obj -> (Role) obj);
    }

    @Override
    public void update(Role object) {
        super.update(object);
    }

    @Override
    public void delete(Role object) {
        super.delete(object);
    }

    @Override
    public Optional<Role> getByPK(Long key) {
        return super.getByPK(key).map(obj -> (Role) obj);
    }

    @Override
//  INSERT INTO railway.role (name) VALUES (?)
    protected void prepareStatementForInsert(PreparedStatement statement, Identified object) {
//        try {
//            Role role = (Role) object;
//            statement.setString(1, role.getName());
//        } catch (ClassCastException | SQLException e) {
//            classLogger.error("Couldn't make PreparedStatement for INSERT", e);
//            throw new DAOException(e);
//        }
    }

    @Override
//    UPDATE railway.password SET name=? WHERE id= ?
    protected void prepareStatementForUpdate(PreparedStatement statement, Identified object) {
//        try {
//            Role role = (Role) object;
//            statement.setString(1, role.getName());
//            statement.setLong(2, role.getId());
//        } catch (ClassCastException | SQLException e) {
//            classLogger.error("Couldn't make PreparedStatement for UPDATE", e);
//            throw new DAOException(e);
//        }
    }

    @Override
    public Optional<List<Role>> getAll() {
        return super.getAll().map(list -> (List<Role>) list);
    }
}
