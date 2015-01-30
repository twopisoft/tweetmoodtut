
package com.twopi.tutorial;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import java.util.logging.Logger;

@Path("/")
public class TweetMoodResource {
	
	private final static Logger LOG = Logger.getLogger(TweetMoodResource.class.getName());
    
    /** Method processing HTTP GET requests, producing "text/plain" MIME media
     * type.
     * @return String that will be send back as a response of type "text/plain".
     */
    @GET 
    @Produces("text/plain")
    @Path("get")
    public String getIt() {
        return "Hi there!";
    }
}
