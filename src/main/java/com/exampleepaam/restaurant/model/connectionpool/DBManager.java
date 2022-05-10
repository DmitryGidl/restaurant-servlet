package com.exampleepaam.restaurant.model.connectionpool;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Singleton which helps to get DB connections using Hikari Connection Pooling
 */
public class DBManager {
    static final Logger logger = LoggerFactory.getLogger(DBManager.class);

    private static DBManager instance;
    private ComboPooledDataSource cpds;

    /**
     * Singleton.
     */
    public static synchronized DBManager getInstance() {
        if (instance == null) instance = new DBManager();
        return instance;
    }

    private DBManager() {
        try {
            createPool();
        } catch (Exception e) {
            logger.error(e.toString());
        }
    }


    /**
     * Establishes a connection to the database.
     *
     * @return the connection to the database
     */
    public Connection getConnection() {
        Connection conn = null;
        try {
            conn = cpds.getConnection();
        } catch (SQLException e) {
            logger.error(e.toString());
        }
        return conn;
    }


    /**
     * Gets utils properties.
     *
     * @return the databases properties
     * @throws IOException by failed or interrupted I/O operations.
     */
    private Properties getProperties() throws IOException {
        Properties props = new Properties();
        props.load(DBManager.class.getResourceAsStream("/db.properties"));
        return props;
    }

    /**
     * Creates a pool.
     *
     * @throws IOException            by failed or interrupted I/O operations.
     * @throws PropertyVetoException  by setDriverClass method
     * @throws ClassNotFoundException by Class.forName
     */
    private void createPool() throws PropertyVetoException, IOException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        Properties props = getProperties();
        cpds = new ComboPooledDataSource();
        cpds.setDriverClass(props.getProperty("driver"));
        cpds.setJdbcUrl(props.getProperty("url"));
        cpds.setUser(props.getProperty("user"));
        cpds.setPassword(props.getProperty("password"));
        cpds.setMaxStatements(180);
    }

}
