package com.twopi.tutorial.servlet;

import java.util.List;

import com.twopi.tutorial.db.Tweet;

/**
 * Class representing the response to a sentiment analysis request
 * @author arshad01
 *
 */
public class TweetMoodResponse {
    
    private long _requestId;
    private List<Tweet> _tweets = null;
    
    public TweetMoodResponse() {
    }
    
    public TweetMoodResponse(long requestId, List<Tweet> tweets) {
        _requestId = requestId;
        _tweets = tweets;
    }
    
    /**
     * @return the requestId
     */
    public long getRequestId() {
        return _requestId;
    }

    public int getTweetCount() {
        return _tweets.size();
    }

    /**
     * @return the tweets
     */
    public List<Tweet> getTweets() {
        return _tweets;
    }

}
