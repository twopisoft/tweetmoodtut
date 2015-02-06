
package com.twopi.tutorial;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import com.twopi.tutorial.db.DBHelper;
import com.twopi.tutorial.db.TweetRequest;
import com.twopi.tutorial.idol.IDOLServiceHelper;
import com.twopi.tutorial.servlet.TweetMoodErrorResponse;
import com.twopi.tutorial.servlet.TweetMoodPendingResponse;
import com.twopi.tutorial.servlet.TweetMoodResponse;
import com.twopi.tutorial.twitter.TwitterHelper;
import com.twopi.tutorial.utils.AssertUtil;
import com.twopi.tutorial.utils.Constants;

import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

/**
 * JAX-RS resource class for the sentiment analysis web serivice
 * @author arshad01
 *
 */
@Path("/")
public class TweetMoodResource {
	
	private final static Logger LOG = Logger.getLogger(TweetMoodResource.class.getName());
	
	@Context ServletContext context;
    
    /**
     * Finds tweets for the given search string and performs sentiment analysis.
     * @param search - Search query. Can contain terms separated by spaces.
     * @return A list of tweets and the evaluated sentiment
     */
    @GET 
    @Produces("application/json")
    @Path("getmood")
    public TweetRequest getMood(@QueryParam("search") String search) {
        LOG.info("getMood: search="+search);
        
        AssertUtil.assertParam(search);
        
        DBHelper dbHelper = (DBHelper) context.getAttribute(Constants.DB_HELPER_ATTR);
        AssertUtil.assertField(dbHelper);
        
        TweetRequest request;
        try {
            String normalizedSearch = normalize(search);
            
            request = dbHelper.addRequest(normalizedSearch);
            
            AssertUtil.assertField(request);
            
            TweetRequest parentReq = dbHelper.getTweetRequest(request.getParentId());
            
            long reqId = request.getRequestId();
            boolean needProcessing = request.getParentId() == 0;
            
            if (parentReq != null && parentReq.getStatus().equals(Constants.TR_FAILED_STATUS)) {
                reqId = parentReq.getRequestId();
                needProcessing = true;
            }
            
            if (needProcessing) {
                
                //Either fire off a new sentiment analysis request or
                //restart a failed one
                processRequest(normalizedSearch, reqId);
                
                LOG.info("Sentiment Analysis started for query=\""+search+"\"");
            } 
        } catch (Throwable e) {
            e.printStackTrace();
            throw new RuntimeException(e.toString());
        }
        
        return request;
    }

    /**
     * Polls for the result of a request.
     * @param reqid - The request Id of the pending request
     * @return TweetMoodResponse - 
     *      If request is pending then return TweetMoodPendingResponse,
     *      else if it failed then return TweetMoodErrorResponse
     *      else return the analyzed tweets
     *      
     * Note: Failed requests can always be restarted by sending the same search query again.
     */
    @GET
    @Produces("application/json")
    @Path("getmoodpoll/{reqid}")
    public TweetMoodResponse getMoodPoll(@PathParam("reqid") String reqid) {
        LOG.info("reqId="+reqid);
        
        AssertUtil.assertParam(reqid);
        
        long requestId = Long.parseLong(reqid);
        
        return moodAnalysisResponse(requestId);
    }
    
