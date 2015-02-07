package com.twopi.tutorial.client.javafx;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.logging.Logger;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.twopi.tutorial.db.TweetRequest;
import com.twopi.tutorial.servlet.TweetMoodErrorResponse;
import com.twopi.tutorial.servlet.TweetMoodResponse;
import com.twopi.tutorial.utils.AssertUtil;
import com.twopi.tutorial.utils.Constants;

public class TweetMoodClient {

    private final static Logger LOG = Logger.getLogger(TweetMoodClient.class.getName());
    
    private Client _client;
    
    public TweetMoodClient() {
        _client = ClientBuilder.newClient();
    }
    
    public TweetMoodResponse getTweets(String query) {
        AssertUtil.assertParam(query);
        
        TweetMoodResponse completionResponse = null;
        
        try {
            Invocation.Builder builder = _client.target(Constants.CL_GETMOOD_SVC_URL)
                    .queryParam("search", URLEncoder.encode(query, "UTF-8"))
                    .request(MediaType.TEXT_PLAIN_TYPE)
                    .accept(MediaType.APPLICATION_JSON);
            
            try {
                TweetRequest request = builder.get(TweetRequest.class);
                
                long reqId = request.getRequestId();
                LOG.info("Got request: id="+reqId);
                completionResponse = pollForCompletion(reqId);
                
            } catch (WebApplicationException wex) {
                String errMsg = "Exception while processing request: "+wex.getMessage();
                LOG.severe(errMsg);
                throw new RuntimeException(errMsg);
            }
            
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        
        return completionResponse;
    }

    private TweetMoodResponse pollForCompletion(long reqId) {
        
        Invocation.Builder builder = _client.target(Constants.CL_GETMOOD_POLL_SVC_URL)
                .path(Long.toString(reqId))
                .request(MediaType.TEXT_PLAIN_TYPE)
                .accept(MediaType.APPLICATION_JSON);
        
        TweetMoodResponse response = null;
        do {
            try {
                Thread.sleep(Constants.CL_POLL_INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
           response = handleResponse(builder); 
        } while (response.getStatus().equals(Constants.TR_PENDING_STATUS));

        
        return response;
    }
    
    private TweetMoodResponse handleResponse(Invocation.Builder builder) {
        
        TweetMoodResponse tweetMoodResponse = null;
        
        try {
            tweetMoodResponse = builder.get(TweetMoodResponse.class);
            
            long reqId = tweetMoodResponse.getRequestId();
            LOG.info("Request Id: " + reqId + " status="+tweetMoodResponse.getStatus());
            
            if (tweetMoodResponse.getStatus().equals(Constants.TR_FAILED_STATUS)) {
                TweetMoodErrorResponse errResponse = builder.get(TweetMoodErrorResponse.class);
                
                String errMsg = "Fail reason=" + errResponse.getFailReason();
                LOG.severe(errMsg);
                throw new RuntimeException(errMsg);
            } 
        } catch (WebApplicationException wex) {
            String errMsg = "Exception while processing request: "+wex.getMessage();
            LOG.severe(errMsg);
            throw new RuntimeException(errMsg);
        }
        
        return tweetMoodResponse;
    }
    
    public static void main(String[] args) {
        
        TweetMoodClient client = new TweetMoodClient();
        
        TweetMoodResponse response = client.getTweets("$hpq");
        
        System.out.println("Found "+response.getTweetCount()+" tweets");

    }

}
