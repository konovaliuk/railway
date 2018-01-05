package com.liashenko.app.persistance.dao.mysql;

import com.liashenko.app.persistance.dao.AbstractJDBCDao;
import com.liashenko.app.persistance.dao.Identified;
import com.liashenko.app.persistance.dao.PasswordDao;
import com.liashenko.app.persistance.dao.DAOException;
import com.liashenko.app.persistance.domain.Password;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class PasswordDaoImpl extends AbstractJDBCDao implements PasswordDao {

    private static final Logger classLogger = LogManager.getLogger(PasswordDaoImpl.class);

    public PasswordDaoImpl(Connection connection, ResourceBundle localeQueries) {
        super(connection, localeQueries);
    }

    public String getExistsQuery() {
        return localeQueries.getString("is_id_exists_in_password_tbl");
    }

    @Override
    public String getSelectQuery() {
        return localeQueries.getString("select_all_from_password_tbl");
    }

    @Override
    public String getCreateQuery() {
        return localeQueries.getString("create_in_password_tbl");
    }

    @Override
    public String getUpdateQuery() {
        return localeQueries.getString("update_in_password_tbl");
    }

    @Override
    public String getDeleteQuery() {
        return localeQueries.getString("delete_from_password_tbl");
    }

    @Override
    protected List<Password> parseResultSet(ResultSet rs){
        if (rs == null) return Collections.emptyList();
        List<Password> list = new ArrayList<>();
        try {
            while (rs.next()){
                Password password = new Password();
                password.setId(rs.getLong("id"));
                password.setPassword(rs.getBytes("password"));
                password.setSalt(rs.getBytes("salt"));
                password.setAlgorithm(rs.getString("algorithm"));
                password.setIterations(rs.getInt("iterations"));
                list.add(password);
            }
        } catch (SQLException e) {
            classLogger.error("Couldn't parse ResultSet", e);
            throw new DAOException(e);
        }
        return list;
    }

    @Override
    public boolean isExists(Long key) {
        return super.isExists(key);
    }

    @Override
    public void create(Password password){
        super.create(password);
    }

    @Override
    public Optional<Password> persist(Password object){
        return super.persist(object).map(obj -> (Password) obj);
    }

    @Override
    public void update(Password object){
        super.update(object);
    }

    @Override
    public void delete(Password object) {
        super.delete(object);
    }

    @Override
    public Optional<Password> getByPK(Long key){
        return super.getByPK(key).map(obj -> (Password) obj);
    }

    @Override
//  INSERT INTO railway.password (password, salt, iterations, algorithm) VALUES (?, ?, ?, ?)
    protected void prepareStatementForInsert(PreparedStatement statement, Identified object) {
        try {
            Password password = (Password) object;
            statement.setBytes(1, password.getPassword());
            statement.setBytes(2, password.getSalt());
            statement.setInt(3, password.getIterations());
            statement.setString(4, password.getAlgorithm());
        } catch (ClassCastException | SQLException e) {
            classLogger.error("Couldn't make PreparedStatement for INSERT", e);
            throw new DAOException(e);
        }
    }

    @Override
//  UPDATE railway.password SET password=? salt=? iterations=?, algorithm=? WHERE id= ?
    protected void prepareStatementForUpdate(PreparedStatement statement, Identified object){
        try {
            Password password = (Password) object;
            statement.setBytes(1, password.getPassword());
            statement.setBytes(2, password.getSalt());
            statement.setInt(3, password.getIterations());
            statement.setString(4, password.getAlgorithm());
            statement.setLong(5, password.getId());
        } catch (ClassCastException | SQLException e) {
            classLogger.error("Couldn't make PreparedStatement for UPDATE", e);
            throw new DAOException(e);
        }
    }

    @Override
    public Optional<List<Password>> getAll(){
        return super.getAll().map(list -> (List<Password>) list);
    }
}
