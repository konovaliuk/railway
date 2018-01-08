package com.liashenko.app.persistance.dao.mysql;

import com.liashenko.app.persistance.dao.DaoFactory;
import com.liashenko.app.persistance.dao.GenericJDBCDao;
import com.liashenko.app.persistance.dao.exceptions.DAOException;
import com.liashenko.app.persistance.domain.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.util.Optional;
import java.util.ResourceBundle;

public class MySQLDaoFactory implements DaoFactory {
    private static final Logger classLogger = LogManager.getLogger(MySQLDaoFactory.class);
    private static volatile MySQLDaoFactory instance;

    private MySQLDaoFactory(){
    }

    public static MySQLDaoFactory getInstance(){
        MySQLDaoFactory localInstance = instance;
        if (localInstance == null) {
            synchronized (MySQLDaoFactory.class){
              localInstance = instance;
              if (localInstance == null) {
                  instance = localInstance = new MySQLDaoFactory();
              }
            }
        }
        return instance;
    }

    @Override
    public synchronized Optional<GenericJDBCDao> getDao(Connection connection, Class clazz, ResourceBundle localeQueries){
        if (connection == null) throw new DAOException("No connection with db");

        if (clazz == User.class) return Optional.of(new UserDaoImpl(connection, localeQueries));
        if (clazz == Password.class) return Optional.of(new PasswordDaoImpl(connection, localeQueries));
        if (clazz == Role.class) return Optional.of(new RoleDaoImpl(connection, localeQueries));
        if (clazz == Station.class) return Optional.of(new StationDaoImpl(connection, localeQueries));
        if (clazz == Train.class) return Optional.of(new TrainDaoImpl(connection, localeQueries));
        if (clazz == TimeTable.class) return Optional.of(new TimeTableDaoImpl(connection, localeQueries));
        if (clazz == Route.class) return Optional.of(new RouteDaoImpl(connection, localeQueries));
        if (clazz == TrainRate.class) return Optional.of(new TrainRateDaoImpl(connection, localeQueries));
        if (clazz == VagonType.class) return Optional.of(new VagonTypeDaoImpl(connection, localeQueries));
        if (clazz == RouteRate.class) return Optional.of(new RouteRateDaoImpl(connection, localeQueries));
        if (clazz == RouteNum.class) return Optional.of(new RouteNumDaoImpl(connection, localeQueries));
        if (clazz == PricePerKmForVagon.class)
            return Optional.of(new PricePerKmForVagonDaoImpl(connection, localeQueries));

        classLogger.error("DAO for entity " + clazz + " doesn't exist");
        return Optional.empty();
    }
}
