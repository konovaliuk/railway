package com.liashenko.app.service.storage_connection;


import com.liashenko.app.service.storage_connection.data_source.TomcatDataSource;
import com.liashenko.app.service.exceptions.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;


public class DBConnectService {

    private static final Logger classLogger = LogManager.getLogger(DBConnectService.class);

    private static DBConnectService instance;
    private static Connection connection;

    private TomcatDataSource tomcatDataSource;

    public DBConnectService() {
        this.tomcatDataSource = TomcatDataSource.getInstance();
    }

    public Optional<Connection> getConnection() {
        return Optional.ofNullable(tomcatDataSource.getConnection());
    }

    public static void rollback(Connection connection) {
        if (connection != null){
            try {
                connection.rollback();
            } catch (SQLException e) {
                classLogger.error("Couldn't make rollback", e);
            }
        }
    }

    public static void close(Connection connection) {
        System.out.println("DBConnectService.close 2");
        if (connection != null){
            try {
                System.out.println("DBConnectService.close 3isReadOnly : " + connection.isReadOnly());
                if (connection.isReadOnly()) {
                    connection.setReadOnly(false);
                }
                System.out.println("DBConnectService.close 4isReadOnly : " + connection.isReadOnly());
                connection.close();
            } catch (SQLException e) {
                classLogger.error("Couldn't close connection with database", e);
                throw new ServiceException();
            }
        }
    }
}
