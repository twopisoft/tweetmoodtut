package com.twopi.tutorial.utils;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class AssertUtil {

    /**
     * Assert the validity of parameter when inside a web service
     * @param param
     * @return
     */
    public static boolean assertParamSvc(String param) {
        if (param == null || param.equals("")) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        return true;
    }
    
    /**
     * Assert the validity of parameter (for local use)
     * @param param
     * @return
     */
    public static boolean assertParam(String param) {
        if (param == null || param.equals("")) {
            throw new IllegalArgumentException("Invalid parameter");
        }
        return true;
    }
    
    /**
     * Assert the validity of an object when inside a web service
     * @param obj
     * @return
     */
    public static boolean assertFieldSvc(Object obj) {
        if (obj == null) {
            throw new WebApplicationException(Response.Status.SERVICE_UNAVAILABLE);
        }
        
        return true;
    }
    
    /**
     * Assert the validity of an object (for local use)
     * @param obj
     * @return
     */
    public static boolean assertField(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Invalid parameter");
        }
        
        return true;
    }
}
