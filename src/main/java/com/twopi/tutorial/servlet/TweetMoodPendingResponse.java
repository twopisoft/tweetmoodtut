package com.twopi.tutorial.servlet;

import java.util.List;

import com.twopi.tutorial.db.Tweet;

/**
 * A special sub-class of TweetMoodResponse class to indicate that response is still pending
 * @author arshad01
 *
 */
public final class TweetMoodPendingResponse extends TweetMoodResponse {
    
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
    public final boolean getPending() {
        return true;
    }
}
