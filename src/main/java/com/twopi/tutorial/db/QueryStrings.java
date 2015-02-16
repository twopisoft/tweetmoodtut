package com.twopi.tutorial.db;

/**
 * Class for storing query strings. Package scope only.
 * @author twopi
 *
 */
class QueryStrings {
    
   final static String INSERT_REQUEST = 
            "INSERT INTO tweetmood.requests (request_id, request_created_at,request_query,request_parent_id," +
            "                                request_status,request_status_message) "+
            "SELECT ?::int, ?::timestamp, request_query, request_id, 'pending', 'in progress...' FROM tweetmood.requests " +
            "WHERE request_query = ? AND request_parent_id = 0 "+
            "UNION "+
            "SELECT * FROM (SELECT ?::int, ?::timestamp, ?::varchar, 0, 'pending', 'in progress...') AS tmp "+
            "WHERE NOT EXISTS "+
            " (SELECT ?::timestamp, request_query, request_id, 'pending', 'in progress...' FROM tweetmood.requests "+
            "  WHERE request_query = ? AND request_parent_id = 0)";
   
   final static String FIND_REQUEST =
           "SELECT * FROM tweetmood.requests WHERE request_id = ?";
   
   final static String FIND_TWEETS =
           "SELECT * FROM tweetmood.tweets WHERE tweet_request_id = ?";
   
   final static String FIND_DAILY_STATS =
           "SELECT DATE(tweet_date_created)as date, COUNT(tweet_mood) as count, tweet_mood FROM tweetmood.tweets "+
           " WHERE tweet_request_id = ? GROUP BY date,tweet_mood ORDER BY date";
   
   final static String FIND_TOTAL_STATS =
           "SELECT tweet_mood, COUNT(tweet_mood) FROM tweetmood.tweets " +
           "  WHERE tweet_request_id = ? GROUP BY tweet_mood";
   
   final static String UPDATE_STATUS =
           "UPDATE tweetmood.requests " +
           "  SET request_status = ?, request_status_message = ?, " +
              "request_last_completion = ?" + 
           "  WHERE request_id = ?";
            
    final static String INSERT_TWEET = "INSERT INTO tweetmood.tweets " +
            "  (tweet_request_id, tweet_twitter_id, tweet_topic, tweet_date_created,"+
            "   tweet_language,tweet_text,tweet_clean_text,tweet_user_id,tweet_user_name," +
            "   tweet_retweeted,tweet_favorited,tweet_mood,tweet_mood_score) " +
            "  VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
}
