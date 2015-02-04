package com.twopi.tutorial.twitter;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.twopi.tutorial.db.Tweet;
import com.twopi.tutorial.utils.Constants;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
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
    
    private final static Logger LOG = Logger.getLogger(TwitterHelper.class.getName());
    
    private Twitter _twitter = null;
    
    public TwitterHelper(String oauthKey, String oauthSecret, String oauthToken, String oauthTokenSecret) {
        
        ConfigurationBuilder cb = new ConfigurationBuilder()
                                    .setOAuthConsumerKey(oauthKey)
                                    .setOAuthConsumerSecret(oauthSecret)
                                    .setOAuthAccessToken(oauthToken)
                                    .setOAuthAccessTokenSecret(oauthTokenSecret);
        
        _twitter = new TwitterFactory(cb.build()).getInstance();
    }
    
    public List<Tweet> searchTweets(String queryStr) {
        List<Tweet> tweets = new ArrayList<Tweet>();
        
        try {
            Query query = new Query(queryStr);
            QueryResult result;
            int count = 0;
            do {
                result = _twitter.search(query);
                List<Status> stats = result.getTweets();
                count += stats.size();
                for (Status tw : stats) {
                    tweets.add(mapTweet(tw));
                }
            } while (((query = result.nextQuery()) != null) && (count <= Constants.TWITTER_BATCH_MAX));
            
            LOG.info("Returning "+tweets.size()+" tweets");
            
        } catch (TwitterException tce) {
            LOG.warning("Exception while searching for tweets: "+tce.toString());
        }
        
        return tweets;
    }

    private Tweet mapTweet(Status tw) {
        Tweet tweet = new Tweet();
        
        tweet.setTweetTwitterId(tw.getId());
        tweet.setDateCreated(tw.getCreatedAt());
        tweet.setText(tw.getText());
        tweet.setLanguage(tw.getLang());
        tweet.setFavorited(tw.isFavorited());
        tweet.setRetweeted(tw.isRetweeted());
        tweet.setUserId(tw.getUser().getId());
        tweet.setUserName(tw.getUser().getName());
        
        return tweet;
    }
}
