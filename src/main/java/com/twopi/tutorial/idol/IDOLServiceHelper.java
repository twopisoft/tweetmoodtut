package com.twopi.tutorial.idol;

import java.util.logging.Logger;

/**
 * Class providing methods to invoke IDOLOnDemand API requests.
 * 
 * @author arshad01
 *
 */
public class IDOLServiceHelper {
    
    private final static Logger LOG = Logger.getLogger(IDOLServiceHelper.class.getName());
    
    private String _apiKey = null;

    public IDOLServiceHelper(String apiKey) {
        this._apiKey = apiKey;
    }
}
