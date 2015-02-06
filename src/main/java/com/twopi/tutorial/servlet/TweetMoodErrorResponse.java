package com.twopi.tutorial.servlet;

import java.util.List;

import com.twopi.tutorial.db.Tweet;

/**
 * Class for representing an Error response. Also includes the failure reason
 * @author twopi
 *
 */
public final class TweetMoodErrorResponse extends TweetMoodResponse {

    private String _reason;
    
    public TweetMoodErrorResponse(long reqId, String reason) {
        super(reqId,null);
        _reason = reason;
    }
    
    @Override
    public final int getTweetCount() {
        return 0;
    }
    
    @Override
    public final List<Tweet> getTweets() {
        return null;
    }

    /**
     * Extra method to add a "pending" field in the response.
     * @return
     */
    public final boolean getFailed() {
        return true;
    }
    
    public final String getFailReason() {
        return _reason;
    }
}