    /**
     * Helper method to check for a request's result.
     * @param reqId
     * @return - TweetMoodResponse
     */
    private TweetMoodResponse moodAnalysisResponse(long reqId) {
        
        DBHelper dbHelper = (DBHelper) context.getAttribute(Constants.DB_HELPER_ATTR);
        AssertUtil.assertField(dbHelper);
        
        TweetMoodResponse response;
        try {
            TweetRequest request = dbHelper.getTweetRequest(reqId);
            
            if (request == null) {
                response =  new TweetMoodErrorResponse(reqId, "Invalid Request Id");
            } else {
                long parentId = request.getParentId();
                
                // Is this a top level request? Adjust the parentRequest accordingly
                TweetRequest parentRequest =  parentId !=0 ?
                                                dbHelper.getTweetRequest(parentId) :
                                                request;
                
                if (parentRequest.getStatus().equals(Constants.TR_PENDING_STATUS)) {
                    // For ongoing request, return a Pending response
                    response = new TweetMoodPendingResponse(reqId);
                } else if (parentRequest.getStatus().equals(Constants.TR_FAILED_STATUS)) {
                    // For a failed request, retunr an Error response. Failed requests can always be restarted.
                    response = new TweetMoodErrorResponse(reqId, parentRequest.getStatusMessage());
                    updateStatus(reqId, Constants.TR_FAILED_STATUS, parentRequest.getStatusMessage());
                } else {
                    // For completed requests, return the analyzed tweets
                    response = dbHelper.getTweets(parentRequest.getRequestId());
                    if (parentId != 0 && !request.getStatus().equals(Constants.TR_COMPLETED_STATUS)) {
                        updateStatus(reqId, Constants.TR_COMPLETED_STATUS, parentRequest.getStatusMessage());
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
            throw new RuntimeException(e.toString());
        }
        
        return response;
    }

    /**
     * Process request in a CompletableFuture chain.
     * @param normalizedSearch
     * @param reqId
     */
    private void processRequest(String normalizedSearch, long reqId) {
        
        TwitterHelper twitterHelper = (TwitterHelper) context.getAttribute(Constants.TWITTER_HELPER_ATTR);
        AssertUtil.assertField(twitterHelper);
        
        IDOLServiceHelper idolHelper = (IDOLServiceHelper) context.getAttribute(Constants.IDOL_SVC_ATTR);
        AssertUtil.assertField(idolHelper);
        
        DBHelper dbHelper = (DBHelper) context.getAttribute(Constants.DB_HELPER_ATTR);
        AssertUtil.assertField(dbHelper);
        
        // Process Request Chain
        
        // First obtain the tweets from Twitter
        CompletableFuture.supplyAsync(() -> twitterHelper.searchTweets(normalizedSearch))
        
        // Handle any errors
        .handle((tweets, exc) -> {
            if (exc != null) {
                updateStatus(reqId, Constants.TR_FAILED_STATUS, exc.getMessage());
                exc.printStackTrace();
            }
            return tweets;
        })
        
        // Then do sentiment analysis using IDOLOnDemand Sentiment Analysis API
        .thenApply((tweets) -> idolHelper.doSentimentAnalysis(tweets))
        
        // Handle any errors
        .handle((tweets, exc) -> {
            if (exc != null) {
                updateStatus(reqId, Constants.TR_FAILED_STATUS, exc.getMessage());
                exc.printStackTrace();
            }
            return tweets;
        })
        
        // Accept the analyzed data and store into the DB
        .thenAccept((tweets) -> {
            try {
                dbHelper.addTweets(reqId, tweets);
                updateStatus(reqId, Constants.TR_COMPLETED_STATUS, "ok");
            } catch (Throwable th) {
                updateStatus(reqId, Constants.TR_FAILED_STATUS, th.getMessage());
                
                th.printStackTrace();
                throw new RuntimeException(th.toString());
            } 
        }); 
    }
    
    /**
     * Updates the status of a request. Done here so as to properly handle SQLException thrown by
     * DBHelper method
     * @param reqId
     * @param status
     * @param statusMessage
     */
    private void updateStatus(long reqId, String status, String statusMessage) {
        
        DBHelper dbHelper = (DBHelper) context.getAttribute(Constants.DB_HELPER_ATTR);
        
        AssertUtil.assertField(dbHelper);
        
        try {
            dbHelper.updateStatus(reqId, status, statusMessage);
        } catch (SQLException sqe) {
            sqe.printStackTrace();
            LOG.warning("Failed to update the status of reqId="+reqId);
        }
    }
    
    /**
     * Normalizes the query string as follows:
     * - Trim leading/trailing spaces
     * - Remove duplicate whitespaces and replace with a single space
     * - Convert to lower case (twitter searches are not case sensitive)
     * @param query
     * @return normalized query string
     */
    private String normalize(String query) {
        return query.trim().replaceAll("\\s+", " ").toLowerCase();
    }
}
