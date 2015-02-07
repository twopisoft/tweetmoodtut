package com.twopi.tutorial.servlet;

import com.twopi.tutorial.utils.Constants;

/**
 * A special sub-class of TweetMoodResponse class to indicate that response is still pending
 * @author arshad01
 *
 */
public final class TweetMoodPendingResponse extends TweetMoodResponse {
    
    public TweetMoodPendingResponse(long reqId) {
        super(reqId,Constants.TR_PENDING_STATUS);
    }
    
    @Override
    public final int getTweetCount() {
        return 0;
    }
}
