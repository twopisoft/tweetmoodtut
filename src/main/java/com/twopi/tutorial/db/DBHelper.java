package com.twopi.tutorial.db;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

/**
 * Class providing methods to interact with the database.
 * 
 * @author arshad01
 *
 */
public class DBHelper {

    private Connection _conn = null;

    private final static Logger LOG = Logger.getLogger(DBHelper.class.getName());
    
    // AtomicLong to generate requests ids. 
    private AtomicLong _nextRequestId = new AtomicLong(1000000);
    
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

        if (_conn == null) {
            try {
                Class.forName(dbClass);
                _conn = DriverManager.getConnection(dbUrl, dbUser, dbPwd);
                _conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                //_conn.setAutoCommit(false);
                
                LOG.info("DB Connection opened");
                LOG.info("Transcation isolation level: "+_conn.getTransactionIsolation());
            } catch (ClassNotFoundException cnfe) {
                LOG.severe("Exception while opening DB connection: "
                        + cnfe.toString());
            } catch (SQLException sqe) {
                LOG.severe("Exception while opening DB connection: "
                        + sqe.toString());
            }
        }

        return _conn;
    }
    
    /**
     * Adds a sentiment analysis request into the tweetmood.requests table
     * @param queryStr - Normalized query string
     * @return TweetRequest object containing request id which can be used for polling for results.
     */
    public TweetRequest addRequest(String queryStr) throws SQLException {
        assertState();
        
       TweetRequest tweetRequest = insertRequest(queryStr);
       
       return tweetRequest;
    }

    /**
     * Inserts a new request into the tweetmood.requests table. The query uses insert with select to add a row and
     * set the parent request id if another request is already present for the given search term. If none is present
     * then a row with parent id=0 is inserted. For example, if the query string is '$hpq $aapl', then the first call
     * to this method updates the table as follows:
     * 
     * request_id  |   request_created_at    | request_query | request_parent_id | request_status 
     * ------------+-------------------------+---------------+-------------------+----------------
     *     1000000 | 2015-02-04 21:42:34.861 | $hpq $aapl    |                 0 | pending
     * 
     * A second call with same query string will result in following update:
     * 
     * request_id  |   request_created_at    | request_query | request_parent_id | request_status 
     * ------------+-------------------------+---------------+-------------------+----------------
     *     1000000 | 2015-02-04 21:42:34.861 | $hpq $aapl    |                 0 | pending
     *     1000001 | 2015-02-04 21:42:54.587 | $hpq $aapl    |           1000000 | pending
     *     
     * Note that in the second row, the parent id points to the first row.
     *     
     * @param queryStr - The (normalized) query string.
     * @return the created TweetRequest object or NULL if none could be created.
     * @throws SQLException
     */
    private TweetRequest insertRequest(String queryStr) throws SQLException {
        
        Timestamp creationTime = new Timestamp(new Date().getTime());
        String creationTimeStr = creationTime.toString();
        long newReqId = _nextRequestId.getAndIncrement();
        
        String insertReqQuery = String.format(
                                    "INSERT INTO tweetmood.requests (request_id, request_created_at,request_query,request_parent_id,request_status) "+
                                            "SELECT %d, '%s'::timestamp, request_query, request_id, 'pending' FROM tweetmood.requests " +
                                            "WHERE request_query = '%s' AND request_parent_id = 0 "+
                                            "UNION "+
                                            "SELECT * FROM (SELECT %d, '%s'::timestamp, '%s', 0, 'pending') AS tmp "+
                                            "WHERE NOT EXISTS "+
                                            " (SELECT '%s'::timestamp, request_query, request_id, 'pending' FROM tweetmood.requests "+
                                            "  WHERE request_query = '%s' AND request_parent_id = 0)",
                                    newReqId,
                                    creationTimeStr,
                                    queryStr,
                                    newReqId,
                                    creationTimeStr,
                                    queryStr,
                                    creationTimeStr,
                                    queryStr);
        
        LOG.info("query="+insertReqQuery);
        
        Statement insertReqStmt = _conn.createStatement();
        
        if (insertReqStmt.executeUpdate(insertReqQuery) > 0) {
            return getTweetRequest(newReqId);
        }
        
        return null;
    }

    /**
     * Returns a TweetRequest object for the given request Id.
     * @param reqId
     * @return TweetRequest object or NULL if none was found.
     * @throws SQLException
     */
    private TweetRequest getTweetRequest(long reqId) throws SQLException {
        
        String findQuery = String.format("SELECT * FROM tweetmood.requests WHERE request_id = %d",reqId);
        
        Statement findStmt = _conn.createStatement();
        ResultSet rs = findStmt.executeQuery(findQuery);
        if (rs.next()) {
            return mapTweetRequest(rs);
        }
                        
        return null;
    }

    /**
     * Creates a TweetRequest object given the current ResultSet cursor.
     * @param rs - ResultSet cursor
     * @return - TweetRequest object populated with fields extracted from the ResultSet cursor.
     * @throws SQLException
     */
    private TweetRequest mapTweetRequest(ResultSet rs) throws SQLException {
        TweetRequest tweetRequest = new TweetRequest();
        
        tweetRequest.setRequestId(rs.getLong("request_id"));
        tweetRequest.setCreatedAt(rs.getTimestamp("request_created_at"));
        tweetRequest.setQuery(rs.getString("request_query"));
        tweetRequest.setParentId(rs.getLong("request_parent_id"));
        tweetRequest.setStatus(rs.getString("request_status"));
        
        return tweetRequest;
    }

    /**
     * Closes DB Connection. Even though the connection can be closed directly,
     * its better to call this method to close and cleanup the connection.
     */
    public void closeConnection() {
        if (_conn != null) {
            try {
                _conn.close();
                LOG.info("DB Connection closed");
            } catch (SQLException sqe) {
                LOG.warning("Failed to close DB connection: "
                        + sqe.toString());
            } finally {
                _conn = null;
            }
        }
    }
    
    /**
     * Check the state of DB connection. For now only checks if connection was initialized.
     * @return true if connection status is ok otherwise throw IllegalStateException.
     */
    private boolean assertState() {
        if (_conn == null) {
            throw new IllegalStateException("DB connection is not initialized");
        }
        return true;
    }
}
