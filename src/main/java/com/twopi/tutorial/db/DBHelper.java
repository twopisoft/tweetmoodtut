package com.twopi.tutorial.db;

import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Class providing methods to interact with the database.
 * 
 * @author arshad01
 *
 */
public class DBHelper {

    private Connection conn = null;

    private final static Logger LOG = Logger.getLogger(DBHelper.class.getName());
    
    /**
     * Opens a connection to the Database. If the connection exists, then
     * returns the cached connection.
     * 
     * @param dbClass - JDBC driver class name
     * @param dbUrl - DB URL
     * @param dbUser - DB User name
     * @param dbPwd - DB password
     * @return - DB Connection
     */
    public Connection openConnection(String dbClass, String dbUrl,
            String dbUser, String dbPwd) {

        if (conn == null) {
            try {
                Class.forName(dbClass);
                conn = DriverManager.getConnection(dbUrl, dbUser, dbPwd);
                LOG.info("DB Connection opened");
            } catch (ClassNotFoundException cnfe) {
                LOG.severe("Exception while opening DB connection: "
                        + cnfe.toString());
            } catch (SQLException sqe) {
                LOG.severe("Exception while opening DB connection: "
                        + sqe.toString());
            }
        }

        return conn;
    }

    /**
     * Closes DB Connection. Even though the connection can be closed directly,
     * its better to call this method to close and cleanup the connection.
     */
    public void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
                LOG.info("DB Connection closed");
            } catch (SQLException sqe) {
                LOG.warning("Failed to close DB connection: "
                        + sqe.toString());
            } finally {
                conn = null;
            }
        }
    }
}
