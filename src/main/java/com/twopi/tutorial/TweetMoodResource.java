
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
import com.twopi.tutorial.servlet.TweetMoodPendingResponse;
import com.twopi.tutorial.servlet.TweetMoodResponse;
import com.twopi.tutorial.twitter.TwitterHelper;
import com.twopi.tutorial.utils.AssertUtil;
import com.twopi.tutorial.utils.Constants;

import java.sql.SQLException;
import java.util.logging.Logger;

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
        
        TwitterHelper twitterHelper = (TwitterHelper) context.getAttribute(Constants.TWITTER_HELPER_ATTR);
        AssertUtil.assertField(twitterHelper);
        
        IDOLServiceHelper idolHelper = (IDOLServiceHelper) context.getAttribute(Constants.IDOL_SVC_ATTR);
        AssertUtil.assertField(idolHelper);
        
        DBHelper dbHelper = (DBHelper) context.getAttribute(Constants.DB_HELPER_ATTR);
        AssertUtil.assertField(dbHelper);
        
        TweetRequest request;
        try {
            request = dbHelper.addRequest(normalize(search));
            
            AssertUtil.assertField(request);
            
            if (request.getRequestId() == request.getParentId()) {
                //fire off the sentiment analysis chain
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new RuntimeException(e.toString());
        }
        
        return request;
    }
    
    @GET
    @Produces("application/json")
    @Path("getmoodpoll/{reqid}")
    public TweetMoodResponse getMoodPoll(@PathParam("reqid") String reqid) {
        LOG.info("reqId="+reqid);
        
        AssertUtil.assertParam(reqid);
        
        return new TweetMoodPendingResponse();
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
