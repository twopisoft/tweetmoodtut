package com.twopi.tutorial.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import com.twopi.tutorial.servlet.TweetMoodResponse;
import com.twopi.tutorial.utils.AssertUtil;
import com.twopi.tutorial.utils.Constants;

/**
 * Class providing methods to interact with the database. 
 * 
 * Note: that all query strings are stored in this class rather
 * than the Constants or any other class so as to keep them close to their usage.
 * 
 * @author arshad01
 *
 */
public class DBHelper {

    private Connection _conn = null;

    private final static Logger LOG = Logger.getLogger(DBHelper.class.getName());
    
    // AtomicLong to generate requests ids.
    private static AtomicLong _nextRequestId = new AtomicLong();
    
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
                
                LOG.info("DB Connection opened");
                
                initNextRequestId();
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
     * @throws SQLException
     */
    public TweetRequest addRequest(String queryStr) throws SQLException {
        assertState();
        
       TweetRequest tweetRequest = insertRequest(queryStr);
       
       return tweetRequest;
    }
    
    /**
     * Inserts tweets for a given request. Once the tweets are added, the request status is updated to "completed"
     * @param reqId
     * @param tweets
     * @throws SQLException
     */
    public void addTweets(long reqId, List<Tweet> tweets) throws SQLException {
        assertState();
        
        LOG.info("Adding tweets for request_id="+reqId);
        
        for (Tweet tw : tweets) {
            insertTweet(reqId, tw);
        }
        
        LOG.info(tweets.size() + " tweets inserted");
    }
    
    /**
     * Checks if a request is in pending state.
     * @param reqId
     * @return true if request is in pending state
     * @throws SQLException
     */
    public boolean isRequestPending(long reqId) throws SQLException {
        assertState();
        
        TweetRequest tweetRequest = getTweetRequest(reqId);
        
        AssertUtil.assertField(tweetRequest);
        
        return tweetRequest.getStatus().equals(Constants.TR_PENDING_STATUS);
    }

    /**
     * Returns tweets for a given request.
     * @param reqId
     * @return Tweets for a given request id.
     * @throws SQLException
     */
    public TweetMoodResponse getTweets(long reqId) throws SQLException {
        assertState();
        
        String tweetQuery = "SELECT * FROM tweetmood.tweets WHERE tweet_request_id = " + reqId;
        
        Statement tweetStmt = _conn.createStatement();
        
        ResultSet rs = tweetStmt.executeQuery(tweetQuery);
        
        List<Tweet> tweets = new ArrayList<Tweet>();
        while (rs.next()) {
            tweets.add(new Tweet().load(rs));
        }
        
        LOG.info("Returning "+tweets.size()+" tweets");
        
        return new TweetMoodResponse(reqId, tweets);
    }

