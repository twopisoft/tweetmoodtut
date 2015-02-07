package com.twopi.tutorial.servlet;

import java.util.List;
import java.util.Map;

import com.twopi.tutorial.db.Tweet;

/**
 * Class representing the response to a sentiment analysis request
 * @author arshad01
 *
 */
public class TweetMoodResponse {
    
    private long _requestId;
    private List<Tweet> _tweets = null;
    private Map<String,Map<String,String>> _stats = null;
    private String _status;
    private int _tweetCount;
    
    public TweetMoodResponse() {
        
    }
    
    public TweetMoodResponse(long requestId, List<Tweet> tweets, Map<String, Map<String, String>> stats, String status) {
        _requestId = requestId;
        _tweets = tweets;
        _stats = stats;
        _status = status;
    }
    
    public TweetMoodResponse(long reqId, String status) {
        this(reqId, null, null, status);
    }
    
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

    public int getTweetCount() {
        return _tweetCount;
    }

    /**
     * @param tweetCount the tweetCount to set
     */
    public void setTweetCount(int tweetCount) {
        _tweetCount = _tweets != null ? _tweets.size() : 0;
    }

    /**
     * @return the tweets
     */
    public List<Tweet> getTweets() {
        return _tweets;
    }

    /**
     * @param tweets the tweets to set
     */
    public void setTweets(List<Tweet> tweets) {
        _tweets = tweets;
    }

    /**
     * @return the stats
     */
    public Map<String, Map<String,String>> getStats() {
        return _stats;
    }

    /**
     * @param stats the stats to set
     */
    public void setStats(Map<String, Map<String,String>> stats) {
        _stats = stats;
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
