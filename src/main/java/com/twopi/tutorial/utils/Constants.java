package com.twopi.tutorial.utils;

/**
 * Class to store constants.
 * 
 * @author arshad01
 *
 */
public final class Constants {

    // Parameter names for Database
    public final static String DB_USER_PARAM            = "db.user";
    public final static String DB_PWD_PARAM             = "db.password.key";
    public final static String DB_DRV_CLASS_PARAM       = "db.driver.class";
    public final static String DB_URL_PARAM             = "db.url";

    // Other constants related to DB
    public final static String DB_CONNECTION_PARAM      = "db.connection";
    public final static String DB_HELPER_ATTR           = "db.helper";
    public final static String DB_TOTAL_STATS           = "total";
    public final static String DB_DAILY_STATS           = "daily";

    // Parameter names for IDOL API
    public final static String IDOLAPI_KEY_PARAM        = "idolapi.key";
    
    // Other constants related to IDOL Service
    public final static String IDOL_SVC_ATTR            = "idol.service.helper";
    public final static int    MAX_ERRORS               = 10;
    public final static String IDOL_BASE_URL            = "https://api.idolondemand.com/1/api";
    public final static String IDOL_SENT_ANALYSIS_URL   = IDOL_BASE_URL + "/sync/analyzesentiment/v1?";

    // Parameter names for Twitter
    public final static String TWOAUTH_KEY_PARAM          = "twitteroauth.key";
    public final static String TWOAUTH_SECRET_PARAM       = "twitteroauth.secret.key";
    public final static String TWOAUTH_TOKEN_PARAM        = "twitteroauth.token.key";
    public final static String TWOAUTH_TOEKN_SECRET_PARAM = "twitteroauth.token.secret.key";
    
    // Other constants related to Twitter Helper
    public final static String TWITTER_HELPER_ATTR        = "twitter.helper";
    public final static int    TWITTER_BATCH_MAX          = 1000;
    public final static int    TWEET_MIN_LENGTH           = 5;
    
    //TweetRequest statuses
    public final static String TR_PENDING_STATUS          = "pending";
    public final static String TR_COMPLETED_STATUS        = "completed";
    public final static String TR_FAILED_STATUS           = "failed";
    public final static String TR_PARTIAL_STATUS          = "partial";
    
    // Constants for TweetMoodClient
    public final static String CL_SVC_BASE_URL            = "http://localhost:8080/tweetmoodtut/ws";
    public final static String CL_GETMOOD_SVC_URL         = CL_SVC_BASE_URL + "/getmood?";
    public final static String CL_GETMOOD_POLL_SVC_URL    = CL_SVC_BASE_URL + "/getmoodpoll";
    public final static int    CL_POLL_INTERVAL           = 5*1000; //5 second poll interval
    
}
