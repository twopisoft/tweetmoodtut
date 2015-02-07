package com.twopi.tutorial.twitter;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.twitter.Extractor;
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
    
    private Extractor _extractor = null;
    
    public TwitterHelper(String oauthKey, String oauthSecret, String oauthToken, String oauthTokenSecret) {
        
        ConfigurationBuilder cb = new ConfigurationBuilder()
                                    .setOAuthConsumerKey(oauthKey)
                                    .setOAuthConsumerSecret(oauthSecret)
                                    .setOAuthAccessToken(oauthToken)
                                    .setOAuthAccessTokenSecret(oauthTokenSecret);
        
        _twitter = new TwitterFactory(cb.build()).getInstance();
        
        _extractor = new Extractor();
    }
    
    /**
     * Search for tweets for the given query string
     * @param queryStr
     * @return - List of found tweets.
     */
    public List<Tweet> searchTweets(String queryStr) {
        LOG.info("Searching tweets for query=\""+queryStr+"\"");
        
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
    
    /**
     * Cleans the tweet's text so that it can be used for the sentiment analysis. We make use of twitter-text for Java
     * library. All the hashtags, urls, user names, and cash tags are removed from the text. The method uses
     * slightly less performant but highly convenient String.replaceAll method rather than using a StringBuilder in
     * conjunction with a Pattern/Matcher
     * @param text - Tweet's text
     * @return Cleaned text
     */
    public String cleanTweetText(String text) {
        List<String> hashTags = _extractor.extractHashtags(text);
        List<String> urls = _extractor.extractURLs(text);
        List<String> mentionedNames = _extractor.extractMentionedScreennames(text);
        List<String> cashTags = _extractor.extractCashtags(text);
        String replyName = _extractor.extractReplyScreenname(text);
        
        List<String> entities = new ArrayList<String>();
        entities.addAll(hashTags);
        entities.addAll(urls);
        entities.addAll(mentionedNames);
        entities.addAll(cashTags);
        entities.add("#");
        entities.add("@");
        entities.add("\\$");
        
        if (replyName != null) {
            entities.add(replyName);
        }
        
        for (String entity : entities) {
            text = text.replaceAll(entity, "");
        }
        
        text = text.toString().trim().replaceAll("\\s+", " ");

        return text;
    }

    /**
     * Map twitter4j Status object to Tweet object
     * @param tw - twitter4j Status object
     * @return - Populated Tweet object
     */
    private Tweet mapTweet(Status tw) {
        Tweet tweet = new Tweet();
        
        tweet.setTweetTwitterId(tw.getId());
        tweet.setDateCreated(tw.getCreatedAt());
        tweet.setText(tw.getText());
        tweet.setCleanText(cleanTweetText(tw.getText()));
        tweet.setLanguage(tw.getLang());
        tweet.setFavorited(tw.isFavorited());
        //tweet.setRetweeted(tw.isRetweeted());
        tweet.setRetweeted(tw.isRetweet());
        tweet.setUserId(tw.getUser().getId());
        tweet.setUserName(tw.getUser().getName());
        
        return tweet;
    }
}