    /**
     * Inserts a new request into the tweetmood.requests table. The query uses insert with select to add a row and
     * set the parent request id if another request is already present for the given search term. If none is present
     * then a row with parent id=0 is inserted. For example, if the query string is '$hpq $aapl', then the first call
     * to this method updates the table as follows:
     * 
     *  request_id |   request_created_at    | request_last_completion | request_query | request_parent_id | request_status | request_status_message 
     * ------------+-------------------------+-------------------------+---------------+-------------------+----------------+------------------------
     *     1000000 | 2015-02-05 09:41:14.552 |                         | $hpq $aapl    |                 0 | pending        |
     * 
     * A second call with same query string will result in following update:
     * 
     *  request_id |   request_created_at    | request_last_completion | request_query | request_parent_id | request_status | request_status_message 
     * ------------+-------------------------+-------------------------+---------------+-------------------+----------------+------------------------
     *     1000000 | 2015-02-05 09:41:14.552 |                         | $hpq $aapl    |                 0 | pending        | 
     *     1000001 | 2015-02-05 09:42:58.046 |                         | $hpq $aapl    |           1000000 | pending        | 
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
                                    "INSERT INTO tweetmood.requests (request_id, request_created_at,request_query,request_parent_id,request_status,request_status_message) "+
                                            "SELECT %d, '%s'::timestamp, request_query, request_id, 'pending', 'in progress...' FROM tweetmood.requests " +
                                            "WHERE request_query = '%s' AND request_parent_id = 0 "+
                                            "UNION "+
                                            "SELECT * FROM (SELECT %d, '%s'::timestamp, '%s', 0, 'pending', 'in progress...') AS tmp "+
                                            "WHERE NOT EXISTS "+
                                            " (SELECT '%s'::timestamp, request_query, request_id, 'pending', 'in progress...' FROM tweetmood.requests "+
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
    public TweetRequest getTweetRequest(long reqId) throws SQLException {
        assertState();
        
        String findQuery = String.format("SELECT * FROM tweetmood.requests WHERE request_id = %d",reqId);
        
        Statement findStmt = _conn.createStatement();
        ResultSet rs = findStmt.executeQuery(findQuery);
        if (rs.next()) {
            return new TweetRequest().load(rs);
        }
                        
        return null;
    }
    
    /**
     * Updates a request's status
     * @param reqId
     * @param status
     * @param statusMessage
     * @throws SQLException
     */
    public void updateStatus(long reqId, String status, String statusMessage) throws SQLException {
        assertState();
        
        boolean isCompleted = status.equals(Constants.TR_COMPLETED_STATUS);
        
        String updateStatQueryFmt = "UPDATE tweetmood.requests " +
                                    "  SET request_status = '%s', request_status_message = '%s', " +
                                       "request_last_completion = " + (isCompleted ? "'%s'::timestamp" : "%s") +
                                    "  WHERE request_id = %d";
        
        Timestamp completionTime = new Timestamp(new Date().getTime());
        
        String updateStateQuery = String.format(updateStatQueryFmt, 
                                                status, 
                                                statusMessage.substring(0, Math.min(statusMessage.length(),100)),
                                                (isCompleted ? completionTime.toString() : "NULL"),
                                                reqId);
        
        LOG.info("updateStateQuery="+updateStateQuery);
        
        Statement updateStatStmt = _conn.createStatement();
        
        updateStatStmt.executeUpdate(updateStateQuery);
        
        LOG.info("Setting \""+status+"\" status for request_id: "+reqId);
    }
    
    /**
     * Helper method to insert a Tweet entity
     * @param reqId
     * @param tw
     * @throws SQLException
     */
    private void insertTweet(long reqId, Tweet tw) throws SQLException {
        
        String insertTwQuery = String.format(
                                    "INSERT INTO tweetmood.tweets " +
                                    "  (tweet_request_id, tweet_twitter_id, tweet_topic, tweet_date_created,"+
                                    "   tweet_language,tweet_text,tweet_clean_text,tweet_user_id,tweet_user_name," +
                                    "   tweet_retweeted,tweet_favorited,tweet_mood,tweet_mood_score) " +
                                    " VALUES (%d, %d, '%s', '%s'::timestamp, '%s', '%s', '%s', %d, '%s', " +
                                    "        '%b','%b','%s',%f)",
                                    reqId,
                                    tw.getTweetTwitterId(),
                                    tw.getTopic(),
                                    tw.getDateCreated().toString(),
                                    tw.getLanguage(),
                                    tw.getText().replaceAll("'", "''"),
                                    tw.getCleanText().replaceAll("'", "''"),
                                    tw.getUserId(),
                                    tw.getUserName(),
                                    tw.isRetweeted(),
                                    tw.isFavorited(),
                                    tw.getTweetMood(),
                                    tw.getMoodScore());
        
        Statement insertTwStmt = _conn.createStatement();
        
        insertTwStmt.executeUpdate(insertTwQuery);
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
    
    /**
     * Initializes the _nextRequestId field on startup by reading the last value of the request_id. Since
     * request_id is monotonically increasing, we use the max function to find the last value inserted. For 
     * an empty table scenario, the null value returned by max is converted to 999999 so that we
     * can start at a million.
     * @throws SQLException
     */
    private void initNextRequestId() throws SQLException {
         
        String findLastQuery = "SELECT COALESCE(MAX(request_id),999999) AS last_value FROM tweetmood.requests";
        
        Statement findLastStmt = _conn.createStatement();
        
        ResultSet rs = findLastStmt.executeQuery(findLastQuery);
        
        if (rs.next()) {
            long lastValue = rs.getLong("last_value");
            _nextRequestId.set(lastValue+1);
            LOG.info("nextRequestId="+_nextRequestId.get());
        }
    }

    
}
