package com.twopi.tutorial.servlet;

import java.util.List;

import com.twopi.tutorial.db.Tweet;

public class TweetMoodResponse {
    
    private List<Tweet> _tweets = null;
    
    public TweetMoodResponse() {
    }
    
    public TweetMoodResponse(List<Tweet> tweets) {
        _tweets = tweets;
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
