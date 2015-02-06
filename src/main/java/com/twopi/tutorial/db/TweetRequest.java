package com.twopi.tutorial.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * Class representing the TweetRequest Entity.
 * @author arshad01
 *
 */
public class TweetRequest {

    private long    _requestId;         //Request Id in the db
    private Date    _createdAt;         //Request creation timestamp
    private Date    _lastCompletion;    //Request last completion time
    private String  _query;             //Query string for the request
    private long    _parentId;          //Parent request Id
    private String  _status;            //Request's status ('pending', 'partial', 'failed', 'completed')
    private String  _statusMessage;     //Status Message
    
    /**
     * @return the requestId
     */
    public long getRequestId() {
        return _requestId;
    }

    /**
     * @param requestId the requestId to set
     */
    public void setRequestId(long requestId) {
        _requestId = requestId;
    }

    /**
     * @return the createdAt
     */
    public Date getCreatedAt() {
        return _createdAt;
    }

    /**
     * @param createdAt the createdAt to set
     */
    public void setCreatedAt(Date createdAt) {
        _createdAt = createdAt;
    }

    /**
     * @return the lastCompletion
     */
    public Date getLastCompletion() {
        return _lastCompletion;
    }

    /**
     * @param lastCompletion the lastCompletion to set
     */
    public void setLastCompletion(Date lastCompletion) {
        _lastCompletion = lastCompletion;
    }

    /**
     * @return the query
     */
    public String getQuery() {
        return _query;
    }

    /**
     * @param query the query to set
     */
    public void setQuery(String query) {
        _query = query;
    }

    /**
     * @return the parentId
     */
    public long getParentId() {
        return _parentId;
    }

    /**
     * @param parentId the parentId to set
     */
    public void setParentId(long parentId) {
        _parentId = parentId;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return _status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        _status = status;
    }

    /**
     * @return the statusMessage
     */
    public String getStatusMessage() {
        return _statusMessage;
    }

    /**
     * @param statusMessage the statusMessage to set
     */
    public void setStatusMessage(String statusMessage) {
        _statusMessage = statusMessage;
    }
    
    /**
     * Loads a TweetRequest object given the current ResultSet cursor.
     * @param rs - ResultSet cursor
     * @return - TweetRequest object populated with fields extracted from the ResultSet cursor.
     * @throws SQLException
     */
    public TweetRequest load(ResultSet rs) throws SQLException {
        this.setRequestId(rs.getLong("request_id"));
        this.setCreatedAt(rs.getTimestamp("request_created_at"));
        this.setLastCompletion(rs.getTimestamp("request_last_completion"));
        this.setQuery(rs.getString("request_query"));
        this.setParentId(rs.getLong("request_parent_id"));
        this.setStatus(rs.getString("request_status"));
        this.setStatusMessage(rs.getString("request_status_message"));
        
        return this;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String
                .format("TweetRequest [_requestId=%s, _createdAt=%s, _lastCompletion=%s, _query=%s, _parentId=%s, _status=%s, " +
                        "_statusMessage=%s]",
                        _requestId, _createdAt, _lastCompletion, _query,
                        _parentId, _status, _statusMessage);
    }
}
