package com.twopi.tutorial.db;

import java.util.Date;

public class TweetRequest {

    private long    _requestId;     //Request Id in the db
    private Date    _createdAt;     //Request creation timestamp
    private String  _query;         //Query string for the request
    private long    _parentId;      //Parent request Id
    private String  _status;        //Request's status ('pending', 'invalid', 'completed')
    
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
}
