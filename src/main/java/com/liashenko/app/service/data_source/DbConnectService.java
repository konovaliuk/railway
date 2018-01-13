package com.liashenko.app.service.data_source;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.SQLException;

public class DbConnectService {
    private static final Logger classLogger = LogManager.getLogger(DbConnectService.class);
    private static final String JNDI_NODE = "java:/comp/env/jdbc/railway";
    private static volatile DbConnectService instance;
    private static javax.sql.DataSource dataSource;

    private DbConnectService() {
        try {
            InitialContext ic = new InitialContext();
            dataSource = (javax.sql.DataSource) ic.lookup(JNDI_NODE);
        } catch (NamingException e) {
            classLogger.error(e);
            throw new DbConnException("Couldn't get connection with database.");
        }
    }

    public static DbConnectService getInstance() {
        DbConnectService localInstance = instance;
        if (localInstance == null) {
            synchronized (DbConnectService.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new DbConnectService();
                }
            }
        }
        return instance;
    }

    public static void rollback(Connection connection) {
        if (connection != null) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                classLogger.error(e);
                throw new DbConnException("Couldn't rollback transaction");
            }
        }
    }

    public static void close(Connection connection) {
        if (connection != null) {
            try {
//                if (connection.isReadOnly()) {
//                    connection.setReadOnly(false);
//                }
                connection.close();
            } catch (SQLException e) {
                classLogger.error(e);
                throw new DbConnException("Couldn't close connection with database");
            }
        }
    }

    public synchronized Connection getConnection() {
        Connection conn = null;
        try {
            if (dataSource != null) {
                conn = dataSource.getConnection();
                if (conn == null) throw new SQLException("Couldn't get connection with database. Connection is null");
            }
        } catch (SQLException e) {
            classLogger.error(e);
            throw new DbConnException("Couldn't get connection with database");
        }
        return conn;
    }
}
