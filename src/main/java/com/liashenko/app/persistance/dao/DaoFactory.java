package com.liashenko.app.persistance.dao;

import java.sql.Connection;
import java.util.Optional;
import java.util.ResourceBundle;

public interface DaoFactory<T extends GenericJDBCDao> {

    Optional<T> getDao(Connection connection, Class clazz, ResourceBundle localeQueries);
}
