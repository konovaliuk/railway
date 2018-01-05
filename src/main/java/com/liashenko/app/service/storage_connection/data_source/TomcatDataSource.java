package com.liashenko.app.service.storage_connection.data_source;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.SQLException;

public class TomcatDataSource {
    private static final Logger classLogger = LogManager.getLogger(TomcatDataSource.class);
    private static final String JNDI_NODE = "java:/comp/env/jdbc/railway";
    private InitialContext ic;
    private static TomcatDataSource instance;
    private static javax.sql.DataSource dataSource;

    private TomcatDataSource(){
        try {
            ic = new InitialContext();
            dataSource = (javax.sql.DataSource) ic.lookup(JNDI_NODE);
        } catch (NamingException e) {
            classLogger.error(e);
        }
    }

    public static TomcatDataSource getInstance() {
        if (instance == null)
            instance = new TomcatDataSource();
        return instance;
    }

    public Connection getConnection() {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
        } catch (NullPointerException | SQLException e) {
            classLogger.error(e);
        }
        return connection;
    }
}
