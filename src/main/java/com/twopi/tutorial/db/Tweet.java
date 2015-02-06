package com.twopi.tutorial.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class Tweet {
    private long    _tweetId;           //Tweet id in the db
    private long    _requestId;         //Tweet's request id in the db
    private long    _tweetTwitterId;    //Tweet id from Twitter
    private String  _topic;             //Tweet topic found by IDOL sentiment analysis (may be null)
    private Date    _dateCreated;       //Tweet creation date and time
    private String  _language;          //Tweet Language in the db
    private String  _text;              //Tweet's text
    private String  _cleanText;         //Cleaned up text
    private String  _userName;          //User name if available
    private long    _userId;            //User id
    private boolean _isRetweeted;       //Whether tweet was retweeted
    private boolean _isFavorited;       //Whether tweet was favorited
    private String  _tweetMood;         //Aggregate tweet sentiment found by IDOL sentiment analysis
    private double  _moodScore;         //Tweet's aggregate sentiment score
        
    /**
     * @return the tweetId
     */
    public long getTweetId() {
        return _tweetId;
    }
    /**
     * @param tweetId the tweetId to set
     */
    public void setTweetId(long tweetId) {
        _tweetId = tweetId;
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
    /**
     * @return the tweetTwitterId
     */
    public long getTweetTwitterId() {
        return _tweetTwitterId;
    }
    /**
     * @param tweetTwitterId the tweetTwitterId to set
     */
    public void setTweetTwitterId(long tweetTwitterId) {
        _tweetTwitterId = tweetTwitterId;
    }
    /**
     * @return the topic
     */
    public String getTopic() {
        return _topic;
    }
    /**
     * @param topic the topic to set
     */
    public void setTopic(String topic) {
        _topic = topic;
    }
    /**
     * @return the dateCreated
     */
    public Date getDateCreated() {
        return _dateCreated;
    }
    /**
     * @param dateCreated the dateCreated to set
     */
    public void setDateCreated(Date dateCreated) {
        _dateCreated = dateCreated;
    }
    /**
     * @return the language
     */
    public String getLanguage() {
        return _language;
    }
    /**
     * @param language the language to set
     */
    public void setLanguage(String language) {
        _language = language;
    }
    /**
     * @return the text
     */
    public String getText() {
        return _text;
    }
    /**
     * @param text the text to set
     */
    public void setText(String text) {
        _text = text;
    }
    /**
     * @return the cleanText
     */
    public String getCleanText() {
        return _cleanText;
    }
    /**
     * @param cleanText the cleanText to set
     */
    public void setCleanText(String cleanText) {
        _cleanText = cleanText;
    }
    /**
     * @return the userName
     */
    public String getUserName() {
        return _userName;
    }
    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        _userName = userName;
    }
    /**
     * @return the userId
     */
    public long getUserId() {
        return _userId;
    }
    /**
     * @param userId the userId to set
     */
    public void setUserId(long userId) {
        _userId = userId;
    }
    /**
     * @return the isRetweeted
     */
    public boolean isRetweeted() {
        return _isRetweeted;
    }
    /**
     * @param isRetweeted the isRetweeted to set
     */
    public void setRetweeted(boolean isRetweeted) {
        _isRetweeted = isRetweeted;
    }
    /**
     * @return the isFavorited
     */
    public boolean isFavorited() {
        return _isFavorited;
    }
    /**
     * @param isFavorited the isFavorited to set
     */
    public void setFavorited(boolean isFavorited) {
        _isFavorited = isFavorited;
    }
    /**
     * @return the tweetMood
     */
    public String getTweetMood() {
        return _tweetMood;
    }
    /**
     * @param tweetMood the tweetMood to set
     */
    public void setTweetMood(String tweetMood) {
        _tweetMood = tweetMood;
    }
    /**
     * @return the moodScore
     */
    public double getMoodScore() {
        return _moodScore;
    }
    /**
     * @param moodScore the moodScore to set
     */
    public void setMoodScore(double moodScore) {
        _moodScore = moodScore;
    }
    
    /**
     * Loads the data from a ResultSet cursor
     * @param rs
     * @throws SQLException
     */
    public Tweet load(ResultSet rs) throws SQLException {
        this.setTweetId(rs.getLong("tweet_id"));
        this.setRequestId(rs.getLong("tweet_request_id"));
        this.setTweetTwitterId(rs.getLong("tweet_twitter_id"));
        this.setTopic(rs.getString("tweet_topic"));
        this.setDateCreated(rs.getTimestamp("tweet_date_created"));
        this.setLanguage(rs.getString("tweet_language"));
        this.setText(rs.getString("tweet_text"));
        this.setCleanText(rs.getString("tweet_clean_text"));
        this.setUserId(rs.getLong("tweet_user_id"));
        this.setUserName(rs.getString("tweet_user_name"));
        this.setRetweeted(rs.getBoolean("tweet_retweeted"));
        this.setFavorited(rs.getBoolean("tweet_favorited"));
        this.setTweetMood(rs.getString("tweet_mood"));
        this.setMoodScore(rs.getDouble("tweet_mood_score"));
        
        return this;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String
                .format("Tweet [_tweetId=%s, _requestId=%s, _tweetTwitterId=%s, _topic=%s, _dateCreated=%s, _language=%s, _text=%s, _cleanText=%s, _userName=%s, _userId=%s, _isRetweeted=%s, _isFavorited=%s, _tweetMood=%s, _moodScore=%s]",
                        _tweetId, _requestId, _tweetTwitterId, _topic,
                        _dateCreated, _language, _text, _cleanText, _userName,
                        _userId, _isRetweeted, _isFavorited, _tweetMood,
                        _moodScore);
    }
    
    
    
}
