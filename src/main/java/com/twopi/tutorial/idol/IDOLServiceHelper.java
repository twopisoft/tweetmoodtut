package com.twopi.tutorial.idol;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.twopi.tutorial.db.Tweet;
import com.twopi.tutorial.idol.response.error.IDOLErrorResponse;
import com.twopi.tutorial.idol.response.sentiment.IDOLSentimentResponse;
import com.twopi.tutorial.utils.AssertUtil;
import com.twopi.tutorial.utils.Constants;
import com.twopi.tutorial.utils.LanguageMapper;

/**
 * Class providing methods to invoke IDOLOnDemand API requests. (At the moment supports only Sentiment Analysis)
 * 
 * @author arshad01
 *
 */
public class IDOLServiceHelper {
    
    private final static Logger LOG = Logger.getLogger(IDOLServiceHelper.class.getName());
    
    private String _apiKey = null;
    private Client _client = null;

    public IDOLServiceHelper(String apiKey) {
        this._apiKey = apiKey;
        _client = ClientBuilder.newClient();
    }

    /**
     * Performs sentiment analysis by calling IDOLOnDemand Sentiment Analysis API.
     * In this tutorial, we use synchronous API calls.
     * @param tweets - Tweets to be analyzed
     * @return
     */
    public List<Tweet> doSentimentAnalysis(List<Tweet> tweets) {
        
        LOG.info("Starting sentiment analysis with IDOL API");
        
        AssertUtil.assertFieldSvc(tweets);
        
        int numErrors = Constants.MAX_ERRORS;
        
        List<Tweet> analyzedTweets = new ArrayList<Tweet>();
        for (Tweet tweet : tweets) {
            try {
                String text = URLEncoder.encode(tweet.getCleanText(), "UTF-8");
                
                if (!text.isEmpty() && 
                    text.length() > Constants.TWEET_MIN_LENGTH &&
                    !tweet.isRetweeted()) {
                    
                    LOG.info("invokeSentimentApi: "+text);
                    
                    IDOLSentimentResponse response =
                            invokeSentimentApi(text,tweet.getLanguage());
                    tweet.setTweetMood(response.getAggregate().getSentiment().name().toLowerCase());
                    tweet.setMoodScore(response.getAggregate().getScore().doubleValue());
                    analyzedTweets.add(tweet);
                }
            } catch (Exception e) {
                LOG.warning("Exception : "+e.toString());
                numErrors--;
                if (numErrors <= 0) {
                    throw new RuntimeException("Too many errors. "+e.getMessage());
                }
            }           
        }
        
        return analyzedTweets;
    }

    /**
     * Helper method to invoke Sentiment analysis API for a single tweet. It uses Jersey Client API to
     * invoke the webservice.
     * @param text
     * @param language - Language identifier received from Twitter. The method maps it to ISO 639-2 format
     * needed by IDOL API.
     * @return - IDOLSentimentResponse object which is created from the JSON response. On error, a RuntimeException
     * is thrown.
     */
    private IDOLSentimentResponse invokeSentimentApi(String text, String language) {
        
        Invocation.Builder builder = _client.target(Constants.IDOL_SENT_ANALYSIS_URL)
                            .queryParam("text", text)
                            .queryParam("language", LanguageMapper.map(language))
                            .queryParam("apikey", _apiKey)
                            .request(MediaType.TEXT_PLAIN_TYPE)
                            .accept(MediaType.APPLICATION_JSON);
        
        Response response = builder.get();
        
        if (response.getStatus() != 200) {
            IDOLErrorResponse errorResponse = builder.get(IDOLErrorResponse.class);
            String errMsg = String.format("Error while executing sentiment analysis api: code=%d, reason=%s, message=%s",
                                    errorResponse.getError(),errorResponse.getReason(),errorResponse.getMessage());
            LOG.severe(errMsg);
            throw new RuntimeException(errMsg);
        }
        
        return builder.get(IDOLSentimentResponse.class);
        
    }
}
