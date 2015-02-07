package com.twopi.tutorial.servlet;

import com.twopi.tutorial.utils.Constants;

/**
 * Class for representing an Error response. Also includes the failure reason
 * @author twopi
 *
 */
public final class TweetMoodErrorResponse extends TweetMoodResponse {

    private String _failReason;
    
    public TweetMoodErrorResponse(long reqId, String failReason) {
        super(reqId, Constants.TR_FAILED_STATUS);
        _failReason = failReason;
    }
    
    public final String getFailReason() {
        return _failReason;
    }

    /**
     * @param failReason the failReason to set
     */
    public void setFailReason(String failReason) {
        _failReason = failReason;
    }
}
