package com.twopi.tutorial.helpers.twitter;

import java.util.logging.Logger;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
/**
 * 
 * Class for interacting with Twitter API. Uses twitter4j (http://twitter4j.org) library
 * internally.
 * 
 * @author arshad01
 *
 */
public class TwitterHelper {
    
    private final static Logger logger = Logger.getLogger(TwitterHelper.class.getName());
    
    private Twitter _twitter = null;
    
    public TwitterHelper(String oauthKey, String oauthSecret, String oauthToken, String oauthTokenSecret) {
        
        ConfigurationBuilder cb = new ConfigurationBuilder()
                                    .setOAuthConsumerKey(oauthKey)
                                    .setOAuthConsumerSecret(oauthSecret)
                                    .setOAuthAccessToken(oauthToken)
                                    .setOAuthAccessTokenSecret(oauthTokenSecret);
        
        _twitter = new TwitterFactory(cb.build()).getInstance();
    }
}
